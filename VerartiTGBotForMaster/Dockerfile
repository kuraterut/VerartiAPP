# Используем официальный образ OpenJDK
FROM eclipse-temurin:17-jdk-jammy

# Рабочая директория в контейнере
WORKDIR /app

# Копируем JAR-файл (предварительно собранный Maven/Gradle)
COPY target/VerartiTGBotForMaster-0.0.1-SNAPSHOT.jar app.jar

# Указываем порт (если приложение использует web-сервер)
EXPOSE 8080

# Команда запуска
ENTRYPOINT ["java", "-jar", "app.jar"]