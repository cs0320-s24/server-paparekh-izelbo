package edu.brown.cs.student.main.search;

import java.util.EnumSet;

/**
 * The SearchOptions class allows for different search options such as case-sensitive,
 * case-insensitive, substring
 */
public class SearchOptions {
  private EnumSet<SearchType> type = EnumSet.noneOf(SearchType.class);
  private String column;

  /** Constructor for the SearchOptions class - initializes the type */
  public SearchOptions() {
    this.type.add(SearchType.CASE_SENSITIVE);
  }

  /**
   * Setter method for changing the type
   *
   * @param type EnumSet<SearchType> to be changed to
   */
  public void setTypeEnumSet(EnumSet<SearchType> type) {
    this.type = type;
  }

  /**
   * Checks if a specific search type is present
   *
   * @param searchType the specific search type to check
   * @return true if the search type is present, false otherwise
   */
  public Boolean isType(SearchType searchType) {
    return type.contains(searchType);
  }

  /**
   * Getter method to retrieve the column
   *
   * @return the column
   */
  public String getColumn() {
    return column;
  }

  /**
   * Sets the target column for search
   *
   * @param column specific column for search
   * @return modified SearchOptions instance
   */
  public SearchOptions setColumn(String column) {
    this.column = column;
    return this;
  }

  /**
   * Basic method to get proper string representation
   *
   * @return formatted String
   */
  @Override
  public String toString() {
    return "SearchOptions{" + "type=" + type + ", column='" + column + '\'' + '}';
  }
}
