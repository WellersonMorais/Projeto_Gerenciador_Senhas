package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private DatabaseConnection() {
    throw new IllegalStateException("Utility class");
  }
    private static final String URL = "jdbc:h2:./password_manager_db"; // arquivo local (password_manager_db.mv.db)
    private static final String USER = "sa";
    private static final String PASSWORD = "s3nh4s3gur4";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
