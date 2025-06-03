package tests;

import org.junit.jupiter.api.Test;
import db.UserSave;

import static org.junit.jupiter.api.Assertions.*;

public class UserSaveTest {

    @Test
    public void testSalvarUsuario() {
        String email = "teste@exemplo.com";
        String hash = "$2a$10$abc1234567890abcdefg"; // Simulando hash

        assertDoesNotThrow(() -> {
            UserSave.salvarUsuario(email, hash);
        });
    }
}
