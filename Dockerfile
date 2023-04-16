FROM openjdk:17

COPY . .
RUN chmod +X ./gradlew
RUN ./gradlew bootJar

FROM openjdk:17 AS builder
COPY --from=builder build/libs/*.jar dream.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/dream.jar"]