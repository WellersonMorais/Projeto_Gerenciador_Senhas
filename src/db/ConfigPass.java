package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConfigPass {

    private ConfigPass() {
    throw new IllegalStateException("Utility class");
    }

    public static String pegarSenhaApp() {
        String sql = "SELECT valor FROM config WHERE chave = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "senha_app_email");
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("valor");
            } else {
                System.err.println("Senha do app não encontrada no banco.");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
