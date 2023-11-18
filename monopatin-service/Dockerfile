FROM ubuntu:latest
LABEL authors="Yamila"
COPY target/monopatin-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8089
CMD ["java", "-jar", "app.jar"]
ENTRYPOINT ["top", "-b"]
