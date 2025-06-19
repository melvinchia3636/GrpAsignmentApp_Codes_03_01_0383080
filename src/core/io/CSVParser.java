package core.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Since no third-party libraries are allowed, this CSVParser class is implemented
 * CSVParser is a simple utility class for reading and writing CSV files.
 * It provides methods to parse a CSV file into an ArrayList of String arrays
 * and to write a 2D array of Strings to a CSV file.
 */
public class CSVParser {
    /**
     * Parses a CSV file and returns its content as an ArrayList of String arrays.
     *
     * @param filePath the path to the CSV file
     * @return an ArrayList containing String arrays, each representing a row in the CSV file
     * @throws RuntimeException if an error occurs while reading the file
     */
    public static ArrayList<String[]> parseCSVFile(String filePath) {
        ArrayList<String[]> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                data.add(values);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file: " + e.getMessage(), e);
        }

        return data;
    }

    /**
     * Parses a CSV formatted String and returns its content as an ArrayList of String arrays.
     *
     * @param csv the CSV formatted String
     * @return an ArrayList containing String arrays, each representing a row in the CSV String
     */
    public static ArrayList<String[]> parseCSVString(String csv) {
        ArrayList<String[]> result = new ArrayList<>();
        String[] lines = csv.split("\n");

        for (String line : lines) {
            result.add(line.split(","));
        }

        return result;
    }

    /**
     * Writes a 2D array of Strings to a CSV file.
     *
     * @param filePath the path to the CSV file
     * @param data     a 2D array of Strings to be written to the CSV file
     * @throws RuntimeException if an error occurs while writing to the file
     */
    public static void toCSVFile(String filePath, String[][] data) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (String[] row : data) {
                writer.write(String.join(",", row));
                writer.write("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing CSV file: " + e.getMessage(), e);
        }
    }

    /**
     * Converts a 2D array of Strings to a CSV formatted String.
     *
     * @param data a 2D array of Strings to be converted
     * @return a String representing the CSV formatted data
     */
    public static String toCSVString(ArrayList<String[]> data) {
        StringBuilder sb = new StringBuilder();

        for (String[] row : data) {
            sb.append(String.join(",", row)).append("\n");
        }

        return sb.toString();
    }

}