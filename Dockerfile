FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/banking-app-*.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
