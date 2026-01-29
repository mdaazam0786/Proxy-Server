# Use OpenJDK 21 slim
FROM amazoncorretto:21

# Create a work directory
WORKDIR /app

# Copy the JAR from proxy-server directory
COPY proxy-server/target/proxy-server-0.0.1-SNAPSHOT.jar app.jar

# Expose your proxy port (Render will override with its own PORT)
EXPOSE 8081

# Default entrypoint to run the app
ENTRYPOINT ["java","-jar","/app/app.jar"]
