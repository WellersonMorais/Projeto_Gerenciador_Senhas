package security;

import org.mindrot.jbcrypt.BCrypt;
import java.security.SecureRandom;

public class PasswordUtils {

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
}
