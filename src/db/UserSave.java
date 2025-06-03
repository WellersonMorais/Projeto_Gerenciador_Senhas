package db;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class UserSave {

    private UserSave() {
    throw new IllegalStateException("Utility class");
    }
    public static void salvarUsuario(String email, String senhaCriptografada) {
        String sql = "INSERT INTO users (email, password) VALUES (?, ?)";
        try (Connection conn = db.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, senhaCriptografada);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
