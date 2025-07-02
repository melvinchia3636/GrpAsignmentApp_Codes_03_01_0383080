package features.modules.DailyEcoChallenge.instances;

import core.instances.Timestamp;

/**
 * Represents a record of a completed or attempted daily eco challenge.
 * This class stores information about when a challenge was attempted,
 * whether it was completed, and any relevant details about the completion.
 */
public class ChallengeRecord {
    private final int index;
    private final Challenge challenge;
    private final String status; // "completed", "skipped", "failed"
    private final Timestamp timestamp;
    private final String notes; // Optional notes about the challenge completion

    /**
     * Creates a new ChallengeRecord instance.
     *
     * @param index The unique index for this record
     * @param challenge The challenge that was attempted
     * @param status The completion status ("completed", "skipped", "failed")
     * @param timestamp When the challenge was recorded
     * @param notes Optional notes about the challenge (can be empty string)
     */
    public ChallengeRecord(int index, Challenge challenge, String status, Timestamp timestamp, String notes) {
        this.index = index;
        this.challenge = challenge;
        this.status = status;
        this.timestamp = timestamp;
        this.notes = notes != null ? notes : "";
    }

    /**
     * Creates a new ChallengeRecord instance without notes.
     *
     * @param index The unique index for this record
     * @param challenge The challenge that was attempted
     * @param status The completion status
     * @param timestamp When the challenge was recorded
     */
    public ChallengeRecord(int index, Challenge challenge, String status, Timestamp timestamp) {
        this(index, challenge, status, timestamp, "");
    }

    /**
     * Converts the record to a string array for CSV storage.
     *
     * @return String array containing [challengeId, status, timestamp, notes]
     */
    public String[] toArray() {
        return new String[]{
                getChallenge().getId(),
                getStatus(),
                String.valueOf(getTimestamp().getTimestamp()),
                getNotes()
        };
    }

    /**
     * Gets the unique index of this record.
     *
     * @return The record index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets the challenge associated with this record.
     *
     * @return The challenge
     */
    public Challenge getChallenge() {
        return challenge;
    }

    /**
     * Gets the completion status of the challenge.
     *
     * @return The status ("completed", "skipped", "failed")
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets the timestamp when the challenge was recorded.
     *
     * @return The timestamp
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Gets any notes about the challenge completion.
     *
     * @return The notes (empty string if no notes)
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Checks if the challenge was completed successfully.
     *
     * @return true if status is "completed", false otherwise
     */
    public boolean isCompleted() {
        return "completed".equalsIgnoreCase(status);
    }

    /**
     * Checks if the challenge was skipped.
     *
     * @return true if status is "skipped", false otherwise
     */
    public boolean isSkipped() {
        return "skipped".equalsIgnoreCase(status);
    }

    /**
     * Checks if the challenge was failed.
     *
     * @return true if status is "failed", false otherwise
     */
    public boolean isFailed() {
        return "failed".equalsIgnoreCase(status);
    }

    /**
     * Returns a string representation of the challenge record.
     *
     * @return A formatted string containing the record details
     */
    @Override
    public String toString() {
        return String.format("ChallengeRecord{index=%d, challenge='%s', status='%s', timestamp=%s, notes='%s'}",
                           index, challenge.getId(), status, timestamp, notes);
    }
}
