# ========== ЭТАП 1: СБОРКА ==========
FROM eclipse-temurin:21.0.7_6-jdk AS build

WORKDIR /workspace

COPY pom.xml .
COPY mvnw .
COPY .mvn ./.mvn
RUN chmod +x mvnw

RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw -q -B -DskipTests dependency:go-offline

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw -T 1C -q -DskipTests package

# ========== ЭТАП 2: ФИНАЛЬНЫЙ ОБРАЗ ==========
FROM eclipse-temurin:21.0.7_6-jre

WORKDIR /app

# Устанавливаем curl и создаём папку для логов
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/* \
    && mkdir -p /app/logs && chmod 755 /app/logs

# Создаём непривилегированного пользователя
RUN useradd -r -u 1001 appuser
RUN chown appuser:appuser /app/logs

# Копируем JAR
COPY --from=build /workspace/target/*.jar /app/app.jar
RUN chown appuser:appuser /app/app.jar && chmod 0444 /app/app.jar

ENV PORT=8081

USER appuser

EXPOSE 8081

HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=5 \
  CMD curl -fsS http://localhost:8081/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "/app/app.jar"]