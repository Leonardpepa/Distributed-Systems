import Model.Account;
import Model.AccountRepository;
import Model.DatabaseConnector;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        server.listen();
    }
}