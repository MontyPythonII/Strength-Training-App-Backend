# syntax=docker/dockerfile:1

FROM eclipse-temurin:25-jdk AS build
WORKDIR /build

COPY mvnw pom.xml ./
COPY .mvn .mvn
COPY src src

# The context-load test needs a live database, which doesn't exist while the image is building
RUN --mount=type=cache,target=/root/.m2 ./mvnw -B package -DskipTests


FROM eclipse-temurin:25-jre
WORKDIR /app

# Run unprivileged so a compromised app isn't root inside the container
RUN useradd --system --no-create-home workout
USER workout

COPY --from=build /build/target/workout-backend-*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
