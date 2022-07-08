FROM openjdk:17
ARG JAR_FILE=core-service/target/core-service-0.0.1-exec.jar
COPY ${JAR_FILE} core.jar
ENTRYPOINT ["java", "-jar", "/core.jar"]