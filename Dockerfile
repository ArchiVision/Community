FROM openjdk:17-oracle

COPY target/community-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

CMD ["java", "-jar", "community-0.0.1-SNAPSHOT.jar"]