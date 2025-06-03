package tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import config.Descriptografador;

public class DescriptografadorTest {

    @Test
    public void testDescriptografarComSenhaIncorreta() {
        String senhaErrada = "senhaErrada";
        assertThrows(Exception.class, () -> {
            Descriptografador.descriptografarArquivo(senhaErrada);
        });
    }
}
