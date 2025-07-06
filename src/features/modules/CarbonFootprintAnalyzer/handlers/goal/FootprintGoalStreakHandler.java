package features.modules.CarbonFootprintAnalyzer.handlers.goal;

import core.cli.commands.CommandInstance;
import core.instances.Timestamp;
import core.manager.GlobalManager;
import core.terminal.Chalk;
import core.terminal.OutputUtils;
import features.auth.data.UserManager;
import features.modules.CarbonFootprintAnalyzer.data.FootprintManager;
import features.modules.CarbonFootprintAnalyzer.instances.FootprintRecord;

import java.util.Arrays;

public class FootprintGoalStreakHandler extends CommandInstance.Handler {
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

        Timestamp t = new Timestamp();
        t.setHour(0);
        t.setMinute(0);
        t.setSecond(0);
        t.setMillisecond(100);

        int streak = 0;
        int longestStreak = 0;

        long timestampOfFirstRecord = footprintManager.getRecords().get(0).getTimestamp().getTimestamp();
        Timestamp firstRecordDate = new Timestamp(timestampOfFirstRecord);
        firstRecordDate.setHour(0);
        firstRecordDate.setMinute(0);
        firstRecordDate.setSecond(0);
        firstRecordDate.setMillisecond(0);

        while (t.getTimestamp() > firstRecordDate.getTimestamp()) {
            FootprintRecord[] records = footprintManager.getRecordsByDate(t);
            double totalAmount = Arrays.stream(records).mapToDouble(FootprintRecord::getEstimatedFootprint).sum();

            if (totalAmount <= goal) {
                streak++;

                if (streak > longestStreak) {
                    longestStreak = streak;
                }
            } else {
                streak = 0;
            }

            t.subtract("day", 1);
        }

        System.out.println();
        System.out.println("You have stayed within your carbon footprint goal of " + new Chalk(
                        String.format("%6f kg CO2e", goal)
                ).blue().bold() + " for:"
        );
        if (streak == 0) {
            System.out.println(new Chalk("No streak yet.").red().bold());
        } else {
            System.out.println(new Chalk(streak + " consecutive day" + (streak > 1 ? "s" : "")).green().bold());
        }

        if (longestStreak == 0) {
            System.out.println(new Chalk("No longest streak yet.").red().bold());
        } else {
            System.out.println("\nLongest streak: " + new Chalk(longestStreak + " consecutive day" + (longestStreak > 1 ? "s" : "")).green().bold());
        }
    }
}
