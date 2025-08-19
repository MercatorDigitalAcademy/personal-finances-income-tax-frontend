# Stage 1: Build
FROM sbtscala/scala-sbt:eclipse-temurin-21.0.8_9_1.11.4_2.13.16 AS builder
WORKDIR /build
COPY . .
RUN sbt clean dist

# Stage 2: Runtime
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=builder /build/target/universal/*.zip ./
RUN apt-get update && apt-get install -y unzip && \
    unzip *.zip && rm *.zip && mv personal-finances-dashboard-frontend-*/* /app && \
    rm -rf personal-finances-dashboard-frontend

EXPOSE 9000

CMD ["sh", "-c", "rm -f /app/RUNNING_PID && /app/bin/personal-finances-dashboard-frontend -Dplay.http.secret.key=$SECRET_KEY"]