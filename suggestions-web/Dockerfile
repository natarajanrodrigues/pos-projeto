FROM payara/server-full
MAINTAINER natarajan <natarajanrodrigues@gmail.com>

ENV DOMAIN domain1

# RUN $PAYARA_PATH/bin/asadmin start-domain --verbose ${DOMAIN} && \
# $PAYARA_PATH/bin/asadmin create-jms-resource --restype javax.jms.ConnectionFactory jms/suggCon && \
# $PAYARA_PATH/bin/asadmin create-jms-resource --restype javax.jms.Queue jms/suggQueue && \
# $PAYARA_PATH/bin/asadmin restart-domain


ENV LIB /opt/payara41/glassfish/domains/${DOMAIN}/lib/
ENV DEPLOY ${PAYARA_PATH}/glassfish/domains/${DOMAIN}/autodeploy/
ADD target/suggestions-web/WEB-INF/lib/ ${LIB}
ADD domain.xml /opt/payara41/glassfish/domains/${DOMAIN}/config/
ENTRYPOINT $PAYARA_PATH/bin/asadmin start-domain --verbose ${DOMAIN}
ADD target/suggestions-web.war  ${DEPLOY}
