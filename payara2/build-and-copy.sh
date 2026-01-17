#!/bin/bash

# Script to build called-ejb and copy to payara2 directory for autodeploy

echo "Building called-ejb..."
cd ../called-service
./gradlew :called-ejb:jar

echo "Copying called-ejb.jar to payara2..."
cp called-ejb/build/libs/called-ejb.jar ../payara2/

echo "Done!"
