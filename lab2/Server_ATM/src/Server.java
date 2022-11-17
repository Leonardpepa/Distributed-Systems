import Controller.ServerProtocolImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    private static final String HOST = "localhost";
    private static final int PORT = Registry.REGISTRY_PORT;
    public Server() throws RemoteException {
        System.setProperty("java.rmi.server.hostname", HOST);
        ServerProtocolImpl serverApi = new ServerProtocolImpl();
        Registry registry = LocateRegistry.createRegistry(PORT);
        String remoteObjectName = "ATM_API";
        registry.rebind(remoteObjectName, serverApi);
        System.out.println("Server listening on port " + PORT);
    }

}
