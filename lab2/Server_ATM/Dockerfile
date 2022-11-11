FROM openjdk:17
COPY /src /app
COPY external_jars/mariadb-java-client-3.0.8.jar /app
WORKDIR /app
EXPOSE 8080
CMD javac *.java && java -cp .:mariadb-java-client-3.0.8.jar Main
