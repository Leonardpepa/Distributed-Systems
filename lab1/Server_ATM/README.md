# How to run
* First build the images and the containers
* Run first the mariadb server
* In the end run the java server

# Build MariaDB Server
```terminal
docker container create --name mariadb-server -p 3306:3306 -e MARIADB_ROOT_PASSWORD=pass -e MARIADB_PASSWORD=pass -e MARIADB_USER=user mariadb
```
# Build Java TCP Server
```terminal
    docker build -t atm/server . 
    docker container create --name server-container -it --link mariadb-server:mariadb-server -p 8080:8080 atm/server 
```
# Start MariaDB Server FIRST !IMPORTANT
```terminal
    docker container start mariadb-server
```
# Start Java TCP Server IN THE END
```terminal
    docker container start server-container --interactive
```
