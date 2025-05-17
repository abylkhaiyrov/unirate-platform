FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/bala-baqsham-lending-0.0.1-SNAPSHOT.jar /app/bala-baqsham-lending.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/bala-baqsham-lending.jar"]