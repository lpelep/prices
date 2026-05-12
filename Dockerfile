FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app
LABEL authors="luis"

# Copiar el wrapper de maven y el pom para descargar dependencias
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

# 2. Copiar la configuración para el checkstyle
COPY config/ config/

# Copiar el código fuente y generar el JAR
COPY src ./src
RUN ./mvnw clean package -DskipTests

# ETAPA 2: Imagen de ejecución ligera
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copiar el JAR desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Usar el punto de entrada para ejecutar la app
ENTRYPOINT ["java", "-Dspring.h2.console.settings.web-allow-others=true", "-jar", "app.jar"]
