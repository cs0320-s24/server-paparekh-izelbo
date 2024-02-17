package edu.brown.cs.student.main.search;

import static edu.brown.cs.student.main.csv.CLI.logToFile;

import edu.brown.cs.student.main.exceptions.FactoryFailureException;
import edu.brown.cs.student.main.interfaces.CreatorFromRow;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A generic parser that reads data from a Reader source and converts each row into an instance of
 * type T using a specified CreatorFromRow implementation.
 *
 * @param <T> The type of objects to be created from each row of the input data.
 */
public class Parser<T> {

  private Reader file;
  private CreatorFromRow<T> creator;

  List<T> values;

  /**
   * Constructs a new Parser instance with the provided reader and creator.
   *
   * @param reader  The reader to read the data from.
   * @param creator The creator to convert rows of data into instances of T.
   */
  public Parser(Reader reader, CreatorFromRow<T> creator) {
    this.file = reader;
    this.creator = creator;
  }

  /**
   * Parses the data read from the provided {@link Reader}, converting each row into an instance of
   * T.
   *
   * @return A list of T instances created from the input data.
   * @throws IOException             If an I/O error occurs during reading.
   * @throws FactoryFailureException If object creation fails for a row.
   */
  public List<T> parse() {
    this.values = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(this.file)) {
      String line;
      while ((line = br.readLine()) != null) {
        // Convert the CSV row into a List<String>
        List<String> rowList = Arrays.asList(line.split(","));
        try {
          // Create an instance of T from the row and add it to the list
          T obj = creator.create(rowList);

          values.add(obj);
        } catch (FactoryFailureException e) {
          // Creation of T fails
          System.out.println("Error creating object from row. Exception: " + e);
          logToFile("Error creating object from row. Exception: " + e);
        }
      }
    } catch (IOException e) {
      System.out.println("There was an error reading the input. Exception: " + e);
      logToFile("Error reading the input. Exception: " + e);
    }
    return this.values;
  }

  public List<T> getValues() {
    return this.values;
  }
}
