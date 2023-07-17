FROM adoptopenjdk:17-jdk-hotspot

WORKDIR /app

COPY target/my-spring-app.jar /app/my-spring-app.jar

EXPOSE 8080

CMD ["java", "-jar", "my-spring-app.jar"]