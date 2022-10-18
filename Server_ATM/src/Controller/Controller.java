package Controller;

import Model.AccountRepository;
import Model.DatabaseConnector;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Controller extends Thread {

    private final int id = -1;
    private Socket clientSocket = null;
    private ObjectInputStream input = null;
    private ObjectOutputStream output = null;
    private DatabaseConnector connector = null;
    private AccountRepository repository = null;

    public Controller(Socket socket) {
        this.clientSocket = socket;
        try {
            this.input = new ObjectInputStream(socket.getInputStream());
            this.output = new ObjectOutputStream(socket.getOutputStream());
            connector = new DatabaseConnector("bank");
            repository = new AccountRepository(connector.getDbConnection(), "account");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public synchronized void start() {
        super.start();
        while (true) {
            try {
                Request request = (Request) input.readObject();
                ServerProtocol serverProtocol = new ServerProtocol(request, repository);
                output.writeObject(serverProtocol.proccessRequest());
            } catch (IOException e) {
//                e.printStackTrace();
                System.out.println("Client " + clientSocket.getInetAddress() + " disconnected");
                return;
            } catch (ClassNotFoundException e) {
                System.out.println("Client " + clientSocket.getInetAddress() + " disconnected");
//                e.printStackTrace();
                return;
            } catch (NullPointerException e) {
                System.out.println("Client " + clientSocket.getInetAddress() + " disconnected");
//                e.printStackTrace();
                return;
            }
        }
    }
}
