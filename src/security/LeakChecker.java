package security;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

public class LeakChecker {

    /**
     * Verifica se a senha foi vazada usando a API "Pwned Passwords"
     * @param password senha a ser checada
     * @return true se a senha foi vazada, false caso contrário
     */
    public static boolean isPasswordLeaked(String password) {
        try {
            // Calcula SHA-1 da senha e deixa maiúscula
            String sha1 = sha1(password).toUpperCase();

            // Pega os 5 primeiros caracteres do hash
            String prefix = sha1.substring(0, 5);

            // Faz requisição à API com o prefixo
            URL url = new URL("https://api.pwnedpasswords.com/range/" + prefix);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;

            // Verifica se algum sufixo retornado coincide com o hash restante
            while ((line = in.readLine()) != null) {
                if (line.startsWith(sha1.substring(5))) {
                    in.close();
                    return true; // Senha vazada
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false; // Senha não encontrada no banco de dados de vazamentos
    }

    /**
     * Calcula SHA-1 da string passada
     * @param input texto a ser hasheado
     * @return hash SHA-1 em hexadecimal
     * @throws Exception caso ocorra erro no cálculo
     */
    private static String sha1(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] result = md.digest(input.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();

        for (byte b : result) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}
