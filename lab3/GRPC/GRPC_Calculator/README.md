# Grpc Calculator example

## Simple example to familiarize myself with GRPC

## How to run
### Server
run: 
```terminal
docker build -f Calculator_Service/Dockerfile -t grpc-calculator-server Calculator_Service

docker run --name calculator-server -it --rm grpc-calculator-server

```

### Client
run: 
```terminal
docker build -f Calculator_Python_Client/Dockerfile -t grpc-calculator-client Calculator_Python_Client

docker run -it --link calculator-server:server grpc-calculator-client python client.py

```
