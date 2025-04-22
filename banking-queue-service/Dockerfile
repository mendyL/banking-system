# ── BUILD STAGE ───────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# 1) Copier et installer le POM parent (non‑récursif)
COPY pom.xml .
RUN mvn install -N -B

# 2) Copier TOUS les modules déclarés dans le parent
COPY banking-partner-service banking-partner-service
COPY banking-queue-service    banking-queue-service
COPY partners-front           partners-front

RUN mvn clean package \
    -pl banking-queue-service -am \
    -DskipTests \
    spring-boot:repackage \
    -B

# ── RUNTIME STAGE ─────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 4) Récupérer le JAR exécutable
COPY --from=build /app/banking-queue-service/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]
