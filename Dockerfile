FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/board-0.0.1-SNAPSHOT.jar /app/board.jar

ENTRYPOINT ["java", "-jar","board.jar"]