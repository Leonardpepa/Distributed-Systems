FROM gradle:7.5-jdk17
COPY . /app
WORKDIR /app
EXPOSE 5050
RUN gradle
RUN gradle clean build
CMD ["gradle", "run"]
