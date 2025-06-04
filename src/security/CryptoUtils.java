package security;

import java.security.SecureRandom;

public class CryptoUtils {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private CryptoUtils() {
        // Evita instanciamento da classe utilitária
        throw new IllegalStateException("Classe utilitária");
    }

    public static byte[] gerarIV(int tamanho) {
        byte[] iv = new byte[tamanho];
        SECURE_RANDOM.nextBytes(iv);
        return iv;
    }
}

