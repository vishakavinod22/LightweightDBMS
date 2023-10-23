package Authentication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Security {
    public static String HashMd5(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] array = messageDigest.digest(password.getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : array) {
                stringBuilder.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return null;
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        String userInputHashed = HashMd5(password);
        assert userInputHashed != null;
        return userInputHashed.equals(hashedPassword);
    }
}
