package Authentication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <h1>Security</h1>
 * This class provides a method to perform MD5 hashing and a method to verify user password
 */
public class Security {
    /**
     * <h2>HashMd5</h2>
     * This method gets the user password at the time of registration and converts it into a hash code using
     * MD5 hashing and stores it in the Users.json file.
     *
     * This method returns a string containing the hash code.
     *
     * @param  password  the user's password at the time of registration
     * @return           true if user registration was successful otherwise false
     */
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

    /**
     * <h2>verifyPassword</h2>
     * This method performs password verification by getting the user password during login and compares it
     * with the hashed password stored in the Users.json file.
     *
     * It then converts the users inputted password into the hash code and verifies if it matches with the
     * hashed password.
     *
     * This method returns a boolean value after password verification.
     *
     * @param  password        the user's password at the time of login
     * @param  hashedPassword  the password stored in the database
     * @return           true if user login was successful otherwise false
     */
    public static boolean verifyPassword(String password, String hashedPassword) {
        String userInputHashed = HashMd5(password);
        assert userInputHashed != null;
        return userInputHashed.equals(hashedPassword);
    }
}
