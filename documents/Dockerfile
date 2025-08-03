FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app
COPY gradlew .
COPY gradle ./gradle
RUN chmod +x gradlew
COPY settings.gradle.kts build.gradle.kts ./
COPY libs libs
COPY services/documents ./services/documents
RUN ./gradlew :services:documents:bootJar --no-daemon
FROM eclipse-temurin:21-jre-jammy
EXPOSE 8080
COPY --from=build /app/services/documents/build/libs/app.jar /app.jar
ARG DOCUMENTS_DATABASE_URL
ARG DOCUMENTS_DATABASE_USERNAME
ARG DOCUMENTS_DATABASE_PASSWORD
ENTRYPOINT java -jar "/app.jar"
