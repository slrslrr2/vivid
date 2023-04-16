FROM openjdk:17

COPY . .
#RUN chmod +X ./gradlew
#RUN ./gradlew bootJar

#FROM openjdk:17 AS builder
COPY logs/dream-logback.log dream-logback2.log
COPY build/libs/*.jar dream.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/dream.jar"]