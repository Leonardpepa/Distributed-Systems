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
            this.dbConnection = DriverManager.getConnection("jdbc:mariadb://database:3306/" + databaseName, "root", "root");
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

    public static void initDB(String tableName) {
        Connection dbConnection = null;
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            dbConnection = DriverManager.getConnection("jdbc:mariadb://database:3306/", "root", "root");
            Statement statement = dbConnection.createStatement();
            statement.executeQuery("CREATE DATABASE IF NOT EXISTS " + tableName);
            statement.executeQuery("CREATE TABLE IF NOT EXISTS " + tableName + ".`account` (`id` INT NOT NULL , `pin` INT NOT NULL , `name` VARCHAR(255) NOT NULL , `balance` DOUBLE NOT NULL );");

            System.out.println("Database server started successfully");
        } catch (SQLException e) {
            try {
                System.out.println("Waiting for database server to start please dont close the app");
                Thread.sleep(5000);
                initDB("bank");
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (dbConnection == null) return;
                dbConnection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Connection getDbConnection() {
        return dbConnection;
    }

}
