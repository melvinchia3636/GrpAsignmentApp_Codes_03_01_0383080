package features.modules.DailyEcoChallenge.instances;

/**
 * Represents a daily eco challenge with unique identifier, description, and difficulty level.
 * This class stores the details of an environmental challenge that users can participate in
 * to promote sustainable living and environmental awareness.
 */
public class Challenge {
    private final String id;
    private final String description;
    private final String difficulty;

    /**
     * Creates a new Challenge instance.
     *
     * @param id The unique identifier for the challenge
     * @param description The detailed description of what the challenge involves
     * @param difficulty The difficulty level of the challenge (e.g., "Easy", "Medium", "Hard")
     */
    public Challenge(String id, String description, String difficulty) {
        this.id = id;
        this.description = description;
        this.difficulty = difficulty;
    }

    /**
     * Gets the unique identifier of the challenge.
     *
     * @return The challenge ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the description of the challenge.
     *
     * @return The challenge description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the difficulty level of the challenge.
     *
     * @return The challenge difficulty
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Returns a string representation of the challenge.
     *
     * @return A formatted string containing the challenge details
     */
    @Override
    public String toString() {
        return String.format("Challenge{id='%s', description='%s', difficulty='%s'}", 
                           id, description, difficulty);
    }

    /**
     * Checks if two challenges are equal based on their ID.
     *
     * @param obj The object to compare with
     * @return true if the challenges have the same ID, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Challenge challenge = (Challenge) obj;
        return id.equals(challenge.id);
    }

    /**
     * Returns the hash code for the challenge based on its ID.
     *
     * @return The hash code
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
