FROM gradle:7.5-jdk17
COPY . /app
WORKDIR /app
RUN gradle
RUN gradle clean build
CMD ["gradle", "run"]