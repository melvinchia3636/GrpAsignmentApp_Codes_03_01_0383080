package features.auth.handlers;

import core.cli.commands.CommandInstance;
import core.constants.Countries;
import core.terminal.Chalk;
import core.terminal.OutputUtils;
import core.manager.GlobalManager;
import features.auth.data.UserManager;
import features.auth.instances.Password;

import java.util.Scanner;

public class SignupHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        UserManager userManager = GlobalManager.getInstance().getUserManager();

        String username = argsMap.get("username").trim();
        String password = argsMap.get("password");
        String countryCode = argsMap.get("country");

        if (!Password.isValid(password)) {
            OutputUtils.printError("Invalid password. Password must:\n" +
                    "    - Be at least 8 characters long\n" +
                    "    - Contain at least one uppercase letter\n" +
                    "    - Contain at least one lowercase letter\n" +
                    "    - Contain at least one digit\n" +
                    "    - Contain at least one special character\n" +
                    "    - Not contain any whitespace characters", false);
            return;
        }

        if (countryCode != null) {
            String country = Countries.getCountryName(countryCode);

            if (country == null) {
                OutputUtils.printError("Invalid country code: " + countryCode, false);
            }
        }

        int retryCount = 0;

        try (Scanner scanner = new Scanner(System.in)) {
            while (retryCount < 3) {
                System.out.print("Enter your password again for confirmation: ");
                String confirmPassword = scanner.nextLine();

                if (password.equals(confirmPassword)) break;                OutputUtils.printError("Passwords do not match. Please try again.", false);
                retryCount++;
            }

            if (retryCount == 3) {
                OutputUtils.printError("Password confirmation failed after 3 attempts.", false);
                return;
            }
        } // Close try-with-resources for Scanner

        userManager.signup(username, password, countryCode);
        userManager.writeToFile();

        OutputUtils.printSuccess("Signup successful for user: " + new Chalk(username).bold().cyan());
    }
}
