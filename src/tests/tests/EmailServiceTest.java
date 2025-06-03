package tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import security.EmailService;

public class EmailServiceTest {

    @Test
    public void testEnvioEmailFalha() {
        Exception excecao = assertThrows(Exception.class, () -> {
            EmailService.sendVerificationEmail("email_invalido", "123456");
        });
        assertNotNull(excecao.getMessage());
    }
}
