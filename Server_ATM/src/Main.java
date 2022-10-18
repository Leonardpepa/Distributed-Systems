import Model.DatabaseConnector;

public class Main {
    public static void main(String[] args) {
        DatabaseConnector.initDB("bank");
        Server server = new Server();
        server.listen();
    }
}