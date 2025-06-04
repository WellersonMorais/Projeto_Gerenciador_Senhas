package security;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import db.ConfigPass;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class EncryptionUtils {

    private EncryptionUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final String SECRET = ConfigPass.pegarChaveSecreta();
    private static final String SALT = ConfigPass.pegarSalt();
    private static final int IV_SIZE = 12; // 12 bytes para GCM
    private static final int TAG_LENGTH = 128;

    public static String encrypt(String strToEncrypt) throws Exception {
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);

        SecretKeySpec secretKey = getSecretKey();

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec);

        byte[] encrypted = cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8));

        byte[] ivAndEncrypted = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, ivAndEncrypted, 0, iv.length);
        System.arraycopy(encrypted, 0, ivAndEncrypted, iv.length, encrypted.length);

        return Base64.getEncoder().encodeToString(ivAndEncrypted);
    }

    public static String decrypt(String strToDecrypt) throws Exception {
        byte[] ivAndEncrypted = Base64.getDecoder().decode(strToDecrypt);

        byte[] iv = new byte[IV_SIZE];
        byte[] encrypted = new byte[ivAndEncrypted.length - IV_SIZE];
        System.arraycopy(ivAndEncrypted, 0, iv, 0, IV_SIZE);
        System.arraycopy(ivAndEncrypted, IV_SIZE, encrypted, 0, encrypted.length);

        SecretKeySpec secretKey = getSecretKey();
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec);

        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    private static SecretKeySpec getSecretKey() throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(SECRET.toCharArray(), SALT.getBytes(StandardCharsets.UTF_8), 65536, 256);
        byte[] key = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(key, "AES");
    }
}
