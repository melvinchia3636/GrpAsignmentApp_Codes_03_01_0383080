package core.manager;

import core.io.IOManager;
import features.auth.data.UserManager;
import features.modules.CarbonFootprintAnalyzer.data.FootprintManager;
import features.modules.CarbonFootprintAnalyzer.data.GlobalEmissionsPerCapita;
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
    private static GlobalManager INSTANCE;

    private final UserManager userManager;
    private final IOManager ioManager;
    private final FootprintManager footprintManager;
    private final ChallengeManager challengeManager;
    private final HabitManager habitManager;
    private final GlobalEmissionsPerCapita globalEmissionsPerCapita;

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Initializes the managers for user, IO, footprint, challenge, and habit modules.
     */
    private GlobalManager(String emissionsDatasetFilePath) {
        if (INSTANCE != null) {
            throw new IllegalStateException("GlobalManager has already been initialized.");
        }

        INSTANCE = this;

        userManager = new UserManager();
        ioManager = new IOManager();
        footprintManager = new FootprintManager();
        challengeManager = new ChallengeManager();
        habitManager = new HabitManager();
        globalEmissionsPerCapita = new GlobalEmissionsPerCapita(emissionsDatasetFilePath);
    }

    /**
     * Static factory method to create the singleton instance of GlobalManager.
     * This method should be called once at the start of the application.
     *
     * @param emissionsDatasetFilePath the file path for the emissions dataset
     */
    public static void createInstance(String emissionsDatasetFilePath) {
        if (INSTANCE == null) {
            new GlobalManager(emissionsDatasetFilePath);
        }
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

    /**
     * Returns the GlobalEmissionsPerCapita instance.
     *
     * @return the GlobalEmissionsPerCapita instance
     */
    public GlobalEmissionsPerCapita getGlobalEmissionsPerCapita() {
        return globalEmissionsPerCapita;
    }
}