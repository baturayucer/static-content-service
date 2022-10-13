#
# Build stage
#
FROM maven:3.8.5-openjdk-17-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:17-slim
COPY --from=build /home/app/target/image-service-0.0.1-SNAPSHOT.jar /usr/local/lib/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "/usr/local/lib/app.jar"]