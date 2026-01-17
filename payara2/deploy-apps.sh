#!/bin/bash

echo "Waiting for Payara to start..."
sleep 60

echo "Deploying called-ejb.jar..."
/opt/payara/appserver/bin/asadmin --user admin --passwordfile=/opt/payara/passwordFile deploy --force /opt/payara/deploy/called-ejb.jar

echo "Waiting for EJB to be ready..."
sleep 10

echo "Deploying called-soap.war..."
/opt/payara/appserver/bin/asadmin --user admin --passwordfile=/opt/payara/passwordFile deploy --force /opt/payara/deploy/called-soap.war

echo "Deployment complete!"
