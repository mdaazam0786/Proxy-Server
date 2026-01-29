# Build stage
FROM amazoncorretto:21 AS builder

WORKDIR /build

# Install Maven
RUN yum install -y maven

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

# Run the application
ENTRYPOINT ["java","-jar","/app/app.jar"]
