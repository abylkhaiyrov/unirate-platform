FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/unirate-platform-eureka-server-0.0.1-SNAPSHOT.jar /app/unirate-platform-eureka-server.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "/app/unirate-platform-eureka-server.jar"]