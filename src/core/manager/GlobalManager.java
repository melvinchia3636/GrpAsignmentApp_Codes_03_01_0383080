package core.manager;

import core.io.IOManager;
import features.auth.UserManager;

/**
 * GlobalManager is a singleton class that provides access to global instances
 * of UserManager and IOManager. It ensures that only one instance of each manager
 * is created and used throughout the application.
 * <p>
 * In ReactJS, this would be similar to using a context provider to share state
 */
public class GlobalManager {
    private static final GlobalManager INSTANCE = new GlobalManager();

    private final UserManager userManager;
    private final IOManager ioManager;

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Initializes the UserManager and IOManager instances.
     */
    private GlobalManager() {
        userManager = new UserManager();
        ioManager = new IOManager();
    }

    /**
     * Returns the singleton instance of GlobalManager.
     *
     * @return the single instance of GlobalManager
     */
    public static GlobalManager getInstance() {
        return INSTANCE;
    }

    /**
     * Returns the UserManager instance.
     *
     * @return the UserManager instance
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * Returns the IOManager instance.
     *
     * @return the IOManager instance
     */
    public IOManager getIOManager() {
        return ioManager;
    }
}