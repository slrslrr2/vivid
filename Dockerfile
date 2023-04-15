FROM openjdk:17

RUN pwd
RUN ls -al

RUN mkdir -p /app/dream && \
    mkdir -p /app/logs && \
    ln -s /app/logs/ /app/dream/logs


RUN pwd
RUN ls -al

COPY build/libs/dream.jar /app/dream/dream.jar
WORKDIR /app/dream/

RUN pwd
RUN ls -al

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/dream/dream.jar"]