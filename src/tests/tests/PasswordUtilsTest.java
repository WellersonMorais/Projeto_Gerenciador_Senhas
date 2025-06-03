package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import security.PasswordUtils;

public class PasswordUtilsTest {

    @Test
    public void testHashAndVerifyPassword() {
        String senha = "MinhaSenhaForte!";
        String hash = PasswordUtils.hashPassword(senha);

        assertNotNull(hash);
        assertTrue(PasswordUtils.verifyPassword(senha, hash));
        assertFalse(PasswordUtils.verifyPassword("outraSenha", hash));
    }
}
