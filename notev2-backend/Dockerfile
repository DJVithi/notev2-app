FROM   container-registry.oracle.com/java/openjdk:25

WORKDIR /app

COPY target/notev2-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]