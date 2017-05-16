FROM payara/server-full
MAINTAINER natarajan <natarajanrodrigues@gmail.com>

ENV DOMAIN domain1

ENV LIB /opt/payara41/glassfish/domains/${DOMAIN}/lib/

ENV DEPLOY ${PAYARA_PATH}/glassfish/domains/${DOMAIN}/autodeploy/

ADD target/suggestions-web/WEB-INF/lib/ ${LIB}

ENTRYPOINT $PAYARA_PATH/bin/asadmin start-domain --verbose ${DOMAIN}

ADD target/suggestions-web.war  ${DEPLOY}
