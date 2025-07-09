FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/library-management-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]