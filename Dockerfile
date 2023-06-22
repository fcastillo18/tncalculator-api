# Stage 1: Build the application
FROM openjdk:17-jdk as builder
WORKDIR /app
COPY . .
RUN microdnf install findutils
RUN ./gradlew build -x test --no-daemon

# Stage 2: Create the final image
FROM openjdk:17-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/tncalculator-api.jar app.jar
CMD ["java", "-jar", "app.jar"]