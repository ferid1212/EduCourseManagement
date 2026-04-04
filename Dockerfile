# Build stage
FROM gradle:8.4-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon -x test

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
EXPOSE 8081
COPY --from=build /home/gradle/src/build/libs/*[!plain].jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
