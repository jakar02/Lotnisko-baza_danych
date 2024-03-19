package baza;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private final String url = "jdbc:postgresql://tyke.db.elephantsql.com:5432/nrxnowgl";
    private final String user = "nrxnowgl";
    private final String password = "r2Z1hLQDOK_bVg4opDHu8R48UJ_EqZRo";

    public DatabaseConnector() {

    }

    public Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return connection;
    }

    public void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
