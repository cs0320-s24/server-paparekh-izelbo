package edu.brown.cs.student.main.interfaces;

import java.util.List;

/** Implements the CreatorFromRow interface to create Star objects from a given List of values. */
public class StarCreator implements CreatorFromRow<Star> {

  @Override
  public Star create(List row) {
    String id = (String) row.get(0);
    String ProperName = (String) row.get(1);
    String X = (String) row.get(2);
    String Y = (String) row.get(3);
    String Z = (String) row.get(4);
    return new Star(id, ProperName, X, Y, Z);
  }
}
