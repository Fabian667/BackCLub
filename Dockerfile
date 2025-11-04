# syntax=docker/dockerfile:1

# Etapa de build: compila el JAR con Maven Wrapper en JDK 21
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

# Copiamos todo el proyecto (incluye mvnw y .mvn/) y preparamos wrapper
COPY . .
RUN chmod +x mvnw \
 && ./mvnw -B -DskipTests package

# Etapa de runtime: imagen más liviana solo con JRE 21
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiamos el artefacto generado
COPY --from=build /workspace/target/*.jar app.jar

# Puerto de la aplicación
EXPOSE 8080

# Variables de entorno típicas para sobreescribir configuración de Spring (opcional)
ENV JAVA_OPTS="" \
    SPRING_PROFILES_ACTIVE=default

# Entrypoint
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]