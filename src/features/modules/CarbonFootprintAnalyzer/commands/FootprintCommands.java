package features.modules.CarbonFootprintAnalyzer.commands;

import core.cli.commands.CommandInstance;

public class FootprintCommands extends CommandInstance {
    public FootprintCommands() {
        super(
                "footprint",
                "Log and analyze your carbon footprint as well as set reduction goals",
                new CommandInstance[] {
                        new FootprintLogCommand(),
                        new FootprintStatsCommands(),
                        new FootprintGoalCommands(),
                        new FootprintDataCommands()
                }
        );

        this.setAuthRequired(true);
    }
}
