package core.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Since the project does not allow the use of Java's built-in Map interface, I'll just implement it myself :)
 * A simple map-like data structure using an ArrayList to store key-value pairs.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class SimpleMap<K, V> {
    private final ArrayList<Entry<K, V>> entries;

    public SimpleMap() {
        this.entries = new ArrayList<>();
    }

    public SimpleMap(ArrayList<Entry<K, V>> entries) {
        this.entries = entries;
    }

    /**
     * Adds a key-value pair to the map. If the key already exists, it updates the value.
     *
     * @param key   the key to add or update
     * @param value the value to associate with the key
     */
    public void put(K key, V value) {
        for (Entry<K, V> entry : entries) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }
        entries.add(new Entry<>(key, value));
    }

    /**
     * Retrieves the value associated with the specified key.
     * If the key does not exist, it returns null.
     *
     * @param key the key whose associated value is to be returned
     * @return the value associated with the specified key, or null if the key does not exist
     */
    public V get(K key) {
        for (Entry<K, V> entry : entries) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    /**
     * Removes the key-value pair associated with the specified key.
     * If the key does not exist, no action is taken.
     *
     * @param key the key to remove
     */
    public void remove(K key) {
        entries.removeIf(entry -> entry.key.equals(key));
    }

    /**
     * Gets the number of key-value pairs in the map.
     *
     * @return the number of entries in the map
     */
    public int size() {
        return entries.size();
    }

    /**
     * Returns a list of all entries in the map.
     *
     * @return a list of entries, where each entry is a key-value pair
     */
    public List<Entry<K, V>> entries() {
        return new ArrayList<>(entries);
    }

    public ArrayList<K> keys() {
        ArrayList<K> keys = new ArrayList<>();
        for (Entry<K, V> entry : entries) {
            keys.add(entry.key);
        }

        return keys;
    }

    public V[] values() {
        @SuppressWarnings("unchecked")
        V[] values = (V[]) new Object[entries.size()];

        for (int i = 0; i < entries.size(); i++) {
            values[i] = entries.get(i).value;
        }

        return values;
    }

    /**
     * Represents a key-value pair entry in the {@link SimpleMap}.
     *
     * @param <K> the type of keys maintained by this entry
     * @param <V> the type of mapped values
     */
    public static class Entry<K, V> {
        public final K key;
        public V value;

        /**
         * Constructs a new entry with the specified key and value.
         *
         * @param key   the key for this entry
         * @param value the value for this entry
         */
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
