# Build stage
FROM gradle:8.4-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# Testləri keçirik ki, build sürətli olsun və xəta verməsin
RUN ./gradlew bootJar -x test --no-daemon

# Run stage
FROM openjdk:17-jdk-slim
EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/app.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]