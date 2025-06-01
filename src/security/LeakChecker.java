package security;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LeakChecker {

    private LeakChecker() {
    throw new IllegalStateException("Utility class");
  }

    public static boolean isPasswordLeaked(String password) {
        try {
            String sha1 = sha1(password).toUpperCase();
            String prefix = sha1.substring(0, 5);
            String suffix = sha1.substring(5);

            URI uri = new URI("https://api.pwnedpasswords.com/range/" + prefix);
            URL url = uri.toURL();

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[0].equalsIgnoreCase(suffix)) {
                    return true; // Senha foi vazada
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Senha segura (não vazada)
    }

   private static String sha1(String input) {
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] result = md.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : result) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    } catch (NoSuchAlgorithmException e) {
        throw new IllegalStateException("SHA-1 algorithm not found", e);
    }
}

}
