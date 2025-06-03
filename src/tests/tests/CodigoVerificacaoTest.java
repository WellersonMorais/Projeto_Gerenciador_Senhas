package tests;

import org.junit.jupiter.api.Test;
import java.util.regex.Pattern;
import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.*;

public class CodigoVerificacaoTest {

    @Test
    public void testCodigoTem6Digitos() {
        String codigo = String.format("%06d", new SecureRandom().nextInt(1000000));
        assertTrue(Pattern.matches("\\d{6}", codigo), "O código de verificação deve ter 6 dígitos");
    }
}
