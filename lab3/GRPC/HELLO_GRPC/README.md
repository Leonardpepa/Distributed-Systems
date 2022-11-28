# Grpc Hello Example

## Simple example to familiarize myself with GRPC
## How to run

### Server
run:
```terminal
docker build -f Dockerfile -t grpc-hello-example-image .

docker run --name grpc-hello-example  -it --rm --network host grpc-hello-example-image 
```

### Client
run:
```terminal
docker exec -it grpc-hello-example ./gradlew :runClient 
```