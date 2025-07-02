package features.modules.GreenHabitTracker.instances;

import core.instances.Timestamp;

/**
 * Represents a record of completing a habit on a specific date.
 * Links a habit to the timestamp when it was completed.
 */
public class HabitRecord {
    private final int habitId;
    private final Timestamp timestamp;

    /**
     * Creates a new HabitRecord instance.
     *
     * @param habitId   The ID of the habit that was completed
     * @param timestamp The timestamp when the habit was completed
     */
    public HabitRecord(int habitId, Timestamp timestamp) {
        this.habitId = habitId;
        this.timestamp = timestamp;
    }

    /**
     * Gets the ID of the habit that was completed.
     *
     * @return The habit ID
     */
    public int getHabitId() {
        return habitId;
    }

    /**
     * Gets the timestamp when the habit was completed.
     *
     * @return The completion timestamp
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Returns a string representation of the habit record.
     *
     * @return A formatted string containing the habit ID and timestamp
     */
    @Override
    public String toString() {
        return String.format("HabitRecord{habitId=%d, timestamp=%s}", habitId, timestamp);
    }

    /**
     * Converts this record to a string array for CSV export.
     *
     * @return an array containing the habit ID and timestamp
     */
    public String[] toArray() {
        return new String[]{String.valueOf(habitId), String.valueOf(timestamp.getTimestamp())};
    }

    /**
     * Checks if two habit records are equal based on their habit ID and date.
     *
     * @param obj The object to compare with
     * @return true if the records have the same habit ID and are on the same date, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        HabitRecord record = (HabitRecord) obj;
        return habitId == record.habitId &&
                timestamp.getYear() == record.timestamp.getYear() &&
                timestamp.getMonth() == record.timestamp.getMonth() &&
                timestamp.getDay() == record.timestamp.getDay();
    }

    /**
     * Returns the hash code for the habit record.
     *
     * @return The hash code
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(habitId) * 31 + 
               (timestamp.getYear() * 10000 + timestamp.getMonth() * 100 + timestamp.getDay());
    }
}
