# 📌 RÔLE : Image Docker du backend Spring Boot.
# 📌 POINTS CLÉS :
#   - Build multi-stage : Maven pour compiler, JRE pour exécuter
#   - Image finale légère (~180 Mo au lieu de ~500 Mo)
#   - Utilisateur non-root pour la sécurité
# 📌 QUESTION POSSIBLE : "Pourquoi multi-stage ?"
#     → L'image finale ne contient pas Maven ni le code source, juste le JAR

# --- Étape 1 : Build avec Maven ---
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copie du pom.xml d'abord (cache Docker optimal)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copie du code source et build
COPY src ./src
RUN mvn clean package -DskipTests

# --- Étape 2 : Runtime avec JRE ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Utilisateur non-root (bonne pratique sécurité)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copie du JAR depuis l'étape de build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]