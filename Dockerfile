FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Копируем только pom.xml для кеширования зависимостей
COPY pom.xml .
RUN apt-get update && apt-get install -y maven && \
    mvn dependency:go-offline -B

# Копируем исходники и собираем
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

# Устанавливаем curl для healthcheck
RUN apt-get update && \
    apt-get install -y --no-install-recommends curl && \
    rm -rf /var/lib/apt/lists/* && \
    groupadd --system spring && \
    useradd --system --gid spring --create-home spring

COPY --from=build /app/target/*.jar app.jar
RUN chown spring:spring app.jar && chmod 0444 app.jar

USER spring
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]