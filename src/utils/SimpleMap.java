package utils;

import java.util.Arrays;

/**
 * Since the project requirement doesn't allow the use of Java's built-in Map,
 * we implement a simple map-like structure using an array. :)
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class SimpleMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;

    private Entry<K, V>[] entries;
    private int size = 0;

    /**
     * Constructs an empty SimpleMap with an initial capacity.
     */
    @SuppressWarnings("unchecked")
    public SimpleMap() {
        entries = new Entry[INITIAL_CAPACITY];
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old value is replaced.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     */
    public void put(K key, V value) {
        // Update existing key
        for (int i = 0; i < size; i++) {
            if (entries[i].key.equals(key)) {
                entries[i].value = value;
                return;
            }
        }

        // Resize if necessary
        if (size == entries.length) {
            resize();
        }

        // Add new key-value pair
        entries[size++] = new Entry<>(key, value);
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or null if not found
     */
    public V get(K key) {
        for (int i = 0; i < size; i++) {
            if (entries[i].key.equals(key)) {
                return entries[i].value;
            }
        }
        return null;
    }

    /**
     * Returns true if this map contains a mapping for the specified key.
     *
     * @param key key whose presence in this map is to be tested
     * @return true if this map contains a mapping for the specified key
     */
    public boolean has(K key) {
        for (int i = 0; i < size; i++) {
            if (entries[i].key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public Entry<K, V>[] entries() {
        return Arrays.copyOfRange(entries, 0, size);
    }

    /**
     * Removes the mapping for a key from this map if it is present.
     *
     * @param key key whose mapping is to be removed from the map
     */
    public void remove(K key) {
        for (int i = 0; i < size; i++) {
            if (entries[i].key.equals(key)) {
                // Shift everything left to fill the gap
                for (int j = i; j < size - 1; j++) {
                    entries[j] = entries[j + 1];
                }
                entries[--size] = null;
                return;
            }
        }
    }

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of key-value mappings in this map
     */
    public int size() {
        return size;
    }

    /**
     * Doubles the capacity of the entries array when it is full.
     */
    private void resize() {
        @SuppressWarnings("unchecked")
        Entry<K, V>[] newEntries = new Entry[entries.length * 2];
        System.arraycopy(entries, 0, newEntries, 0, entries.length);
        entries = newEntries;
    }

    /**
     * Inner class representing a key-value pair.
     *
     * @param <K> key type
     * @param <V> value type
     */
    public static class Entry<K, V> {
        K key;
        V value;

        /**
         * Constructs an Entry with the specified key and value.
         *
         * @param key   the key
         * @param value the value
         */
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Returns the key of this entry.
         *
         * @return the key
         */
        public K getKey() {
            return key;
        }

        /**
         * Returns the value of this entry.
         *
         * @return the value
         */
        public V getValue() {
            return value;
        }
    }
}
