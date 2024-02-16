package edu.brown.cs.student.main.datamodel;

import edu.brown.cs.student.main.parser.CreatorFromRow;
import edu.brown.cs.student.main.parser.FactoryFailureException;
import java.util.ArrayList;
import java.util.List;

/**
 * The StringListCreator class implements the CreatorFromRow interface. It creates an ArrayList
 * based on the List row
 */
public class StringListCreator implements CreatorFromRow<ArrayList<String>> {

  /**
   * Creates an ArrayList<String> from the input List<String> row
   *
   * @param row List<String> representing the row of values
   * @return ArrayList<String> that contains the elements of the input row
   * @throws FactoryFailureException when an error occurs during ArrayList creation
   */
  @Override
  public ArrayList<String> create(List<String> row) throws FactoryFailureException {
    return (ArrayList<String>) row;
  }
}
