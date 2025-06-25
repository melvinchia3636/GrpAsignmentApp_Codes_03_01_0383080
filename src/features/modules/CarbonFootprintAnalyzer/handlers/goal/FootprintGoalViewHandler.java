package features.modules.CarbonFootprintAnalyzer.handlers.goal;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
import core.terminal.Chalk;
import core.terminal.OutputUtils;
import features.auth.data.UserManager;
import features.modules.CarbonFootprintAnalyzer.data.FootprintManager;
import features.modules.CarbonFootprintAnalyzer.instances.FootprintRecord;

import java.util.Arrays;

public class FootprintGoalViewHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        UserManager userManager = GlobalManager.getInstance().getUserManager();
        FootprintManager footprintManager = GlobalManager.getInstance().getFootprintManager();
        double goal = userManager.getFootprintGoal();

        if (goal == 0.0) {
            OutputUtils.printError("You have not set a carbon footprint goal yet.", false);
            System.out.println(new Chalk("Use the command 'footprint goal set <goal>' to set your carbon footprint goal.").yellow());
            return;
        }

        FootprintRecord[] last7DaysRecords = footprintManager.getRecordsForLastXDays(7);
        double averageFootprint = Arrays.stream(last7DaysRecords)
                .mapToDouble(FootprintRecord::getEstimatedFootprint)
                .average()
                .orElse(0.0);

        System.out.println("🎯Your current carbon footprint goal is " + new Chalk(
                String.format("%6f kg CO2e", goal)
        ).blue().bold() + ".");

        if (last7DaysRecords.length == 0) {
            OutputUtils.printError("No carbon footprint records found for the last 7 days.", false);
            System.out.println(new Chalk("Log your first activity using the 'footprint log' command.").yellow());
        } else {
            System.out.printf("\n📉Your average carbon footprint for the last 7 days is %s\n",
                    new Chalk(String.format("%.4f kg CO2e", averageFootprint)).blue().bold());

            if (averageFootprint > goal) {
                System.out.println(new Chalk("⚠️ You are exceeding your carbon footprint goal!").yellow().bold());
            } else {
                OutputUtils.printSuccess(new Chalk("You are within your carbon footprint goal!").green().bold().toString());
            }
        }
    }
}
