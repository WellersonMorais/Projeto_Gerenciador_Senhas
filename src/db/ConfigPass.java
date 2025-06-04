package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConfigPass {

    private ConfigPass() {
        throw new IllegalStateException("Utility class");
    }

    private static String pegarValor(String chave) {
        String sql = "SELECT valor FROM config WHERE chave = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, chave);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("valor");
            } else {
                System.err.println("Configuração '" + chave + "' não encontrada no banco.");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String pegarSenhaApp() {
        return pegarValor("senha_app_email");
    }

    public static String pegarChaveSecreta() {
        return pegarValor("chave_secreta");
    }

    public static String pegarSalt() {
        return pegarValor("salt");
    }

    public static String pegarEmail() {
        return pegarValor("email_app");
    }
}
