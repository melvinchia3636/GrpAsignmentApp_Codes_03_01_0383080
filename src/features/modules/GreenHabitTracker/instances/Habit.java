package features.modules.GreenHabitTracker.instances;

/**
 * Represents a green habit that a user wants to track.
 * Contains the habit's unique identifier and name.
 */
public class Habit {
    private final int id;
    private final String name;

    /**
     * Creates a new Habit instance.
     *
     * @param id   The unique identifier for the habit
     * @param name The name/description of the habit
     */
    public Habit(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Gets the unique identifier of the habit.
     *
     * @return The habit ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the habit.
     *
     * @return The habit name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a string representation of the habit.
     *
     * @return A formatted string containing the habit details
     */
    @Override
    public String toString() {
        return String.format("Habit{id=%d, name='%s'}", id, name);
    }

    /**
     * Converts this habit to a string array for CSV export.
     *
     * @return an array containing the habit ID and name
     */
    public String[] toArray() {
        return new String[]{String.valueOf(id), name};
    }

    /**
     * Checks if two habits are equal based on their ID.
     *
     * @param obj The object to compare with
     * @return true if the habits have the same ID, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Habit habit = (Habit) obj;
        return id == habit.id;
    }

    /**
     * Returns the hash code for the habit based on its ID.
     *
     * @return The hash code
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
