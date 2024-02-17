package edu.brown.cs.student.main.server;

/**
 * Enumeration representing the different search types: CASE_SENSITIVE, CASE_INSENSITIVE, EXACT,
 * SUB_STRING
 */
public enum SearchType {
  CASE_SENSITIVE(1),
  CASE_INSENSITIVE(2),
  EXACT(4),
  SUB_STRING(8);

  private int id;

  SearchType(int id) {
    this.id = id;
  }
}
