package features.auth.instances;

public class Password {
    // A prefix to be added to the text before encryption due to the nature of XOR encryption
    // that will still properly decrypt a string despite having slightly altered password
    // but the text decrypted will not match the original text.
    private final String PREFIX = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";

    private final String password;

    /**
     * Constructs a Password object with the given password.
     *
     * @param password the password to be set
     * @throws IllegalArgumentException if the password does not meet the security requirements
     */
    public Password(String password) {
        this.password = password;
    }

    /**
     * Validates the given password against a set of security rules.
     *
     * @param password the password to validate
     * @throws IllegalArgumentException if the password does not meet the security requirements
     */
    public static boolean isValid(String password) {
        if (password.matches(".*\\s.*")) {
           return false; // Password cannot contain any whitespace characters
        }

        if (password.length() < 8) {
            return false; // Password must be at least 8 characters long
        }

        if (!password.matches(".*[A-Z].*")) {
            return false; // Password must contain at least one uppercase letter
        }

        if (!password.matches(".*[a-z].*")) {
           return false; // Password must contain at least one lowercase letter
        }

        if (!password.matches(".*\\d.*")) {
            return false; // Password must contain at least one digit
        }

        return password.matches(".*[!@#$%^&*(),.?\":{}|<>].*"); // Password must contain at least one special character
    }

    /**
     * Encrypts the given text using XOR encryption with the password.
     *
     * @param text the text to encrypt
     * @return the encrypted text
     */
    private String xorCipher(String text) {
        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            char k = password.charAt(i % password.length());
            encrypted.append((char) (c ^ k));
        }

        return encrypted.toString();
    }

    /**
     * Encrypts the given text by adding a prefix and applying XOR encryption.
     *
     * @param text the text to encrypt
     * @return the encrypted text with a prefix
     * @throws IllegalArgumentException if the text is null
     */
    public String encrypt(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text to encrypt cannot be null");
        }


        text = PREFIX + text;
        return xorCipher(text);
    }

    /**
     * Decrypts the given encrypted text by removing the prefix and applying XOR decryption.
     *
     * @param encryptedText the encrypted text to decrypt
     * @return the decrypted text
     * @throws IllegalArgumentException if the encrypted text is null or does not have the expected prefix
     */
    public String decrypt(String encryptedText) {
        if (encryptedText == null) {
            throw new IllegalArgumentException("Encrypted text cannot be null");
        }

        String decryptedText = xorCipher(encryptedText);
        if (!decryptedText.startsWith(PREFIX)) {
            throw new IllegalArgumentException("Decrypted text does not have the expected prefix.");
        }

        return decryptedText.substring(PREFIX.length()); // Remove the prefix added during encryption
    }
}
