# builder stage
FROM openjdk:17 AS builder
WORKDIR /app
COPY . .
RUN chmod +X ./gradlew
RUN ./gradlew bootJar
RUN mv build/libs/app.jar /app.jar

# run stage
FROM openjdk:17
COPY --from=builder /app.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
