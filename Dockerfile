FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/*jar-with-dependencies.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

EXPOSE 2121
EXPOSE 2020
