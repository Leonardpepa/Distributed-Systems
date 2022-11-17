import Controller.Server;
import Model.DatabaseConnector;

import java.rmi.RemoteException;

public class Main {
    public static void main(String[] args) {
        DatabaseConnector.initDB("bank", 0);
        try {
            Server server = new Server();

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}