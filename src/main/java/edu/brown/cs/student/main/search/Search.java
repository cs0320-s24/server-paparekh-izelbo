package edu.brown.cs.student.main.search;

import static edu.brown.cs.student.main.csv.CLI.logToFile;

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

  private final StringBuilder foundPositions = new StringBuilder();

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
      logToFile("No data found in CSV");
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
          if (isWordFound) {
            // If not the first found instance, prepend ", " for formatting
            this.foundPositions.append(", ");
          }
          this.foundPositions.append("[").append(i).append(",").append(j).append("]");
          isWordFound = true; // Set flag to true bc we found the word
        }
      }
    }

    if (isWordFound) {
      printLogger(this.wordToFind + " was found in " + this.foundPositions);
    } else {
      printLogger("The word " + this.wordToFind + " was not found in any row.");
      logToFile("The word " + this.wordToFind + " was not found in any row.");
    }
  }

  /** Searches for a word within a specific column of the CSV data. */
  private void searchColumn() {
    if (this.values == null || this.values.isEmpty()) {
      printLogger("No data found in CSV.");
      logToFile("No data found in CSV.");
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
        if (isWordFound) {
          this.foundPositions.append(", ");
        }
        this.foundPositions.append("[").append(i).append(",").append(columnIndex).append("]");
        isWordFound = true;
      }
    }

    if (isWordFound) {
      printLogger(this.wordToFind + " was found in " + this.foundPositions);
    } else {
      printLogger("Word " + this.wordToFind + " not found in column " + this.columnToSearch + ".");
      logToFile("Word " + this.wordToFind + " not found in column " + this.columnToSearch + ".");
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
    logToFile("Column " + this.columnToSearch + " not found.");
    return -1; //  column wasn't found
  }

  private void printLogger(String message) {
    System.out.println(message);
    this.output.append(message);
  }


  public StringBuilder getMatches() {
    return this.foundPositions;
  }


  /**
   * Returns the collected search results and messages as a String. This method allows for testing
   * the output of search operations without side effects.
   *
   * @return A string representation of the search results and any relevant messages.
   */
  public String getOutput() {
    // Ensure to call CSVSearch before calling this method to populate `output` appropriately
    String result = this.output.toString();
    output.setLength(0);
    return result;
  }
}