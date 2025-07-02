package core.instances;

/**
 * After some research, I found that there is no built-in JSON object in Java for some reason.
 * So, I've implemented a simple JSON object class that can handle basic JSON operations.
 * It does not support nested objects or arrays, but it can handle strings, integers, doubles, and booleans.
 * (Why? Because if I want to implement a full JSON parser, my sanity will be completely destroyed.)
 */
public class JSONObject {
    private SimpleMap<String, Object> map = new SimpleMap<>();

    /**
     * Puts a string value into the JSON object with the specified key.
     *
     * @param key   the key to associate with the value
     * @param value the string value to store
     */
    public void put(String key, String value) {
        map.put(key, value);
    }

    /**
     * Puts an integer value into the JSON object with the specified key.
     *
     * @param key   the key to associate with the value
     * @param value the integer value to store
     */
    public void put(String key, int value) {
        map.put(key, value);
    }

    /**
     * Puts a double value into the JSON object with the specified key.
     *
     * @param key   the key to associate with the value
     * @param value the double value to store
     */
    public void put(String key, double value) {
        map.put(key, value);
    }

    /**
     * Puts a boolean value into the JSON object with the specified key.
     *
     * @param key   the key to associate with the value
     * @param value the boolean value to store
     */
    public void put(String key, boolean value) {
        map.put(key, value);
    }

    /**
     * Retrieves a string value associated with the specified key.
     *
     * @param key the key whose associated value is to be returned
     * @return the string value, or null if not found or not a string
     */
    public String getString(String key) {
        Object value = map.get(key);
        return value instanceof String ? (String) value : null;
    }

    /**
     * Retrieves an integer value associated with the specified key.
     *
     * @param key the key whose associated value is to be returned
     * @return the integer value, or null if not found or not an integer
     */
    public Integer getInt(String key) {
        Object value = map.get(key);
        return value instanceof Integer ? (Integer) value : null;
    }

    /**
     * Retrieves a double value associated with the specified key.
     *
     * @param key the key whose associated value is to be returned
     * @return the double value, or null if not found or not a double
     */
    public Double getDouble(String key) {
        Object value = map.get(key);
        return value instanceof Double ? (Double) value : null;
    }

    /**
     * Retrieves a boolean value associated with the specified key.
     *
     * @param key the key whose associated value is to be returned
     * @return the boolean value, or null if not found or not a boolean
     */
    public Boolean getBoolean(String key) {
        Object value = map.get(key);
        return value instanceof Boolean ? (Boolean) value : null;
    }

    /**
     * Parses a JSON-formatted string and populates this object with its key-value pairs.
     * Only supports flat JSON objects with string, integer, double, boolean, or null values.
     *
     * @param json the JSON string to parse
     * @throws IllegalArgumentException if the JSON is invalid or contains unsupported types
     */
    public void parseFromString(String json) {
        SimpleMap<String, Object> newMap = new SimpleMap<>();

        json = json.trim();

        if (!json.startsWith("{") || !json.endsWith("}")) {
            throw new IllegalArgumentException("Invalid JSON object");
        }

        String content = json.substring(1, json.length() - 1).trim();
        if (content.isEmpty()) return;

        String[] pairs = content.split(",\\s*");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":", 2);
            if (keyValue.length != 2) {
                throw new IllegalArgumentException("Invalid key-value pair: " + pair);
            }

            String key = keyValue[0].trim().replace("\"", "");
            String value = keyValue[1].trim();

            if (value.startsWith("\"") && value.endsWith("\"")) {
                newMap.put(key, value.substring(1, value.length() - 1));
            } else if (value.equals("true") || value.equals("false")) {
                newMap.put(key, Boolean.parseBoolean(value));
            } else if (value.equals("null")) {
                newMap.put(key, null);
            } else if (value.matches("-?\\d+")) {
                newMap.put(key, Integer.parseInt(value));
            } else if (value.matches("-?\\d+(\\.\\d+)?")) {
                newMap.put(key, Double.parseDouble(value));
            } else if (value.matches("[a-zA-Z0-9_]+")) {
                // Handle unquoted alphanumeric strings (like challenge IDs)
                newMap.put(key, value);
            } else {
                throw new IllegalArgumentException("Unsupported value type: " + value);
            }
        }

        map = newMap;
    }

    /**
     * Returns a string representation of this JSON object in JSON format.
     *
     * @return the JSON string representation of this object
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (String key : map.keys()) {
            if (!first) {
                sb.append(", ");
            }

            Object value = map.get(key);
            sb.append("\"").append(key).append("\": ");
            
            // Properly format values based on their type
            if (value instanceof String) {
                sb.append("\"").append(value).append("\"");
            } else if (value == null) {
                sb.append("null");
            } else {
                sb.append(value);
            }
            
            first = false;
        }
        sb.append("}");

        return sb.toString();
    }
}
