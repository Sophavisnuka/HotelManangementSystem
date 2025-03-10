package JavaProject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import java.sql.ResultSet;

public class MySQLConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/hotel";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1234";

    // Establish the connection
    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    // Execute a query (SELECT)
    public static ResultSet executeQuery(String query) {
        try (Connection conn = getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            return resultSet;

        } catch (SQLException e) {
            System.out.println("Query execution failed!");
            e.printStackTrace();
        }
        return null;
    }

    // Execute a parameterized query (Prevents SQL Injection)
    public static ResultSet executePreparedQuery(String query, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {

            // Set all parameters dynamically
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            return statement.executeQuery();

        } catch (SQLException e) {
            System.out.println("Query execution failed!");
            e.printStackTrace();
        }
        return null;
    }

    // Execute an update (INSERT, UPDATE, DELETE)
    public static int executeUpdate(String query) {
        try (Connection conn = getConnection();
             Statement statement = conn.createStatement()) {

            return statement.executeUpdate(query);

        } catch (SQLException e) {
            System.out.println("Update execution failed!");
            e.printStackTrace();
        }
        return 0;
    }

    // Execute an update (INSERT, UPDATE, DELETE) query with multiple parameters
    public static int executePreparedUpdate(String query, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {

            // Set all parameters dynamically
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            return statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Update execution failed!");
            e.printStackTrace();
        }
        return 0;
    }

    // Close ResultSet
    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Close connection
}
