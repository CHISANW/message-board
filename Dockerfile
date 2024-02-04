FROM adoptopenjdk:11-jre-hotspot

WORKDIR /app

COPY build/libs/board-0.0.1-SNAPSHOT.jar /app/board.jar

EXPOSE 8080

CMD ["java", "-jar", "board.jar"]