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
import java.util.Calendar;

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

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 100);

        int streak = 0;
        int thisStreak = Integer.MIN_VALUE;
        int longestStreak = 0;

        long timestampOfFirstRecord = footprintManager.getRecords().get(0).getTimestamp().getTimestamp();
        while (c.getTimeInMillis() > timestampOfFirstRecord) {
            FootprintRecord[] records = footprintManager.getRecordsByDate(new Timestamp(c.getTimeInMillis()));
            double totalAmount = Arrays.stream(records).mapToDouble(FootprintRecord::getEstimatedFootprint).sum();

            if (totalAmount <= goal) {
                streak++;
            } else {
                if (streak > longestStreak) {
                    longestStreak = streak;
                }

                if (thisStreak == Integer.MIN_VALUE) {
                    thisStreak = streak;
                }
            }

            c.add(Calendar.DATE, -1);
        }

        System.out.println();
        System.out.println("🔥You have stayed within your carbon footprint goal of " + new Chalk(
                        String.format("%6f kg CO2e", goal)
                ).blue().bold() + " for:"
        );
        if (streak == 0) {
            System.out.println(new Chalk("No streak yet.").red().bold());
        } else {
            System.out.println(new Chalk("▶️ " + thisStreak + " consecutive day" + (thisStreak > 1 ? "s" : "")).green().bold());
        }

        if (longestStreak == 0) {
            System.out.println(new Chalk("No longest streak yet.").red().bold());
        } else {
            System.out.println("\n🏆 Longest streak: " + new Chalk(longestStreak + " consecutive day" + (longestStreak > 1 ? "s" : "")).green().bold());
        }
    }
}
