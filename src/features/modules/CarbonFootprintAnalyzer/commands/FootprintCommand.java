package features.modules.CarbonFootprintAnalyzer.commands;

import core.cli.commands.CommandInstance;

public class FootprintCommand extends CommandInstance {
    public FootprintCommand() {
        super(
                "footprint",
                "Log and analyze your carbon footprint as well as set reduction goals",
                new CommandInstance[] {
                        new FootprintLogCommand(),
                        new FootprintStatsCommand(),
                        new FootprintGoalCommand(),
                        new FootprintDataCommand()
                }
        );

        this.setAuthRequired(true);
    }
}
