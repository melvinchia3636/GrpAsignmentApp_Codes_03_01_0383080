package features.auth.handlers;

import constants.Countries;
import core.cli.commands.CommandInstance;
import core.terminal.OutputUtils;
import core.manager.GlobalManager;
import features.auth.UserManager;
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
            throw new IllegalArgumentException("Invalid password. Password must:\n" +
                    "    - Be at least 8 characters long\n" +
                    "    - Contain at least one uppercase letter\n" +
                    "    - Contain at least one lowercase letter\n" +
                    "    - Contain at least one digit\n" +
                    "    - Contain at least one special character\n" +
                    "    - Not contain any whitespace characters");
        }

        if (countryCode != null) {
            String country = Countries.getCountryName(countryCode);

            if (country == null) {
                throw new IllegalArgumentException("Invalid country code: " + countryCode);
            }
        }

        int retryCount = 0;
        Scanner scanner = new Scanner(System.in);

        while (retryCount < 3) {
            System.out.print("Enter your password again for confirmation: ");
            String confirmPassword = scanner.nextLine();

            if (password.equals(confirmPassword)) break;

            OutputUtils.printError("Passwords do not match. Please try again.");
            retryCount++;
        }

        userManager.signup(username, password, countryCode);
        userManager.writeToFile();
    }
}
