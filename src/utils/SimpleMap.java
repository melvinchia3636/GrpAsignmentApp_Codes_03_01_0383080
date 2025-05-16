package utils;

public class SimpleMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;

    private Entry<K, V>[] entries;
    private int size = 0;

    @SuppressWarnings("unchecked")
    public SimpleMap() {
        entries = new Entry[INITIAL_CAPACITY];
    }

    public void put(K key, V value) {
        // Update existing key
        for (int i = 0; i < size; i++) {
            if (entries[i].key.equals(key)) {
                entries[i].value = value;
                return;
            }
        }

        // Add new key
        if (size == entries.length) {
            resize();
        }

        entries[size++] = new Entry<>(key, value);
    }

    public V get(K key) {
        for (int i = 0; i < size; i++) {
            if (entries[i].key.equals(key)) {
                return entries[i].value;
            }
        }
        return null;
    }

    public void remove(K key) {
        for (int i = 0; i < size; i++) {
            if (entries[i].key.equals(key)) {
                // Shift everything left
                for (int j = i; j < size - 1; j++) {
                    entries[j] = entries[j + 1];
                }
                entries[--size] = null;
                return;
            }
        }
    }

    public int size() {
        return size;
    }

    private void resize() {
        @SuppressWarnings("unchecked")
        Entry<K, V>[] newEntries = new Entry[entries.length * 2];
        System.arraycopy(entries, 0, newEntries, 0, entries.length);
        entries = newEntries;
    }

    // Inner Entry class
    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
