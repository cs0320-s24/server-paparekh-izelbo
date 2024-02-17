package edu.brown.cs.student.main.interfaces;

import java.util.List;

/**
 * Implements the CreatorFromRow interface to convert a List of Strings into an array of Strings.
 */
public class Creator implements CreatorFromRow<String[]> {
  @Override
  public String[] create(List<String> row) {
    return row.toArray(new String[0]);
  }
}

