FROM openjdk:17-jdk-slim

WORKDIR /app

# ソースをコピー
COPY . /app

# Gradle wrapper の権限を設定
RUN chmod +x gradlew

# JAR をビルド
RUN ./gradlew bootJar --no-daemon

# JVM オプションを設定（必要なモジュールを開放）
ENV JAVA_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED"

# コンテナ起動時にビルド済み JAR を実行
CMD ["sh", "-c", "java $JAVA_OPTS -jar build/libs/forum-0.0.1-SNAPSHOT.jar"]
