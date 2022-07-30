FROM openjdk:17
ARG JAR_FILE=analytics-service/target/analytics-service-0.0.1-exec.jar
COPY ${JAR_FILE} analytics.jar
ENTRYPOINT ["java", "-jar", "/analytics.jar"]