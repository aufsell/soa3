# syntax=docker/dockerfile:1

# Option: Use locally downloaded Mule runtime
FROM eclipse-temurin:17-jdk

WORKDIR /opt

# Copy pre-downloaded Mule runtime (download from mulesoft.com)
COPY mule-ee-distribution-standalone-4.10.2.zip ./mule-standalone.zip
RUN apt-get update && apt-get install -y unzip && \
    unzip -q mule-standalone.zip && \
    rm mule-standalone.zip && \
    mv mule-enterprise-standalone-* mule && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

ENV MULE_HOME=/opt/mule

# Copy the deployable JAR to apps folder
COPY build/restsoapmule.jar ${MULE_HOME}/apps/

EXPOSE 7777

CMD ["sh", "-c", "${MULE_HOME}/bin/mule"]
