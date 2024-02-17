package edu.brown.cs.student.main.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The SearchResult class processes the result of a search operation such as the number of
 * matches/row and column indices
 */
public class SearchResult {
  private int totalFound;
  private HashMap<Integer, ArrayList<Integer>> results = new HashMap<>();

  /** Constructor for SearchResult class that initializes totalFound and clears the result */
  public SearchResult() {
    totalFound = 0;
    results.clear();
  }

  /**
   * Adds a matching result based on the row and column indices
   *
   * @param row - row integer
   * @param column - column integer
   */
  public void addResult(Integer row, Integer column) {
    if (!results.containsKey(row)) {
      results.put(row, new ArrayList<>());
      totalFound++;
    }

    results.get(row).add(column);
  }

  /**
   * Gets the results by returning the HashMap
   *
   * @return results HashMap
   */
  public HashMap<Integer, ArrayList<Integer>> getResults() {
    return results;
  }

  /**
   * Gets the results by returning the ArrayList
   *
   * @param row Integer row value
   * @return ArrayList corresponding to the row
   */
  public ArrayList<Integer> getResults(Integer row) {
    return results.getOrDefault(row, null);
  }

  /**
   * Getter for the totalFound
   *
   * @return totalFound integer
   */
  public int getTotalFound() {
    return totalFound;
  }

  /** Prints the search results in a well-formatted message */
  public void printResults() {
    if (totalFound > 0) {
      System.out.println(" Count -  Row [Columns]:");

      Integer[] sortedRows = results.keySet().toArray(new Integer[0]);
      Arrays.sort(sortedRows);

      int index = 1;
      for (Integer row : sortedRows) {
        System.out.print(
            String.format("  %4d - %4d %s\n", index++, row, results.get(row).toString()));
      }
    }
    System.out.println(String.format("Total rows found: %d", totalFound));
  }
}
