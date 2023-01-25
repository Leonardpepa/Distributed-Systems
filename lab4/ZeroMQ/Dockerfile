FROM python:3.6-buster
WORKDIR /app
COPY . /app
ENV SHELL=/bin/bash
RUN apt-get update && apt-get -yy install libmariadb-dev
RUN apt-get install libmariadb3 libmariadb-dev -yy
RUN pip3 install pyzmq  mariadb
WORKDIR /app/src/bank/
CMD [ "python", "-u" , "zeromq_server.py" ] 