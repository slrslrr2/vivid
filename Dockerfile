FROM openjdk:17
WORKDIR /app/dream/
RUN mkdir -p /app/dream && \
    mkdir -p /app/logs && \
    ln -s /app/logs/ /app/dream/logs
COPY build/libs/dream.jar /app/dream/dream.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/dream/dream.jar"]