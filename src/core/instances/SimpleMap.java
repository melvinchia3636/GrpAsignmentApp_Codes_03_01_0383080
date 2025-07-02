package core.instances;

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

    /**
     * Adds a key-value pair to the map. If the key already exists, it updates the value.
     *
     * @param key   the key to add or update
     * @param value the value to associate with the key
     */
    public void put(K key, V value) {
        for (Entry<K, V> entry : entries) {
            if (entry.getKey().equals(key)) {
                entry.setValue(value);
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
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public boolean containsKey(K key) {
        for (Entry<K, V> entry : entries) {
            if (entry.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public void sortByValue() {
        entries.sort((e1, e2) -> {
            if (e1.getValue() instanceof Comparable && e2.getValue() instanceof Comparable) {
                return ((Comparable<V>) e1.getValue()).compareTo(e2.getValue());
            }

            return 0; // If values are not comparable, do not change order
        });
    }

    public void reverse() {
        ArrayList<Entry<K, V>> reversedEntries = new ArrayList<>();

        for (int i = entries.size() - 1; i >= 0; i--) {
            reversedEntries.add(entries.get(i));
        }

        entries.clear();
        entries.addAll(reversedEntries);
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
            keys.add(entry.getKey());
        }

        return keys;
    }

    public V[] values() {
        @SuppressWarnings("unchecked")
        V[] values = (V[]) new Object[entries.size()];

        for (int i = 0; i < entries.size(); i++) {
            values[i] = entries.get(i).getValue();
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
        private final K key;
        private V value;

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

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SimpleMap{");
        for (Entry<K, V> entry : entries) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append(", ");
        }
        if (sb.length() > 10) { // Remove trailing comma and space
            sb.setLength(sb.length() - 2);
        }
        sb.append('}');
        return sb.toString();
    }
}
