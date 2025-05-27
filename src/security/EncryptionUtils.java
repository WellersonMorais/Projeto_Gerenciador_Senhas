package security;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class EncryptionUtils {
    // Chave secreta e salt — idealmente devem vir de fonte segura (env vars, arquivo criptografado, etc)
    private static final String SECRET = "chaveSuperSecreta123";
    private static final String SALT = "saltsalt123";

    public static String encrypt(String strToEncrypt) throws Exception {
        // Gera IV aleatório (16 bytes)
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // Obtém a chave AES derivada da senha secreta
        SecretKeySpec secretKey = getSecretKey();

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        // Criptografa o texto
        byte[] encrypted = cipher.doFinal(strToEncrypt.getBytes("UTF-8"));

        // Junta IV + ciphertext para enviar juntos
        byte[] ivAndEncrypted = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, ivAndEncrypted, 0, iv.length);
        System.arraycopy(encrypted, 0, ivAndEncrypted, iv.length, encrypted.length);

        // Codifica em Base64 e retorna
        return Base64.getEncoder().encodeToString(ivAndEncrypted);
    }

    public static String decrypt(String strToDecrypt) throws Exception {
        // Decodifica Base64 para bytes
        byte[] ivAndEncrypted = Base64.getDecoder().decode(strToDecrypt);

        // Extrai IV e ciphertext
        byte[] iv = new byte[16];
        byte[] encrypted = new byte[ivAndEncrypted.length - 16];
        System.arraycopy(ivAndEncrypted, 0, iv, 0, 16);
        System.arraycopy(ivAndEncrypted, 16, encrypted, 0, encrypted.length);

        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        SecretKeySpec secretKey = getSecretKey();

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        // Decripta e retorna string
        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted, "UTF-8");
    }

    private static SecretKeySpec getSecretKey() throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(SECRET.toCharArray(), SALT.getBytes(), 65536, 256);
        byte[] key = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(key, "AES");
    }
}
