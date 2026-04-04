# Build stage
FROM gradle:8.4-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

# İcazə xətasını aradan qaldırmaq üçün bu sətir çox vacibdir:
RUN chmod +x gradlew

# Build əmri (heç bir artıq simvol olmadan)
RUN ./gradlew bootJar -x test --no-daemon

# Run stage
FROM amazoncorretto:17-alpine-jdk
EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/app.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]