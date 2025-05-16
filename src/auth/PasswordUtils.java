package auth;

import utils.MD5;

public class PasswordUtils {
    public static String hashPassword(String inputPassword) {
        return MD5.toHexString(MD5.computeMD5(inputPassword.getBytes()));
    }

    public static boolean validatePassword(String inputPassword, String hash) {
        String hashedInputPassword = MD5.toHexString(MD5.computeMD5(inputPassword.getBytes()));
        return hashedInputPassword.equals(hash);
    }
}