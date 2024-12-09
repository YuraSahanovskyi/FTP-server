# Використовуємо офіційний образ Maven для побудови проєкту
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

# Встановлюємо робочу директорію в контейнері
WORKDIR /app

# Копіюємо файл pom.xml і завантажуємо залежності
COPY pom.xml .
RUN mvn dependency:go-offline

# Копіюємо весь проєкт в контейнер
COPY src /app/src

# Будуємо застосунок
RUN mvn clean package -DskipTests

# Вибираємо базовий образ з JRE для запуску програми
FROM eclipse-temurin:21-jre-alpine

# Встановлюємо робочу директорію для виконання програми
WORKDIR /app

# Копіюємо побудований JAR файл з попереднього етапу
# Замініть 'your-app.jar' на фактичну назву з build
COPY --from=build /app/target/*.jar /app/app.jar

# Вказуємо команду для запуску застосунку
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# Оголошуємо порт, який використовуватиме застосунок
EXPOSE 2121
EXPOSE 2020
