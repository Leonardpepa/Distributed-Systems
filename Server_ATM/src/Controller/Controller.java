package Controller;

import Model.AccountRepository;
import Model.DatabaseConnector;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

public class Controller extends Thread {

    private int client_id = -1;
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        super.run();
        boolean errorOccurred = false;
        while (true) {
            try {
                Request request = (Request) input.readObject();
                if (client_id != -1) request.setId(client_id);
                ServerProtocol serverProtocol = new ServerProtocol(request, this);
                Response response = serverProtocol.proccessRequest();
                output.writeObject(response);
            } catch (IOException e) {
                errorOccurred = true;
                System.out.println("IO Error:  " + e.getMessage());
            } catch (ClassNotFoundException e) {
                errorOccurred = true;
                System.out.println("Class not found Error:  " + e.getMessage());
            } catch (NullPointerException e) {
                errorOccurred = true;
                System.out.println("Null Error:  " + e.getMessage());
            } finally {
                try {
                    if (errorOccurred) {
                        clientSocket.close();
                        System.out.println("Client " + clientSocket.getInetAddress() + " disconnected");
                        return;
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    public DatabaseConnector getConnector() {
        return connector;
    }

    public void setConnector(DatabaseConnector connector) {
        this.connector = connector;
    }

    public AccountRepository getRepository() {
        return repository;
    }

    public void setRepository(AccountRepository repository) {
        this.repository = repository;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }
}
