package org.bank.Model;

import org.bank.grpc.Bank.Statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class StatementRepository implements CRUDRepository<Statement>{

    private Connection connection;

    private final PreparedStatement createStmt;
    private final PreparedStatement readStmt;

    private final PreparedStatement readByAccId;

    public StatementRepository(Connection connection){
        try {
            this.connection = connection;
            this.createStmt = connection.prepareStatement("INSERT INTO statement (account_id, type, message) VALUES (?, ?, ?)");
            this.readStmt = connection.prepareStatement("SELECT * FROM statement WHERE id = ?");
            this.readByAccId = connection.prepareStatement("SELECT * FROM statement WHERE account_id = ?");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Statement create(Statement object) {
        try {
            createStmt.clearParameters();
            createStmt.setInt(1, object.getAccountId());
            createStmt.setString(2, object.getType());
            createStmt.setString(3, object.getMessage());

            if (createStmt.executeUpdate() == 0) {
                return null;
            }

            System.out.println("Created: " + object);
            return object;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Statement> readByAccId(int id){
        try {
            readByAccId.clearParameters();
            readByAccId.setInt(1, id);

            ResultSet res = readByAccId.executeQuery();
            List<Statement> stmts = MyUtils.deserializeListStmt(res);
            return stmts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Statement read(int id) {
        try {
            readStmt.clearParameters();
            readStmt.setInt(1, id);

            ResultSet res = readStmt.executeQuery();
            if (!res.next()) {
                return null;
            }
            Statement stmt = MyUtils.deserializeStmt(res);
            System.out.println("read: " + stmt);
            return stmt;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Statement update(Statement object) {
        return null;
    }

    @Override
    public Statement delete(Statement object) {
        return null;
    }
}
