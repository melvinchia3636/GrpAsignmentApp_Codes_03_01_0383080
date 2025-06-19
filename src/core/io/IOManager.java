package core.io;

import core.terminal.Chalk;
import core.manager.GlobalManager;
import core.instances.JSONObject;
import core.terminal.OutputUtils;
import features.auth.data.UserManager;
import features.auth.instances.Password;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * IOManager is responsible for managing all sorts of input/output operations related to user data.
 */
public class IOManager {
    private final Path currentDir = Paths.get("").toAbsolutePath();
    private final Path profilesDir = currentDir.resolve("profiles");

    private File userProfileFolder;

    /**
     * Initializes the IOManager and ensures the profiles directory exists.
     * If the profiles directory does not exist, it attempts to create it.
     *
     * @throws IOError if the profiles directory cannot be created
     */
    public IOManager() {
        File profilesFolder = profilesDir.toFile();
        if (!profilesFolder.exists()) {
            boolean created = profilesFolder.mkdirs();

            if (!created) {
                throw new IOError(new IOException("Failed to create profiles directory: " + profilesDir));
            }

            OutputUtils.printSuccess("Profiles directory created: " + new Chalk(profilesDir.toString()).purple());
        }
    }

    /**
     * Initializes a user profile with the given username.
     * If the profile already exists and throwIfExists is true, an error is thrown.
     * If the profile does not exist, it creates a new directory for the user profile.
     *
     * @param username      the username for the user profile
     * @param throwIfExists whether to throw an error if the user profile already exists
     * @throws Error if the user profile already exists and throwIfExists is true
     */
    public void initUserProfile(String username, boolean throwIfExists) {
        Path userProfileDir = profilesDir.resolve(username);
        userProfileFolder = userProfileDir.toFile();

        if (userProfileExists(username)) {
            if (throwIfExists) {
                throw new Error("User profile already exists: " + username);
            }
            return;
        }

        boolean created = userProfileFolder.mkdir();

        if (!created) {
            throw new IOError(new IOException("Failed to create user profile file: " + userProfileFolder));
        }

        OutputUtils.printSuccess("User profile created: " + new Chalk(userProfileFolder.toString()).purple());
    }

    /**
     * Parses a JSON object from a file with the given filename and password.
     * The file is expected to be encrypted with the provided password.
     * This method is only used during login.
     *
     * @param filename the name of the file to read
     * @param password the password used for decryption
     * @return a JSONObject parsed from the decrypted content of the file
     * @throws Error if decryption fails or if the file cannot be read
     */
    public JSONObject parseStringFromFile(String filename, String password) {
        String data = readFromFile(filename);
        JSONObject decryptedJSON = new JSONObject();

        try {
            Password pwd = new Password(password);
            decryptedJSON.parseFromString(pwd.decrypt(data));
            return decryptedJSON;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to decrypt data file: " + filename + ". Please check your password.", e);
        }
    }

    /**
     * Parses a JSON object from a file with the given filename.
     * The file is expected to be encrypted with the UserManager's password.
     * This method is used across the application after a user has logged in.
     *
     * @param filename the name of the file to read
     * @return a JSONObject parsed from the decrypted content of the file
     * @throws IllegalStateException if UserManager password is not set
     * @throws Error                 if decryption fails or if the file cannot be read
     */
    public String parseStringFromFile(String filename) {
        UserManager userManager = GlobalManager.getInstance().getUserManager();

        if (userManager.getPassword() == null) {
            throw new IllegalStateException("UserManager password is not set. Please login first.");
        }

        String data = readFromFile(filename);
        return userManager.getPassword().decrypt(data);
    }

    /**
     * Writes a String content to a file with the given filename.
     * The content is encrypted using the UserManager's password before writing.
     *
     * @param filename the name of the file to write
     * @param content  the String content to write to the file
     * @throws IllegalStateException if UserManager password is not set or if user profile folder is not initialized
     * @throws IOError               if there is an error writing to the file
     */
    public void writeToFile(String filename, String content) {
        String encryptedContent = getEncryptedString(content);

        Path filePath = userProfileFolder.toPath().resolve(filename + ".bin");
        try {
            Files.write(filePath, encryptedContent.getBytes());
        } catch (IOException e) {
            throw new IOError(
                    new IOException("Failed to write to file: " + filePath, e)
            );
        }
    }

    /**
     * Writes a JSON object to a file with the given filename.
     * The content is encrypted using the UserManager's password before writing.
     *
     * @param filename the name of the file to write
     * @param content  the JSONObject to write to the file
     * @throws IllegalStateException if UserManager password is not set or if user profile folder is not initialized
     * @throws IOError               if there is an error writing to the file
     */
    public void writeToFile(String filename, JSONObject content) {
        writeToFile(filename, content.toString());
    }


    /**
     * Checks if a user profile exists for the given username.
     *
     * @param username the username to check
     * @return true if the user profile exists, false otherwise
     */
    public boolean userProfileExists(String username) {
        Path userProfileDir = profilesDir.resolve(username);
        return Files.exists(userProfileDir) && Files.isDirectory(userProfileDir);
    }

    /**
     * Reads the content of a file with the given filename.
     * The file is expected to be in the user profile folder.
     *
     * @param filename the name of the file to read
     * @return the content of the file as a String
     * @throws IllegalStateException if user profile folder is not initialized
     * @throws IOError               if there is an error reading from the file
     */
    public String readFromFile(String filename) {
        if (userProfileFolder == null) {
            throw new IllegalStateException("User profile folder is not initialized. Call initUserProfile() first.");
        }

        Path filePath = userProfileFolder.toPath().resolve(filename + ".bin");
        try {
            return new String(Files.readAllBytes(filePath));
        } catch (IOException e) {
            throw new IOError(
                    new IOException("Failed to read from file: " + filePath, e)
            );
        }
    }

    public boolean existsFile(String filename) {
        if (userProfileFolder == null) {
            throw new IllegalStateException("User profile folder is not initialized. Call initUserProfile() first.");
        }

        Path filePath = userProfileFolder.toPath().resolve(filename + ".bin");
        return Files.exists(filePath);
    }


    private String getEncryptedString(String content) {
        UserManager userManager = GlobalManager.getInstance().getUserManager();
        Password password = userManager.getPassword();

        if (password == null) {
            throw new IllegalStateException("UserManager password is not set. Please login first.");
        }

        if (userProfileFolder == null) {
            throw new IllegalStateException("User profile folder is not initialized. Call initUserProfile() first.");
        }

        return password.encrypt(content);
    }


}
