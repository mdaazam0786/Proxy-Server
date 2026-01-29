# Build stage
FROM maven:3.9.6-amazoncorretto-21 AS builder

WORKDIR /build

# Copy the entire project
COPY . .

# Build the project
RUN cd proxy-server && mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:21

WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /build/proxy-server/target/proxy-server-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8081

# Run the application with CLI arguments
# Port and origin will be passed as environment variables or command line args
ENTRYPOINT ["java","-jar","/app/app.jar"]
CMD ["--server.port=${PORT:-8081}", "--proxy.origin=${PROXY_ORIGIN}"]
