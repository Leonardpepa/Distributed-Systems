package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private final Connection dbConnection;

    public DatabaseConnector(String databaseName) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            this.dbConnection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/" + databaseName + "?user=root&password=");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getDbConnection(){
        return dbConnection;
    }

}
