FROM openjdk:17 AS builder
RUN chmod +X ./gradlew
RUN ./gradlew bootJar

COPY --from=builder build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
#WORKDIR /app/dream/
#RUN mkdir -p /app/dream && \
#    mkdir -p /app/logs && \
#    ln -s /app/logs/ /app/dream/logs
#
#COPY build/libs/dream.jar dream.jar
#
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "dream.jar"]