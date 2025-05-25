
package security;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

public class LeakChecker {
    public static boolean isPasswordLeaked(String password) {
        try {
            String sha1 = sha1(password).toUpperCase();
            String prefix = sha1.substring(0, 5);
            URL url = new URL("https://api.pwnedpasswords.com/range/" + prefix);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.startsWith(sha1.substring(5))) {
                    return true;
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

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
