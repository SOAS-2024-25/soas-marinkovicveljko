FROM eclipse-temurin:17
EXPOSE 8761
ADD target/naming-server-1.00.jar /app/naming-server.jar
ENTRYPOINT [ "java", "-jar", "/app/naming-server.jar" ]