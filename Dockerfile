# Build stage
FROM amazoncorretto:21 AS builder

WORKDIR /build

# Install Maven 3.9.x
RUN yum install -y wget && \
    wget https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz && \
    tar -xzf apache-maven-3.9.6-bin.tar.gz && \
    mv apache-maven-3.9.6 /opt/maven && \
    rm apache-maven-3.9.6-bin.tar.gz

ENV PATH="/opt/maven/bin:${PATH}"

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
