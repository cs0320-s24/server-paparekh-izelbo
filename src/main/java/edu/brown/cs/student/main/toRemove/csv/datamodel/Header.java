package edu.brown.cs.student.main.toRemove.csv.datamodel;

import java.util.ArrayList;
import java.util.List;

/**
 * The Header class contains the header information and deals with retrieving column indices and
 * getting the total number of columns
 */
public class Header {
  List<String> columnNames = new ArrayList<>();

  /** Constructor for the Header class */
  public Header() {}

  /**
   * Initializes the columnNames instance variable
   *
   * @param columnNames
   */
  public void initialize(List<String> columnNames) {
    this.columnNames = columnNames;
  }

  /**
   * Retrieves the column index based on the String column input/name
   *
   * @param columnName String columnName
   * @return Integer representing the column index
   */
  public Integer getColumnIndex(String columnName) {
    Integer columnIndex = null;

    try {
      columnIndex = Integer.parseInt(columnName);

      // Check for out-of-bound / invalid column index
      if (columnIndex >= columnNames.size()) {
        columnIndex = -1;
      }
    } catch (NumberFormatException e) {
      return columnNames.indexOf(columnName);
    }
    return columnIndex;
  }

  /**
   * Gets the number of columns
   *
   * @return Integer representing the size of the columns
   */
  public Integer getSize() {
    return columnNames.size();
  }
}
