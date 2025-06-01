package features.auth.functions;

public class PasswordValidator {

    /**
     * Validates the password based on specific criteria.
     *
     * @param password The password to validate.
     * @throws IllegalArgumentException if the password does not meet the criteria.
     */
    public static void validate(String password) {
        if (password.matches(".*\\s.*")) {
            throw new IllegalArgumentException("Password cannot contain any whitespace characters");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Password must contain at least one lowercase letter");
        }
        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain at least one digit");
        }
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            throw new IllegalArgumentException("Password must contain at least one special character");
        }
    }
}
