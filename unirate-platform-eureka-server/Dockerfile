FROM openjdk:17-jdk-slim AS build

WORKDIR /build
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw
RUN ./mvnw dependency:resolve

COPY src src
RUN ./mvnw package -DskipTests

# Финальный этап
FROM openjdk:17-jdk-slim
WORKDIR /demo
COPY --from=build /build/target/*.jar demo.jar
EXPOSE 8087
ENTRYPOINT ["java", "-jar", "demo.jar"]