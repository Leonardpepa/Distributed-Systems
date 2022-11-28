FROM python:3.10
COPY . /app
WORKDIR /app
RUN pip install grpcio-tools
RUN  python -m grpc_tools.protoc -I proto --python_out=generated/ --pyi_out=generated/ --grpc_python_out=generated/  proto/bank.proto
CMD [ "python", "client.py" ]


# docker build -f Python_client_GRPC/Dockerfile -t grpc-client Python_Client_GRPC

# docker run -it --rm --network host grpc-client
