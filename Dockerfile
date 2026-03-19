# Build
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml ./
COPY src ./src

RUN mvn -B -DskipTests clean package

# Run
FROM eclipse-temurin:21-jre-jammy AS runtime

ENV APP_HOME=/opt/app
WORKDIR ${APP_HOME}

RUN groupadd --system orderGroup && \
    useradd --system --gid orderGroup --create-home --home-dir /home/orderUser orderUser

COPY --from=build /workspace/target/*.jar ${APP_HOME}/app.jar
RUN chown -R orderUser:orderGroup ${APP_HOME}

USER orderUser

EXPOSE 8081
ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "/opt/app/app.jar"]


