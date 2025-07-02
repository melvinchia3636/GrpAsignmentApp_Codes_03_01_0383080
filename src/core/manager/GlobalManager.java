package core.manager;

import core.io.IOManager;
import features.auth.data.UserManager;
import features.modules.CarbonFootprintAnalyzer.data.FootprintManager;
import features.modules.DailyEcoChallenge.data.ChallengeManager;
import features.modules.GreenHabitTracker.data.HabitManager;

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
    private final FootprintManager footprintManager;
    private final ChallengeManager challengeManager;
    private final HabitManager habitManager;

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Initializes the UserManager and IOManager instances.
     */
    private GlobalManager() {
        userManager = new UserManager();
        ioManager = new IOManager();
        footprintManager = new FootprintManager();
        challengeManager = new ChallengeManager();
        habitManager = new HabitManager();
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
     * Initializes the managers of each module after user login.
     */
    public void initModuleManagers() {
        footprintManager.init();
        challengeManager.init();
        habitManager.init();
    }

    public void reset() {
        userManager.reset();
        footprintManager.clearRecords();
        challengeManager.clearRecords();
        habitManager.clearRecords();
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

    /**
     * Returns the FootprintManager instance.
     *
     * @return the FootprintManager instance
     */
    public FootprintManager getFootprintManager() {
        return footprintManager;
    }

    /**
     * Returns the ChallengeManager instance.
     *
     * @return the ChallengeManager instance
     */
    public ChallengeManager getChallengeManager() {
        return challengeManager;
    }

    /**
     * Returns the HabitManager instance.
     *
     * @return the HabitManager instance
     */
    public HabitManager getHabitManager() {
        return habitManager;
    }
}