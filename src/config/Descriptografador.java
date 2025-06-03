package config;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Descriptografador {

    private Descriptografador() {

    throw new IllegalStateException("Utility class");
    }

    private static final String ENCRYPTED_FILE = "src/config/private_key.enc";

    private static SecretKey gerarChaveSecreta(char[] senha, byte[] salt) throws Exception {
        SecretKeyFactory fabrica = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec especificacao = new PBEKeySpec(senha, salt, 65536, 256);
        SecretKey chaveTemporaria = fabrica.generateSecret(especificacao);
        return new SecretKeySpec(chaveTemporaria.getEncoded(), "AES");
    }

    public static String descriptografarArquivo(String senha) throws Exception {
        String conteudoArquivo = new String(Files.readAllBytes(Paths.get(ENCRYPTED_FILE)), StandardCharsets.UTF_8);

        String[] partes = conteudoArquivo.split(":");
        if (partes.length != 3) throw new IllegalArgumentException("Arquivo corrompido ou inválido.");

        byte[] salt = Base64.getDecoder().decode(partes[0]);
        byte[] iv = Base64.getDecoder().decode(partes[1]);
        byte[] textoCriptografado = Base64.getDecoder().decode(partes[2]);

        SecretKey chave = gerarChaveSecreta(senha.toCharArray(), salt);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        Cipher cifra = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cifra.init(Cipher.DECRYPT_MODE, chave, ivSpec);

        byte[] textoDescriptografado = cifra.doFinal(textoCriptografado);
        return new String(textoDescriptografado, "UTF-8");
    }

}
