package Model;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountRepository implements CRUDRepository<Account> {
    private final Connection conn;
    private final String table;
    private final PreparedStatement authStmt;
    private final PreparedStatement createStmt;

    private final PreparedStatement readStmt;

    private final PreparedStatement updateStmt;

    private final PreparedStatement deleteStmt;

    public AccountRepository(Connection conn, String table) throws SQLException {
        this.conn = conn;
        this.table = table;
        authStmt = conn.prepareStatement("SELECT * FROM " + this.table + " WHERE id = ? AND pin = ? ");
        createStmt = conn.prepareStatement("INSERT INTO " + this.table + " values(?, ?, ?, 0)");
        readStmt = conn.prepareStatement("SELECT * FROM " + this.table + " WHERE id = ?");
        updateStmt = conn.prepareStatement("UPDATE " + this.table + " SET pin = ? , name = ? , balance = ? WHERE id = ? ");
        deleteStmt = conn.prepareStatement("DELETE FROM " + this.table + " WHERE id = ?;");
    }

    public Account auth(int id, int pin) {
        try {
            authStmt.clearParameters();
            authStmt.setInt(1, id);
            authStmt.setInt(2, pin);
            ResultSet res = authStmt.executeQuery();
            if (res.next()) {
                return MyUtils.decerializeAcc(res);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Account create(Account object) {
        Account found = read(object.getId());

        if (found != null) {
            System.out.println("Account already exists");
            return null;
        }
        try {
            createStmt.clearParameters();
            createStmt.setInt(1, object.getId());
            createStmt.setInt(2, object.getPin());
            createStmt.setString(3, object.getName());

            if (createStmt.executeUpdate() == 0) {
                System.out.println("Error while inserting a new account");
            }

            System.out.println("Created: " + object);
            return object;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Account read(int id) {
        try {
            readStmt.clearParameters();
            readStmt.setInt(1, id);
            ResultSet res = readStmt.executeQuery();
            if (!res.next()) {
                return null;
            }
            Account acc = MyUtils.decerializeAcc(res);
            System.out.println("read: " + acc);
            return acc;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Account update(Account object) {
        Account found = read(object.getId());

        if (found == null) {
            System.out.println("Account with that id doesn't exist");
            return null;
        }

        try {
            updateStmt.clearParameters();
            updateStmt.setInt(1, object.getPin());
            updateStmt.setString(2, object.getName());
            updateStmt.setDouble(3, object.getBalance());
            updateStmt.setInt(4, found.getId());

            if (updateStmt.executeUpdate() == 0) {
                System.out.println("Error something went wrong with update");
                return null;
            }
            System.out.println("Updated: " + object);
            return object;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Account delete(Account object) {
        Account found = read(object.getId());

        if (found == null) {
            System.out.println("Account with that id doesnt exist");
            return null;
        }

        try {
            deleteStmt.clearParameters();
            deleteStmt.setInt(1, found.getId());

            if (deleteStmt.executeUpdate() == 0) {
                System.out.println("Error while deleting account");
            }

            System.out.println("Deleted: " + found);
            return found;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
