FROM openjdk:19-jdk-slim

# Set the working directory
WORKDIR /app

# Copy your Spring Boot JAR file into the container
COPY target/spring-boot-docker.jar /app/spring-boot-docker.jar

# Expose the port your application will run on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "spring-boot-docker.jar"]

