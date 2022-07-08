FROM openjdk:17
ARG JAR_FILE=gateway-service/target/gateway-service-0.0.1-exec.jar
COPY ${JAR_FILE} gateway.jar
ENTRYPOINT ["java", "-jar", "/gateway.jar"]