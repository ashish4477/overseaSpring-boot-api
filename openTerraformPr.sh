#!/usr/bin/env bash
# Create Pull Request for infrastructure changes
#
# Requires:
# GITLAB_ACCESS_TOKEN
# ENVIRONMENT
# CI_COMMIT_SHA
#

debug_message() { if [[ "${DEBUG}" == "true" ]]; then echo -e "DEBUG: ${1}" ; fi }

set -e

TEMP_DIR=$(mktemp -d)
APPLICATION="ovf"
SOURCE_BRANCH="master"
DESTINATION_BRANCH="master"
DEPLOYMENT_BRANCH="auto/${CI_COMMIT_SHA}"
PR_TITLE="AUTO: ${APPLICATION} ${ENVIRONMENT}"
COM_MESSAGE="Update ${ENVIRONMENT} image version"

cd "${TEMP_DIR}"
debug_message "Cloning ${SOURCE_BRANCH} branch of ${APPLICATION}/infrastructure..."
git clone -b "${SOURCE_BRANCH}" --single-branch "https://oauth2:${GITLAB_ACCESS_TOKEN}@gitlab.com/us-vote/infrastructure" "${SOURCE_BRANCH}"
git config --global user.name "usvote autobot"
git config --global user.email "usvote_autobot@gitlab.com"
cd "${SOURCE_BRANCH}"
git checkout "${SOURCE_BRANCH}"
./sops_wrapper.sh decrypt
git checkout -b "${DEPLOYMENT_BRANCH}"

if [[ "${ENVIRONMENT}" == "prod" ]]; then
  IMAGE_VERSION_LINE=$(grep IMAGE_VERSION "stage/${APPLICATION}/config.yaml")
  IMAGE_SHA=${IMAGE_VERSION_LINE#*: }
else
  IMAGE_SHA="${CI_COMMIT_SHA}"
fi;

sed -i "s/.*IMAGE_VERSION.*/IMAGE_VERSION: ${IMAGE_SHA}/" "${ENVIRONMENT}/${APPLICATION}/config.yaml"
debug_message "Updating ${DESTINATION}/${APPLICATION} docker tag to: ${IMAGE_SHA}"

sops -e -i --input-type binary "${ENVIRONMENT}/${APPLICATION}/config.yaml"
git add "${ENVIRONMENT}/${APPLICATION}/config.yaml"
git commit -m "${COM_MESSAGE}"
git push --set-upstream origin "${DEPLOYMENT_BRANCH}"
BASE_URL="https://gitlab.com/api/v4/projects"
INFRA_PROJECT_ID=14635675

BODY="{
    \"id\": \"${CI_COMMIT_SHA}\",
    \"source_branch\": \"${DEPLOYMENT_BRANCH}\",
    \"target_branch\": \"${DESTINATION_BRANCH}\",
    \"description\": \"Triggered by: ${CI_PIPELINE_URL}\",
    \"remove_source_branch\": true,
    \"title\": \"${PR_TITLE}\",
    \"merge_when_pipeline_succeeds\": true
}";

curl -s -X POST "${BASE_URL}/${INFRA_PROJECT_ID}/merge_requests" \
  --header "Content-Type: application/json" \
  --header "PRIVATE-TOKEN:${GITLAB_ACCESS_TOKEN}"  \
  --data "${BODY}" > mr_creation_response

debug_message "$(cat mr_creation_response)"


if [[ "${ENVIRONMENT}" != "prod" ]]; then
  MR_IID=$(cat mr_creation_response | jq -r '.iid')
  BODY="{
      \"id\": \"${INFRA_PROJECT_ID}\",
      \"iid\": \"${MR_IID}\",
      \"should_remove_source_branch\": true
  }";
  sleep 10;
  curl -s -X PUT "${BASE_URL}/${INFRA_PROJECT_ID}/merge_requests/${MR_IID}/merge" \
    --header "Content-Type: application/json" \
    --header "PRIVATE-TOKEN:${GITLAB_ACCESS_TOKEN}" \
    --data "${BODY}" > mr_commit_resp

  debug_message "$(cat mr_commit_resp)"

  MR_COMMIT_SHA=$(cat mr_commit_resp | jq -r '.merge_commit_sha')

  curl -s -X GET "${BASE_URL}/${INFRA_PROJECT_ID}/pipelines" \
    --header "PRIVATE-TOKEN:${GITLAB_ACCESS_TOKEN}" > tmp_resp

  PIPELINE_URL=$(cat tmp_resp | jq -r ".[] | select(.sha | contains(\"${MR_COMMIT_SHA}\")).web_url")
  if [[ "${PIPELINE_URL}" != "" ]]; then
    echo "View Pipeline At:"
    echo "${PIPELINE_URL}";
  else
    echo "Could not retrieve pipeline URL, please check"
    echo "https://gitlab.com/us-vote/infrastructure/-/pipelines"
    echo "and/or"
    echo "https://gitlab.com/us-vote/infrastructure/-/merge_requests"
    echo "to ensure the request was merged properly"
    exit 1;
  fi;

else
  WEB_URL=$(cat mr_creation_response | jq -r '.web_url')
  echo "View MR in Infra to merge:"
  echo "${WEB_URL}"
fi;
