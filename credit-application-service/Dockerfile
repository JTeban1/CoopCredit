# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy and install parent POM first
COPY pom.xml ./parent-pom.xml
RUN mvn install:install-file -Dfile=parent-pom.xml -DgroupId=com.riwi -DartifactId=coopcredit-parent -Dversion=1.0.0 -Dpackaging=pom

# Copy microservice files
COPY credit-application-service/pom.xml ./pom.xml
COPY credit-application-service/src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
