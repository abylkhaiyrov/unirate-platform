FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/unirate-platform-university-0.0.1-SNAPSHOT.jar /app/unirate-platform-university.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/unirate-platform-api-gateway.jar"]