# 1. ビルドステージ（JAR作成）
FROM openjdk:17-jdk-slim AS builder

WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew bootJar --no-daemon

# 2. 開発ステージ（ホットリロード用）
FROM openjdk:17-jdk-slim AS dev

WORKDIR /app

# gradlew と gradle-wrapper.jar を同じ階層にコピー
COPY gradlew build.gradle settings.gradle ./
COPY gradle/wrapper /app/gradle/wrapper
RUN chmod +x gradlew
RUN chmod -R +r /app/gradle/wrapper

ENV SPRING_DEVTOOLS_RESTART_ENABLED=true

# ソースコードのみマウントしてホットリロード
CMD ["./gradlew", "bootRun", "--no-daemon", "--project-dir=/app"]
