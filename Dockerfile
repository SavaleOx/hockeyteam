FROM eclipse-temurin:25-jre
WORKDIR /app

# Создаём папку для логов
RUN mkdir -p /app/logs
# Даём права на запись всем пользователям (или конкретному, под которым работает приложение)
RUN chmod 777 /app/logs

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd --system spring \
    && useradd --system --gid spring --create-home spring

# Копируем уже собранный JAR из локальной папки target
# ВАЖНО: JAR должен существовать ДО сборки образа
COPY target/*.jar app.jar

RUN chown spring:spring /app/app.jar \
    && chmod 0444 /app/app.jar

USER spring

EXPOSE 8081

HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=5 \
  CMD curl -fsS http://localhost:8081/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "/app/app.jar"]