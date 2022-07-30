FROM openjdk:17
ARG JAR_FILE=config-service/target/config-service-0.0.1-exec.jar
COPY ${JAR_FILE} config.jar
COPY ./server.jks /tmp/server.jks
ENTRYPOINT ["java", "-jar", "/config.jar"]