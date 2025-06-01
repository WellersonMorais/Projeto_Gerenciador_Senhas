package db;

import java.sql.Connection;
import java.sql.Statement;

public class SetupDatabase {
    public static void setup() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    email VARCHAR(255) PRIMARY KEY,
                    password VARCHAR(255) NOT NULL
                );
            """;

            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
