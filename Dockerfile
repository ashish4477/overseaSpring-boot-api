FROM maven:3.5.2-jdk-8-alpine AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn clean package

#FROM tomcat:9.0-jre8
FROM tomcat:9.0.20-jdk8-slim
ARG SITE_PASSWORD=NOT-SET
ARG SKIMM_SITE_PASSWORD=NOT-SET

# apache install
ENV HTTPD_VERSION 2.4.41
ENV HTTPD_SHA256 133d48298fe5315ae9366a0ec66282fa4040efa5d566174481077ade7d18ea40
ENV HTTPD_PREFIX /usr/local/apache2
ENV PATH $HTTPD_PREFIX/bin:$PATH
RUN mkdir -p "$HTTPD_PREFIX" \
	&& chown www-data:www-data "$HTTPD_PREFIX"
ENV HTTPD_PATCHES=""
RUN apt-get update && apt-get install -y apache2 && \
  htpasswd -b -c /etc/apache2/.htpasswd usvote "${SITE_PASSWORD}" && \
  htpasswd -b -c /etc/apache2/.skimmhtpasswd theskimm "${SKIMM_SITE_PASSWORD}" && \
  htpasswd -b -c /etc/apache2/.vote411htpasswd vote411 "${VOTE411_SITE_PASSWORD}" && \
	a2enmod proxy_ajp rewrite headers
# for dev
RUN mkdir -p /home/ZohoIntegration/javasdk-application \
  && mkdir -p /home/ZohoIntegration/log/

# tomcat config
COPY tomcat_conf/tomcat-users.xml /usr/local/tomcat/conf/tomcat-users.xml
COPY tomcat_conf/context.xml /usr/local/tomcat/webapps/host-manager/META-INF/context.xml
COPY apache_conf/vhosts.conf /etc/apache2/sites-available/vhosts.conf
COPY ovf.properties /usr/local/tomcat/conf/ovf.properties
RUN ln -s /etc/apache2/sites-available/vhosts.conf /etc/apache2/sites-enabled/vhosts.conf \
  && mkdir -p /var/www/www.overseasvotefoundation.org/votesmart_cache

COPY entrypoint.sh /usr/local/bin/entrypoint.sh
# RUN curl -Ls http://mirrors.koehn.com/apache/tomcat/tomcat-connectors/jk/tomcat-connectors-1.2.46-src.tar.gz -o /tmp/tomcat-connectors-1.2.46-src.tar.gz

# copy latest war file
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/vote-4.5.81.war $CATALINA_HOME/webapps/vote.war
ENTRYPOINT ["entrypoint.sh"]
