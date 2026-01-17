#!/bin/bash

set -e

echo "========================================"
echo "Building complete SOA3 project"
echo "========================================"

# Build called-service (EJB)
echo ""
echo "[1/3] Building called-service (EJB)..."
cd called-service
./gradlew clean :called-ejb:jar
echo "✓ called-ejb built successfully"

# Copy to payara2 for autodeploy
echo ""
echo "[2/3] Copying called-ejb.jar to payara2..."
cp called-ejb/build/libs/called-ejb.jar ../payara2/called-ejb.jar
echo "✓ JAR copied to payara2 directory"

# Build Spring Cloud services
echo ""
echo "[3/3] Building Spring Cloud services..."
cd ../spring-cloud
./gradlew clean build -x test
echo "✓ Spring Cloud services built successfully"

echo ""
echo "========================================"
echo "Build completed successfully!"
echo "========================================"
echo ""
echo "Next steps:"
echo "  1. Run: docker-compose up --build"
echo "  2. Check WSDL at: http://localhost:8080/MovieService?wsdl"
echo "  3. Test REST API at: https://localhost:8088/called/api/..."
echo ""
