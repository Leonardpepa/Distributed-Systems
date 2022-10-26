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
            this.dbConnection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/" + databaseName + "?user=root&password=");
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

    public static void initDB(String name) {
        Connection dbConnection = null;
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            dbConnection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/?user=root&password=");
            Statement statement = dbConnection.createStatement();
            statement.executeQuery("CREATE DATABASE IF NOT EXISTS " + name);
            statement.executeQuery("CREATE TABLE IF NOT EXISTS " + name + ".`account` (`id` INT NOT NULL , `pin` INT NOT NULL , `name` VARCHAR(255) NOT NULL , `balance` DOUBLE NOT NULL );");
        } catch (SQLException e) {
            System.err.println("Error with sql server please check if sql server is up!");
            System.exit(1);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            try {
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
