package features.auth.handlers;

import constants.Countries;
import core.cli.commands.CommandInstance;
import core.terminal.Chalk;
import core.terminal.OutputUtils;
import core.utils.ContextManager;
import features.auth.AuthContext;
import features.auth.functions.PasswordValidator;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class SignupHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        AuthContext authContext = ContextManager.getInstance().getAuthContext();

        String username = argsMap.get("username").trim();
        String password = argsMap.get("password");
        String countryCode = argsMap.get("country");

        if (countryCode != null) {
            String country = Countries.getCountryName(countryCode);

            if (country == null) {
                OutputUtils.printError("Invalid country code: " + countryCode);
                return;
            }

            authContext.setCountry(country);
        }

        try {
            PasswordValidator.validate(password);
        } catch (IllegalArgumentException e) {
            OutputUtils.printError("Invalid password: " + e.getMessage(), false);
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your password again for confirmation: ");
        String confirmPassword = scanner.nextLine();

        if (!password.equals(confirmPassword)) {
            OutputUtils.printError("Passwords do not match. Please try again.", false);
            return;
        }

        Path currentDir = Paths.get("").toAbsolutePath();
        Path profilesDir = currentDir.resolve("profiles");
        File profilesFolder = profilesDir.toFile();

        if (!profilesFolder.isDirectory() || !profilesFolder.exists()) {
            if (profilesFolder.mkdirs()) {
                System.out.println("Created profile directory at: " + new Chalk(profilesDir.toString()).purple());
            } else {
                OutputUtils.printError("Failed to create profile directory at: " + new Chalk(profilesDir.toString()).purple());
                return;
            }
        }

        String[] profiles = Arrays.stream(Objects.requireNonNull(profilesFolder.listFiles()))
                .filter(File::isDirectory)
                .map(File::getName)
                .toArray(String[]::new);

        if (Arrays.asList(profiles).contains(username)) {
            OutputUtils.printError("Profile with username '" + username + "' already exists. " +
                    "Please choose a different username.", false);
            return;
        }

        File userProfileDir = profilesDir.resolve(username).toFile();
        if (userProfileDir.mkdirs()) {
            System.out.println("Created user profile directory at: " + new Chalk(userProfileDir.getAbsolutePath()).purple());
        } else {
            OutputUtils.printError("Failed to create user profile directory at: " + new Chalk(userProfileDir.getAbsolutePath()).purple());
            return;
        }
    }
}
