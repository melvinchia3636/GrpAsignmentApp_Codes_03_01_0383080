package features.modules.CarbonFootprintAnalyzer.handlers.goal;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import core.terminal.Chalk;
import core.terminal.OutputUtils;
import features.auth.data.UserManager;

import java.util.Scanner;

public class FootprintGoalSetHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        UserManager userManager = GlobalManager.getInstance().getUserManager();
        String goalString = argsMap.get("goal");
        double goal = Double.parseDouble(goalString);

        if (userManager.getFootprintGoal() == 0.0) {
            userManager.setFootprintGoal(goal);
            OutputUtils.printSuccess("Your carbon footprint goal has been set to " +
                    new Chalk(String.format("%6f kg CO2e", goal)).blue().bold() + ".");
            return;
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("Your current carbon footprint goal is " + new Chalk(
                String.format("%6f kg CO2e", userManager.getFootprintGoal())
        ).blue().bold() + ".");
        System.out.print("Do you want to change your carbon footprint goal? (yes/no): ");
        String confirmation = sc.nextLine().trim().toLowerCase();

        if ("yes".equals(confirmation)) {
            userManager.setFootprintGoal(goal);
            OutputUtils.printSuccess("Your carbon footprint goal has been updated to " + new Chalk(
                    String.format("%6f kg CO2e", userManager.getFootprintGoal())
            ).blue().bold() + ".");
        } else {
            OutputUtils.printError("Operation cancelled. No changes were made to your carbon footprint goal.");
        }
    }
}
