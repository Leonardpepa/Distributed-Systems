package org.bank.Model;

import org.bank.grpc.Bank.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class AccountRepository implements CRUDRepository<Account>{

    private Connection connection;

    private final PreparedStatement authStmt;
    private final PreparedStatement createStmt;
    private final PreparedStatement readStmt;
    private final PreparedStatement updateStmt;

    private final PreparedStatement updateLimitStmt;

    private final PreparedStatement deleteStmt = null;

    public AccountRepository(Connection connection){
        this.connection = connection;
        try {
            createStmt = connection.prepareStatement("INSERT INTO `account`(`id`, `pin`, `name`, `balance`, `limit`) VALUES (?,?,?,?,?)");
            readStmt = connection.prepareStatement("SELECT `id`, `name`, `balance`, `limit`, `date` FROM `account` WHERE `id`=? ");
            updateStmt = connection.prepareStatement("UPDATE account SET `name`=?, `balance`=?, `limit`=? WHERE `id`=?");
            authStmt = connection.prepareStatement("SELECT `id`, `name`, `date` FROM account WHERE `id`=? AND `pin`=?");
            updateLimitStmt = connection.prepareStatement("UPDATE account SET `limit`=?, `date`=? WHERE `id`=?");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Account auth(int id, int pin) {
        try {
            authStmt.clearParameters();
            authStmt.setInt(1, id);
            authStmt.setInt(2, pin);
            ResultSet res = authStmt.executeQuery();
            if (res.next()) {
                return MyUtils.deserializeAcc(res);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Account create(Account object) {
//        "INSERT INTO `account`(`id`, `pin`, `name`, `balance`, `limit`) VALUES (?,?,?,?,?)"

        try {
            createStmt.clearParameters();
            createStmt.setInt(1, object.getId());
            createStmt.setInt(2, object.getPin());
            createStmt.setString(3, object.getName());
            createStmt.setDouble(4, 0);
            createStmt.setDouble(5, 900);

            if (createStmt.executeUpdate() == 0) {
                return null;
            }

            System.out.println("Created: " + object);
            return object;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Account read(int id) {
//        "SELECT `id`, `name`, `balance`, `limit`, `date` FROM `account` WHERE `id`=? "
        try {
            readStmt.clearParameters();
            readStmt.setInt(1, id);

            ResultSet res = readStmt.executeQuery();
            if (!res.next()) {
                return null;
            }
            Account acc = MyUtils.deserializeAcc(res);
            System.out.println("read: " + acc);
            return acc;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Account update(Account object) {
//      "UPDATE account SET `name`=?, `balance`=?, `limit`=? WHERE `id`=?"

        try {
            updateStmt.clearParameters();
            updateStmt.setString(1, object.getName());
            updateStmt.setDouble(2, object.getBalance());
            updateStmt.setDouble(3, object.getLimit());
            updateStmt.setInt(4, object.getId());

            if (updateStmt.executeUpdate() == 0) {
                return null;
            }
            System.out.println("Updated: " + object);
            return object;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Account updateDailyLimit(int id, double limit){
        try {
            java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
            updateLimitStmt.clearParameters();
            updateLimitStmt.setDouble(1, limit);
            updateLimitStmt.setDate(2, sqlDate);
            updateLimitStmt.setInt(3, id);

            if (updateStmt.executeUpdate() == 0) {
                return null;
            }
            Account acc = read(id);
            System.out.println("Updated: " + acc);
            return acc;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Account delete(Account object) {
//      "SELECT `id`, `name`, `date` FROM account WHERE `id`=? AND `pin`=?"
        return null;

    }
}
