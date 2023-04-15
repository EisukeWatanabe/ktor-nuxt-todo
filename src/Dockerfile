FROM openjdk:11

WORKDIR /app

COPY . .

RUN ./gradlew build

ENTRYPOINT ["java", "-jar", "todo-api-1.0.0.jar"]
