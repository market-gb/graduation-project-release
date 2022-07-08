FROM openjdk:17
ARG JAR_FILE=cart-service/target/cart-service-0.0.1-exec.jar
COPY ${JAR_FILE} cart.jar
ENTRYPOINT ["java", "-jar", "/cart.jar"]