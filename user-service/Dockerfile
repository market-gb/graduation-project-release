FROM openjdk:17
ARG JAR_FILE=user-service/target/user-service-0.0.2-exec.jar
COPY ${JAR_FILE} user.jar
ENTRYPOINT ["java", "-jar", "/user.jar"]