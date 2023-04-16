# builder stage
FROM openjdk:17 AS builder
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew bootJar
RUN mv build/libs/dream.jar /dream.jar

# run stage
FROM openjdk:17
COPY --from=builder /dream.jar /dream.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/dream.jar"]