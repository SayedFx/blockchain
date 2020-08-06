package blockchain.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    public static String getHash(String input) {
        try {
            MessageDigest md256 = MessageDigest.getInstance("SHA-256");
            byte[] digest = md256.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                String hexString = Integer.toHexString(0xff & b);
                if (hexString.length() == 1) sb.append('0');
                sb.append(hexString);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to create hash!: " + e.getMessage());
        }
    }
}
