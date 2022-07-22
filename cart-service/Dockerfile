FROM openjdk:17
ARG JAR_FILE=cart-service/target/cart-service-0.0.3-exec.jar
COPY ${JAR_FILE} cart.jar
ENV REDIS_URL=redis://redis_db:9999
ENTRYPOINT ["java", "-jar", "/cart.jar"]