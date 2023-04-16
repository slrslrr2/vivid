FROM openjdk:17

COPY . .
COPY logs/dream-logback.log dream-logback2.log
COPY build/libs/*.jar dream.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/dream.jar"]