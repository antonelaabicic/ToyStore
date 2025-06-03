FROM eclipse-temurin:21-jdk-alpine
MAINTAINER algebra.hr
COPY target/toystore-1.0.0.jar demo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/demo.jar"]
