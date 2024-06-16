FROM maven:3.9.6-sapmachine-21 as build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-slim
WORKDIR /app
COPY --from=build /app/target/community-0.0.1-SNAPSHOT.jar community-bot.jar
EXPOSE 8080
ENTRYPOINT ["java", "--enable-preview", "-jar", "community-bot.jar"]