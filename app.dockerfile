FROM gradle:jdk22 as builder
WORKDIR /app
COPY . .
RUN ./gradlew clean build

FROM openjdk:22-jdk
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]