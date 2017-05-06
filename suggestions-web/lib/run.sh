./payara41/glassfish/bin/startserv

./payara41/bin/asadmin create-jms-resource --restype javax.jms.ConnectionFactory jms/suggCon

./payara41/bin/asadmin create-jms-resource --restype javax.jms.Queue jms/suggQueue

cp target/ROOT.war ./payara/glassfish/domains/domain1/autodeploy/

./payara41/bin/asadmin start-domain --verbose domain1