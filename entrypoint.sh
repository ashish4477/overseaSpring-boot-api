#!/usr/bin/env bash
set -e

# password protect staging
if [[ "${ENVIRONMENT}" != "prod" ]]; then
  echo "ENVIRONMENT is ${ENVIRONMENT}, password protecting site"
  sed -i 's/\#REMOVE_FOR_AUTH#//g' /etc/apache2/sites-available/vhosts.conf
  echo "site is now password protected"


fi;

if [[ "${ENVIRONMENT}" != "dev" ]]; then
  echo "ENVIRONMENT is ${ENVIRONMENT}, ammending ovf.properties with sftp settings"
    cat << 'EOF' >> /usr/local/tomcat/conf/ovf.properties
# ivoteisrael sftp config
ivoteisraelPendingVoterRegistrationConfiguration.serverAddress=${IVOTE_ISRAEL_SERVER}
ivoteisraelPendingVoterRegistrationConfiguration.serverPort=${IVOTE_ISRAEL_PORT}
ivoteisraelPendingVoterRegistrationConfiguration.sftpUser=${IVOTE_ISRAEL_USER}
ivoteisraelPendingVoterRegistrationConfiguration.password=${IVOTE_ISRAEL_PASS}
ivoteisraelPendingVoterRegistrationConfiguration.sftpDir=${IVOTE_ISRAEL_SFTP_DIR}
EOF
fi;

# template vhosts according to env
if [[ "${ENVIRONMENT}" == "dev-eks" ]]; then
  sed -i 's/\##ENV##/dev\./g' /etc/apache2/sites-available/vhosts.conf
fi;
if [[ "${ENVIRONMENT}" == "dev" ]]; then
  sed -i 's/\##ENV##/dev\./g' /etc/apache2/sites-available/vhosts.conf
fi;
if [[ "${ENVIRONMENT}" == "stage" ]]; then
  sed -i 's/\##ENV##/stage\./g' /etc/apache2/sites-available/vhosts.conf
fi;
if [[ "${ENVIRONMENT}" == "prod" ]]; then
  sed -i 's/\##ENV##//g' /etc/apache2/sites-available/vhosts.conf
  sed -i 's/\#REMOVE_FOR_PROD#//g' /etc/apache2/sites-available/vhosts.conf
fi;

service apache2 start && \
catalina.sh run
