package features.modules.CarbonFootprintAnalyzer.handlers.goal;

import core.cli.commands.CommandInstance;
import core.manager.GlobalManager;
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
            OutputUtils.printTip("Use the command 'footprint goal set <goal>' to set your carbon footprint goal.");
            return;
        }

        FootprintRecord[] last7DaysRecords = footprintManager.getRecordsForLastXDays(7);
        double averageFootprint = Arrays.stream(last7DaysRecords)
                .mapToDouble(FootprintRecord::getEstimatedFootprint)
                .average()
                .orElse(0.0);

        OutputUtils.printSectionHeader("Your Carbon Footprint Goal");
        OutputUtils.printStatistic("Goal", String.format("%.6f kg CO2e", goal), "blue");

        if (last7DaysRecords.length == 0) {
            OutputUtils.printError("No carbon footprint records found for the last 7 days.", false);
            OutputUtils.printTip("Log your first activity using the 'footprint log' command.");
        } else {
            OutputUtils.printStatistic("7-day average", String.format("%.4f kg CO2e", averageFootprint), "blue");

            if (averageFootprint > goal) {
                OutputUtils.printWarning("You are exceeding your carbon footprint goal!");
                OutputUtils.printTip("Consider reducing high-impact activities to meet your goal.");
            } else {
                OutputUtils.printSuccess("You are within your carbon footprint goal!");
                OutputUtils.printEncouragement("Keep up the great work!");
            }
        }
    }
}
