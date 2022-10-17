import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final ServerSocket server;
    private int PORT = 3008;

    public Server(int port) {
        this.PORT = port;
        try {
            server = new ServerSocket(this.PORT);
            System.out.println("Server Listening in port " + this.PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Server() {
        try {
            server = new ServerSocket(this.PORT);
            System.out.println("Server Listening on port " + this.PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void listen() {
        while (true) {
            try {
                Socket clientConnected = server.accept();
                System.out.println("Client " + clientConnected.getInetAddress() + " connected");
                new Controller(clientConnected).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }


}
