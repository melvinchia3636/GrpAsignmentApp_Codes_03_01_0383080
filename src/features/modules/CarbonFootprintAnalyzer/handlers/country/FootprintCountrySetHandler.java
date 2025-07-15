package features.modules.CarbonFootprintAnalyzer.handlers.country;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import core.terminal.Chalk;
import core.terminal.OutputUtils;
import features.auth.data.UserManager;

import java.util.Scanner;

public class FootprintCountrySetHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        UserManager userManager = GlobalManager.getInstance().getUserManager();
        String country = argsMap.get("country");

        if (userManager.getFootprintCountry().isEmpty()) {
            userManager.setFootprintCountry(country);
            OutputUtils.printSuccess("Your carbon footprint country has been set to " +
                    new Chalk(country).blue().bold() + ".");
            return;
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("Your current carbon footprint country is " + new Chalk(
                userManager.getFootprintCountry()
        ).blue().bold() + ".");
        System.out.print("Do you want to change your carbon footprint country? (yes/no): ");
        String confirmation = sc.nextLine().trim().toLowerCase();

        if ("yes".equals(confirmation)) {
            userManager.setFootprintCountry(country);
            OutputUtils.printSuccess("Your carbon footprint country has been updated to " + new Chalk(
                    userManager.getFootprintCountry()
            ).blue().bold() + ".");
        } else {
            OutputUtils.printError("Operation cancelled. No changes were made to your carbon footprint country.", false);
        }
    }
}
