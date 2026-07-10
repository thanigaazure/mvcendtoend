# Use a small, supported JRE image
FROM eclipse-temurin:21-jre-jammy

# Optional: set working dir
WORKDIR /app

# Allow overriding the jar name at build time (defaults to the first jar in target)
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
