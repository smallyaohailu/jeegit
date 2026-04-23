# syntax=docker/dockerfile:1.6
# Multi-stage build producing a small, non-root production image.

FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /src
COPY pom.xml ./
COPY jeegit-common/pom.xml jeegit-common/
COPY jeegit-tech/pom.xml jeegit-tech/
COPY jeegit-data/pom.xml jeegit-data/
COPY jeegit-ai/pom.xml jeegit-ai/
COPY jeegit-business/pom.xml jeegit-business/
COPY jeegit-app/pom.xml jeegit-app/
COPY jeegit-openapi/pom.xml jeegit-openapi/
COPY jeegit-agent-intake/pom.xml jeegit-agent-intake/
COPY jeegit-bootstrap/pom.xml jeegit-bootstrap/

RUN apt-get update && apt-get install -y --no-install-recommends maven=3.8.7-2 \
    && rm -rf /var/lib/apt/lists/*

# Warm dependency cache (tolerate failure when a child module isn't resolvable without sources)
RUN mvn -q -B -DskipTests -pl jeegit-bootstrap -am dependency:go-offline || true

COPY . .
RUN mvn -q -B -DskipTests package

FROM eclipse-temurin:21-jre-jammy AS runtime
RUN groupadd --system jeegit \
    && useradd --system --gid jeegit --home-dir /app --shell /usr/sbin/nologin jeegit
WORKDIR /app
COPY --from=build /src/jeegit-bootstrap/target/jeegit-bootstrap.jar /app/jeegit.jar
USER jeegit

EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
    CMD curl -fsS http://localhost:8080/actuator/health || exit 1

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/jeegit.jar"]
