package features.modules.CarbonFootprintAnalyzer.commands.data;

import core.cli.arguments.ArgumentDataType;
import core.cli.arguments.ArgumentList;
import core.cli.arguments.KeywordArgument;
import core.cli.commands.CommandInstance;
import features.modules.CarbonFootprintAnalyzer.handlers.data.FootprintDataExportHandler;

public class FootprintDataExportCommand extends CommandInstance {
    public FootprintDataExportCommand() {
        super("export", "Exports your carbon footprint data to a CSV file.",
                "-f my_footprint.csv",
                new ArgumentList(
                        new KeywordArgument("f", "file", "The name of the file to export your carbon footprint data to.", ArgumentDataType.STRING, false),
                        new KeywordArgument("d", "directory", "The directory where the file will be saved.", ArgumentDataType.STRING, false)
                ),
                new FootprintDataExportHandler()
        );
    }
}
