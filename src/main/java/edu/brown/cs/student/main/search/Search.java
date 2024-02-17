package edu.brown.cs.student.main.search;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that provides functionality to search for specific words within a CSV file. It can
 * perform searches across all columns or within a specific column.
 */
public class Search {

  private String wordToFind;
  private List<String[]> values;

  private String columnToSearch;

  private final StringBuilder output = new StringBuilder();

  private final List<int[]> foundPositions = new ArrayList<>();

  /** Constructor for Search class */
  public Search() {}

  /**
   * Searches for a word in a CSV file using a provided parser. The search can be done across all
   * columns or within a specific column based on the parameters provided.
   *
   * @param elements List of elements parsed from the CSV file.
   * @param wordToFind The word to search for within the CSV.
   * @param columnToSearch The specific column to search within. An empty string indicates a search
   *     across all columns.
   */
  public void CSVSearch(List<String[]> elements, String wordToFind, String columnToSearch) {

    this.values = elements;
    this.wordToFind = wordToFind;
    this.columnToSearch = columnToSearch;

    this.foundPositions.clear();

    // Determine which search method to use
    if (columnToSearch.equals("")) {
      searchAll();
    } else {
      searchColumn();
    }
  }

  /** Searches for a word across all columns of the CSV data. */
  public void searchAll() {
    if (this.values == null || this.values.isEmpty()) {
      printLogger("No data found in CSV");
      // logToFile("No data found in CSV");
      return; // Exit method if values are null or empty
    }

    boolean isWordFound = false; // Flag to check if the word was found at least once

    for (int i = 0; i < this.values.size(); i++) {
      String[] row = this.values.get(i);
      if (row == null) {
        continue; // Skip null rows
      }

      for (int j = 0; j < row.length; j++) {
        if (row[j] != null && row[j].equalsIgnoreCase(this.wordToFind)) {
          this.foundPositions.add(new int[] {i, j});
          isWordFound = true; // Set flag to true bc we found the word
        }
      }
    }

    if (isWordFound) {
      String readablePositions = convertToReadableString();
      printLogger(this.wordToFind + " was found in " + readablePositions);
    } else {
      printLogger("The word " + this.wordToFind + " was not found in any row.");
      // logToFile("The word " + this.wordToFind + " was not found in any row.");
    }
  }

  /** Searches for a word within a specific column of the CSV data. */
  private void searchColumn() {
    if (this.values == null || this.values.isEmpty()) {
      printLogger("No data found in CSV.");
      // logToFile("No data found in CSV.");
      return;
    }

    int columnIndex = determineColumnIndex();
    if (columnIndex == -1) {
      return;
    }

    boolean isWordFound = false;

    for (int i = 1; i < this.values.size(); i++) { // Start from 1 to skip the header row
      String[] row = this.values.get(i);
      if (row == null || row.length <= columnIndex) {
        continue; // Skip null rows or rows with insufficient columns
      }

      String cellValue = row[columnIndex];
      if (cellValue != null && cellValue.equalsIgnoreCase(wordToFind)) {
        foundPositions.add(new int[] {i, columnIndex});
        isWordFound = true;
      }
    }

    if (isWordFound) {
      String readablePositions = convertToReadableString();
      printLogger(this.wordToFind + " was found in " + readablePositions);
    } else {
      printLogger("Word " + this.wordToFind + " not found in column " + this.columnToSearch + ".");
      // logToFile("Word " + this.wordToFind + " not found in column " + this.columnToSearch + ".");
    }
  }

  /**
   * Determines the index of a column based on a string identifier, which could be an integer
   * (representing column index) or a string (representing column name).
   *
   * @return The index of the column if found, or -1 if the column cannot be identified.
   * @throws NumberFormatException If the column identifier is an integer -- but not in a valid
   *     format.
   */
  private int determineColumnIndex() throws NumberFormatException {
    // Test if `columnToSearch` is an integer
    try {
      int columnIndex = Integer.parseInt(this.columnToSearch);
      // Check if index is within the valid range
      if (columnIndex >= 0 && columnIndex < this.values.get(0).length) {
        return columnIndex; // Return the index if it's valid
      }
    } catch (NumberFormatException e) {
      // If it's not an int, assume it's a column name and search for it
      String[] headers = this.values.get(0);
      for (int i = 0; i < headers.length; i++) {
        if (headers[i].equalsIgnoreCase(this.columnToSearch)) {
          return i; // Column name found, return its index.
        }
      }
    }
    // If the code reaches this point, the column name was not found
    printLogger("Column " + this.columnToSearch + " not found.");
    // logToFile("Column " + this.columnToSearch + " not found.");
    return -1; //  column wasn't found
  }

  /**
   * Logs message to the output
   *
   * @param message String to be logged
   */
  private void printLogger(String message) {
    System.out.println(message);
    this.output.append(message);
  }

  /**
   * Converts the positions array into a string representation
   *
   * @return String containing the found positions
   */
  private String convertToReadableString() {
    StringBuilder sb = new StringBuilder();
    String[][] positionsArray = getFoundPositionsArray();
    for (String[] position : positionsArray) {
      if (sb.length() > 0) sb.append(", ");
      sb.append("[").append(position[0]).append(",").append(position[1]).append("]");
    }
    return sb.toString();
  }

  /**
   * Gets the position as a 2D array of Strings
   *
   * @return 2D array of Strings representing the found positions
   */
  public String[][] getFoundPositionsArray() {
    String[][] positionsArray = new String[foundPositions.size()][2];
    for (int i = 0; i < foundPositions.size(); i++) {
      int[] position = foundPositions.get(i);
      positionsArray[i][0] = String.valueOf(position[0]);
      positionsArray[i][1] = String.valueOf(position[1]);
    }
    return positionsArray;
  }
  /**
   * Returns the collected search results and messages as a String. This method allows for testing
   * the output of search operations without side effects.
   *
   * @return A string representation of the search results and any relevant messages.
   */
  public String getOutput() {
    String result = this.output.toString();
    output.setLength(0);
    return result;
  }
}
