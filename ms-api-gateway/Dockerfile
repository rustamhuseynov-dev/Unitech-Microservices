FROM alpine:3.18.0
RUN apk add --no-cache openjdk17
COPY build/libs/api-gateway.jar /app/app.jar
WORKDIR /app
CMD ["java", "-jar", "app.jar"]