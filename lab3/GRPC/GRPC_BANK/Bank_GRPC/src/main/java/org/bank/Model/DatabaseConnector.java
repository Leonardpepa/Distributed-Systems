package org.bank.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {

    private Connection connection = null;

    public DatabaseConnector(){
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mariadb://rpc-database:3306/bank", "root", "root");
            this.connection = connection;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void Init_DB(){
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mariadb://rpc-database:3306/bank", "root", "root");
            Statement statement = connection.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS `statement` (`id` INT NOT NULL AUTO_INCREMENT , `account_id` INT NOT NULL , `type` VARCHAR(100) NOT NULL , `message` VARCHAR(255) NOT NULL , `timestamp` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP , PRIMARY KEY (`id`));";
            statement.executeQuery(sql);

            sql = "CREATE TABLE IF NOT EXISTS `account` (`id` INT NOT NULL , `pin` INT NOT NULL , `name` VARCHAR(255) NOT NULL , `balance` DOUBLE NOT NULL , `limit` DOUBLE NOT NULL , `date` DATE NOT NULL DEFAULT CURRENT_TIMESTAMP );";
            statement.executeQuery(sql);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
