# Stage 1
## Start with a Maven image that includes JDK 21
FROM maven:3.9.8-amazoncorretto-21-debian AS build

# Copy source code and pom.xml file to /app folder
WORKDIR /app
COPY pom.xml .
COPY src ./src

## Build source code with maven
RUN mvn package -DskipTests

# Stage 2
## Start with Amazon Correto JDK 21
FROM amazoncorretto:21.0.4
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]