# Use OpenJDK 21
FROM amazoncorretto:21

# Create a work directory
WORKDIR /app

# Copy the JAR from proxy-server directory
COPY target/proxy-server-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8081

# Run the application
ENTRYPOINT ["java","-jar","/app/app.jar"]
