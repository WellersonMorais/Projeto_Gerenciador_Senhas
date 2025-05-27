package security;

import org.mindrot.jbcrypt.BCrypt;
import java.security.SecureRandom;

public class PasswordUtils {

    /**
     * Gera o hash bcrypt da senha em texto puro.
     * @param plainTextPassword senha em texto simples
     * @return hash bcrypt da senha
     */
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(12));
    }

    /**
     * Verifica se a senha em texto puro corresponde ao hash bcrypt fornecido.
     * @param plainTextPassword senha em texto simples para verificação
     * @param hashedPassword hash bcrypt armazenado
     * @return true se a senha bate com o hash, false caso contrário
     */
    public static boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }

    /**
     * Gera uma senha aleatória forte com 16 caracteres.
     * @return senha aleatória forte
     */
    public static String generateStrongPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*()-_=+";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
