FROM openjdk:17

WORKDIR /app

COPY . .

RUN apt-get update && \
    apt-get install -y --no-install-recommends findutils && \
    chmod +x ./gradlew && \
    ./gradlew bootJar

RUN mv build/libs/dream.jar /dream.jar

CMD ["java", "-jar", "/dream.jar"]