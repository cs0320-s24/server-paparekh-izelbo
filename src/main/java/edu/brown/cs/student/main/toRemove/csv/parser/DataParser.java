package edu.brown.cs.student.main.toRemove.csv.parser;

import java.io.IOException;

/**
 * The DataParser interface allows for generic data processing template
 *
 * @param <R> Generic object
 */
public interface DataParser<R> {
  public void processData(R reader, boolean hasHeaders) throws IOException, FactoryFailureException;
}
