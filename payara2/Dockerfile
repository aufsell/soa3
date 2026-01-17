FROM payara/server-full:5.2022.5-jdk11

# Copy required libraries
COPY postgresql-42.7.2.jar /opt/payara/appserver/glassfish/domains/domain1/lib/
COPY jackson-datatype-jsr310-2.17.2.jar /opt/payara/appserver/glassfish/domains/domain1/lib/
COPY jackson-databind-2.17.2.jar /opt/payara/appserver/glassfish/domains/domain1/lib/
COPY jackson-core-2.17.2.jar /opt/payara/appserver/glassfish/domains/domain1/lib/
COPY jackson-annotations-2.17.2.jar /opt/payara/appserver/glassfish/domains/domain1/lib/
COPY jjwt-api-0.11.5.jar /opt/payara/appserver/glassfish/domains/domain1/lib/
COPY jjwt-impl-0.11.5.jar /opt/payara/appserver/glassfish/domains/domain1/lib/
COPY jjwt-jackson-0.11.5.jar /opt/payara/appserver/glassfish/domains/domain1/lib/

# Copy applications to deploy directory
RUN mkdir -p /opt/payara/deploy
COPY called-ejb.jar /opt/payara/deploy/
COPY called-soap.war /opt/payara/deploy/

# Copy post-boot commands for datasource configuration only
COPY post-boot-commands.asadmin /opt/payara/appserver/glassfish/domains/domain1/

# Set environment variable to execute post-boot commands
ENV POSTBOOT_COMMANDS=/opt/payara/appserver/glassfish/domains/domain1/post-boot-commands.asadmin

# Copy and setup deployment script
COPY --chmod=755 deploy-apps.sh /opt/payara/deploy-apps.sh