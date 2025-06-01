package features.terminal.handlers;


import core.cli.commands.CommandInstance;
import core.terminal.Chalk;

public class ExitHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        System.out.println(new Chalk("Exiting the application. Goodbye!").purple().bold());
        System.exit(0);
    }
}
