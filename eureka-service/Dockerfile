FROM openjdk:17
ARG JAR_FILE=eureka-service/target/eureka-service-0.0.1-exec.jar
COPY ${JAR_FILE} eureka.jar
ENTRYPOINT ["java", "-jar", "/eureka.jar"]