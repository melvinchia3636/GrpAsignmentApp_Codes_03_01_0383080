package features.auth.handlers;

import core.cli.commands.CommandInstance;
import core.io.IOManager;
import core.manager.GlobalManager;
import core.terminal.Chalk;
import core.terminal.OutputUtils;

public class ListProfileHandler extends CommandInstance.Handler {
    @Override
    public void run() {
        IOManager ioManager = GlobalManager.getInstance().getIOManager();
        String[] profiles = ioManager.listProfiles();

        if (profiles.length == 0) {
            OutputUtils.printError("No profiles found.", false);
            System.out.println(new Chalk("Type 'signup <username> <password>' to create a new profile.").yellow());
            return;
        }

        System.out.println();
        System.out.printf(new Chalk("\uD83D\uDC64Found %d profile(s):%n%n").bold().toString(), profiles.length);
        for (String profile : profiles) {
            System.out.println("  - " + profile);
        }
        System.out.println();
        System.out.println(new Chalk("Type 'login <username> <password>' to log in to a profile.").yellow());
    }
}
