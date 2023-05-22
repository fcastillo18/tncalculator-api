# Use a base image with Java 17
FROM openjdk:17-jdk

# Set the working directory
WORKDIR /app

# Copy the application JAR file
COPY build/libs/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Define the entry point
ENTRYPOINT ["java", "-jar", "app.jar"]
