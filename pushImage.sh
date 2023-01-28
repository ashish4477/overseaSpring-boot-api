#!/usr/bin/env bash
set -e

GIT_SHA=$(git rev-parse HEAD)
IMAGE_TAG="477658251325.dkr.ecr.us-east-1.amazonaws.com/usvote/overseasvote:${GIT_SHA}"
$(aws ecr get-login --no-include-email)

# SITE_PASSWORD set via gitlab env. only enabled on stage
docker build \
  --build-arg SITE_PASSWORD="${SITE_PASSWORD}" \
  --build-arg SKIMM_SITE_PASSWORD="${SKIMM_SITE_PASSWORD}" \
  --build-arg VOTE411_SITE_PASSWORD="${VOTE411_SITE_PASSWORD}" \
  --tag $IMAGE_TAG .   
docker push $IMAGE_TAG
