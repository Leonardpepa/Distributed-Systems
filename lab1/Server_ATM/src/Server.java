import Controller.Controller;
import Model.DatabaseConnector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Server {

    public static ConcurrentHashMap<Integer, ReentrantReadWriteLock> locks;
    private final ServerSocket server;
    private int PORT = 8080;

    public Server(int port) {
        this.PORT = port;
        try {
            // make sure that database and table exist
            DatabaseConnector.initDB("bank");
            server = new ServerSocket(this.PORT);
            locks = new ConcurrentHashMap<>();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Server() {
        try {
            // make sure that database and table exist
            DatabaseConnector.initDB("bank");
            server = new ServerSocket(this.PORT);
            locks = new ConcurrentHashMap<>();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void listen() {
        System.out.println("Server Listening on port: " + this.PORT);
        while (true) {
            try {
                Socket clientConnected = server.accept();
                System.out.println("Client " + clientConnected.getInetAddress() + " connected");
                Controller controllerThread = new Controller(clientConnected, locks);
                controllerThread.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }


}
