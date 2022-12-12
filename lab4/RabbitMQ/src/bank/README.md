## Run RabbitMQ with docker
```terminal
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.11-management
```

## How to run
1. run the RabbitMQ server
2. start a mysql server or mariadb server on port 3306
3. navigate to RabbitMQ/Scripts and run the activate file to activate the env
4. run server in src/bank/controller/queue_rpc_server.py
5. run client in src/bank/view/client.py

