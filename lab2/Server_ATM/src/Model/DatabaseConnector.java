package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {
    private Connection dbConnection = null;

    public DatabaseConnector(String databaseName) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            this.dbConnection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/" + databaseName, "root", "");
        } catch (SQLException e) {
            System.err.println("Error with sql server please check if sql server is up!");
            try {
                dbConnection.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.exit(1);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void initDB(String dbName, int numberOfTries) {
        Connection conn = null;
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/", "root", "");
            executeInitializationStatments(conn, dbName);

        } catch (SQLException e) {
            if (numberOfTries >= 5) {
                System.err.println("Error | Timeout trying to connect to the database");
                return;
            }

            try {
                Thread.sleep(2000);
                initDB("bank", numberOfTries + 1);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (conn == null) return;
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void executeInitializationStatments(Connection conn, String dbName) throws SQLException {
        Statement statement = conn.createStatement();
        statement.executeQuery("CREATE DATABASE IF NOT EXISTS " + dbName);
        statement.executeQuery("CREATE TABLE IF NOT EXISTS " + dbName + ".`account` (`id` INT NOT NULL , `pin` INT NOT NULL , `name` VARCHAR(255) NOT NULL , `balance` DOUBLE NOT NULL );");

        System.out.println("Database server started successfully");
    }

    public Connection getDbConnection() {
        return dbConnection;
    }

}
