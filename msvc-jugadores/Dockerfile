FROM openjdk:17

WORKDIR /app

COPY ./target/msvc-jugadores-0.0.1-SNAPSHOT.jar .

EXPOSE 8005

ENTRYPOINT ["java", "-jar", "msvc-jugadores-0.0.1-SNAPSHOT.jar"]