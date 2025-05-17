package auth;

import utils.MD5;

/**
 * PasswordUtils provides utility methods for hashing and validating passwords using MD5.
 */
public class PasswordUtils {
    /**
     * Hashes the input password using MD5 and returns the hexadecimal string.
     *
     * @param inputPassword the plain text password
     * @return the MD5 hash as a hexadecimal string
     */
    public static String hashPassword(String inputPassword) {
        return MD5.toHexString(MD5.computeMD5(inputPassword.getBytes()));
    }

    /**
     * Validates the input password against the given hash.
     *
     * @param inputPassword the plain text password
     * @param hash the expected hash value
     * @return true if the password matches the hash, false otherwise
     */
    public static boolean validatePassword(String inputPassword, String hash) {
        String hashedInputPassword = MD5.toHexString(MD5.computeMD5(inputPassword.getBytes()));
        return hashedInputPassword.equals(hash);
    }
}