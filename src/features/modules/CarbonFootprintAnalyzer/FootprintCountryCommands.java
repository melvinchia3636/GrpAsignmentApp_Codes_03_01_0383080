package features.modules.CarbonFootprintAnalyzer;

import core.cli.commands.CommandInstance;
import features.modules.CarbonFootprintAnalyzer.commands.country.FootprintCountryCompareCommand;
import features.modules.CarbonFootprintAnalyzer.commands.country.FootprintCountrySetCommand;

public class FootprintCountryCommands extends CommandInstance {
    public FootprintCountryCommands() {
        super(
                "country",
                "Analyze carbon footprints by country",
                new CommandInstance[] {
                        new FootprintCountrySetCommand(),
                        new FootprintCountryCompareCommand(),
                }
        );

        this.setAuthRequired(true);
    }
}
