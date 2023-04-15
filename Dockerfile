FROM openjdk:17
WORKDIR /app/dream/
RUN mkdir -p /app/dream && \
    mkdir -p /app/logs && \
    ln -s /app/logs/ /app/dream/logs

COPY ./* /app/dream

#COPY build/libs/dream.jar dream.jar

#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "dream.jar"]