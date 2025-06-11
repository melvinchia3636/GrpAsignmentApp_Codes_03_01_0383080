package features.terminal.commands;

import core.cli.arguments.ArgumentDataType;
import core.cli.arguments.ArgumentList;
import core.cli.arguments.KeywordArgument;
import core.cli.commands.CommandInstance;
import features.terminal.handlers.HelpHandler;

public class HelpCommand extends CommandInstance {
    public HelpCommand() {
        super(
                "help",
                "Display help information for commands.",
                "-c login",
                new ArgumentList(
                        new KeywordArgument[]{
                                new KeywordArgument("command", "c", "The command to get help for", ArgumentDataType.STRING, false),
                                new KeywordArgument("tree", "t", "Display all commands in a tree structure", ArgumentDataType.FLAG, false)
                        }
                ),
                new HelpHandler()
        );
    }
}
