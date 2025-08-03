FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app
COPY gradlew .
COPY gradle ./gradle
RUN chmod +x gradlew
COPY settings.gradle.kts build.gradle.kts ./
COPY core core
COPY user-service ./user-service
RUN ./gradlew :user-service:bootJar --no-daemon
FROM eclipse-temurin:21-jre-jammy
EXPOSE 8080
COPY --from=build /app/user-service/build/libs/app.jar /app.jar
ARG USERS_DATABASE_URL
ARG USERS_DATABASE_USERNAME
ARG USERS_DATABASE_PASSWORD
ENTRYPOINT java -jar "/app.jar"
