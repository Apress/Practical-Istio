# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /build

# Copy the complete project structure first
# This ensures Maven has access to all necessary files
COPY . .

# Download dependencies in a separate layer
RUN mvn dependency:go-offline -B -pl payment-service \
    && mvn dependency:resolve-plugins -B -pl payment-service

# Copy the source code
COPY payment-service/src/ payment-service/src/

# Build the application with the spring-boot-maven-plugin
# This ensures we get an executable JAR
RUN mvn clean package -DskipTests -pl payment-service \
    -Dspring.profiles.active=kubernetes \
    -Dspring.cloud.bootstrap.enabled=false


# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Create non-root user
RUN addgroup --system --gid 1001 appgroup && \
    adduser --system --uid 1001 --ingroup appgroup appuser && \
    mkdir -p /app/logs && \
    chown -R appuser:appgroup /app

# Copy the JAR file and verify its presence
COPY --from=builder --chown=appuser:appgroup /build/payment-service/target/*.jar app.jar
RUN ls -l /app/app.jar && \
    echo "JAR file size: $(du -h /app/app.jar | cut -f1)"

USER 1001

ENV JAVA_OPTS="\
    -XX:+UseContainerSupport \
    -XX:+UseG1GC \
    -XX:MaxRAMPercentage=75"

EXPOSE 8080 8081

# Start the application using the jar command directly
# This provides better clarity about what's being executed
ENTRYPOINT ["java", "-jar", "/app/app.jar"]