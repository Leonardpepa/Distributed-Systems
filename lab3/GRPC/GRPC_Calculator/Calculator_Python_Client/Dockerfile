FROM python:3.10
COPY . /app
WORKDIR /app
RUN pip install grpcio-tools
RUN  python -m grpc_tools.protoc -I proto --python_out=generated/ --pyi_out=generated/ --grpc_python_out=generated/  proto/calculator.proto
CMD [ "python", "client.py" ]

# docker build -f Calculator_Python_Client/Dockerfile -t grpc-calculator-client Calculator_Python_Client

# docker run -it --rm --network host grpc-calculator-client
