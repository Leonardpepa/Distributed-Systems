FROM openjdk:17
COPY . /app
WORKDIR /app
EXPOSE 5050
RUN ./gradlew clean
RUN ./gradlew build
CMD ["./gradlew", ":runServer"]
