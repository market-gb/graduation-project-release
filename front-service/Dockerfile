FROM openjdk:17
ARG JAR_FILE=front-service/target/front-service-0.0.2-exec.jar
COPY ${JAR_FILE} front.jar
ENTRYPOINT ["java", "-jar", "/front.jar"]