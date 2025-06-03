package security;

import org.mindrot.jbcrypt.BCrypt;
import java.security.SecureRandom;

public class PasswordUtils {

    private PasswordUtils() {
    throw new IllegalStateException("Utility class");
    }

    /**
     * @param plainTextPassword 
     * @return 
     */
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(12));
    }
    /**
     * @param plainTextPassword 
     * @param hashedPassword 
     * @return 
     */
    public static boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
    /**
     * @return
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
    public static boolean isValidPassword(String password) {
        if (password.length() < 6) return false;
        if (!password.matches(".*[A-Z].*")) return false; // Pelo menos uma letra maiúscula
        if (!password.matches(".*\\d.*")) return false;   // Pelo menos um número
        return true;
}

}
