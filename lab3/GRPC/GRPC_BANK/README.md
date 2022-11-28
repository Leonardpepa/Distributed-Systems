# Εργαστήριο 3

## Implementation with grpcio

## How to run

### Server and Database 
Run:
```terminal
docker compose up
```

### Python Client
Run:
```terminal
docker build -f Python_client_GRPC/Dockerfile -t grpc-bank-client Python_Client_GRPC

docker run -it --rm --network host grpc-bank-client
```


