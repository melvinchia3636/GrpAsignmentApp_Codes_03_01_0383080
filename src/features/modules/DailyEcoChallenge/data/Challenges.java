package features.modules.DailyEcoChallenge.data;

import features.modules.DailyEcoChallenge.instances.Challenge;

/**
 * Contains predefined daily eco challenges with varying difficulty levels.
 * Each challenge includes an ID, description of the environmental action,
 * and difficulty level to help users choose appropriate challenges.
 * Can be extended with additional challenges as needed.
 */
public class Challenges {
    public static final Challenge[] CHALLENGES = {
            // Easy challenges
            new Challenge("ECO001", "Turn off all lights when leaving a room for 5+ minutes", "Easy"),
            new Challenge("ECO002", "Use a reusable water bottle instead of disposable ones today", "Easy"),
            new Challenge("ECO003", "Unplug electronic devices when not in use", "Easy"),
            new Challenge("ECO004", "Take a 5-minute shorter shower than usual", "Easy"),
            new Challenge("ECO005", "Walk or bike instead of driving for trips under 1 mile", "Easy"),
            new Challenge("ECO006", "Use both sides of paper before recycling", "Easy"),
            new Challenge("ECO007", "Bring your own shopping bag to the store", "Easy"),
            new Challenge("ECO008", "Turn off the tap while brushing your teeth", "Easy"),
            new Challenge("ECO009", "Choose digital receipts instead of paper ones", "Easy"),
            new Challenge("ECO010", "Set your thermostat 2 degrees lower in winter or higher in summer", "Easy"),

            // Medium challenges
            new Challenge("ECO011", "Prepare a completely plant-based meal", "Medium"),
            new Challenge("ECO012", "Use public transportation instead of driving", "Medium"),
            new Challenge("ECO013", "Start a small composting bin for food scraps", "Medium"),
            new Challenge("ECO014", "Reduce meat consumption by having two vegetarian meals", "Medium"),
            new Challenge("ECO015", "Organize a carpool with friends or colleagues", "Medium"),
            new Challenge("ECO016", "Replace 3 single-use items with reusable alternatives", "Medium"),
            new Challenge("ECO017", "Research and switch to a renewable energy provider", "Medium"),
            new Challenge("ECO018", "Create a weekly meal plan to reduce food waste", "Medium"),
            new Challenge("ECO019", "Set up a rain water collection system for plants", "Medium"),
            new Challenge("ECO020", "Do a home energy audit and identify 3 improvements", "Medium"),

            // Hard challenges
            new Challenge("ECO021", "Go completely zero-waste for 24 hours", "Hard"),
            new Challenge("ECO022", "Use no single-use plastics for an entire week", "Hard"),
            new Challenge("ECO023", "Organize a community cleanup event", "Hard"),
            new Challenge("ECO024", "Convert your lawn to native plants or edible garden", "Hard"),
            new Challenge("ECO025", "Implement a household greywater recycling system", "Hard"),
            new Challenge("ECO026", "Go car-free for an entire week", "Hard"),
            new Challenge("ECO027", "Install solar panels or solar water heater", "Hard"),
            new Challenge("ECO028", "Convince 5 friends to take on eco-friendly habits", "Hard"),
            new Challenge("ECO029", "Make all household cleaners from natural ingredients", "Hard"),
            new Challenge("ECO030", "Start a neighborhood tool/equipment sharing program", "Hard")
    };

    /**
     * Retrieves a Challenge by its unique ID.
     *
     * @param id The unique identifier of the challenge
     * @return The Challenge corresponding to the ID
     * @throws IllegalArgumentException if no challenge is found with the given ID
     */
    public static Challenge getChallengeById(String id) {
        for (Challenge challenge : CHALLENGES) {
            if (challenge.getId().equalsIgnoreCase(id)) {
                return challenge;
            }
        }
        throw new IllegalArgumentException("No challenge found with ID: " + id);
    }

    /**
     * Retrieves all challenges of a specific difficulty level.
     *
     * @param difficulty The difficulty level ("Easy", "Medium", "Hard")
     * @return An array of challenges matching the specified difficulty
     */
    public static Challenge[] getChallengesByDifficulty(String difficulty) {
        return java.util.Arrays.stream(CHALLENGES)
                .filter(challenge -> challenge.getDifficulty().equalsIgnoreCase(difficulty))
                .toArray(Challenge[]::new);
    }

    /**
     * Gets a random challenge from all available challenges.
     *
     * @return A randomly selected challenge
     */
    public static Challenge getRandomChallenge() {
        int randomIndex = (int) (Math.random() * CHALLENGES.length);
        return CHALLENGES[randomIndex];
    }

    /**
     * Gets a random challenge of a specific difficulty level.
     *
     * @param difficulty The desired difficulty level
     * @return A randomly selected challenge of the specified difficulty
     * @throws IllegalArgumentException if no challenges exist for the given difficulty
     */
    public static Challenge getRandomChallengeByDifficulty(String difficulty) {
        Challenge[] difficultyChallenges = getChallengesByDifficulty(difficulty);
        if (difficultyChallenges.length == 0) {
            throw new IllegalArgumentException("No challenges found for difficulty: " + difficulty);
        }
        int randomIndex = (int) (Math.random() * difficultyChallenges.length);
        return difficultyChallenges[randomIndex];
    }

    /**
     * Gets the total number of available challenges.
     *
     * @return The total count of challenges
     */
    public static int getTotalChallengeCount() {
        return CHALLENGES.length;
    }

    /**
     * Gets the count of challenges for a specific difficulty level.
     *
     * @param difficulty The difficulty level to count
     * @return The number of challenges for the specified difficulty
     */
    public static int getChallengeCountByDifficulty(String difficulty) {
        return getChallengesByDifficulty(difficulty).length;
    }
}
