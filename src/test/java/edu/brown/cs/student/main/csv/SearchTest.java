package edu.brown.cs.student.main.csv;

import static org.testng.internal.junit.ArrayAsserts.assertArrayEquals;

import edu.brown.cs.student.main.interfaces.Creator;
import edu.brown.cs.student.main.search.Parser;
import edu.brown.cs.student.main.search.Search;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Search class.
 *
 * @author Ian Zelbo
 * @version 7.0
 */
public class SearchTest {

  /**
   * Tests that search for values that are, and aren't, present in the CSV
   *
   * @throws FileNotFoundException if test file is not found in directory
   */
  @Test
  public void testBasicSearch() throws FileNotFoundException {

    String path = "/Users/ianzelbo/Desktop/CS32/server-paparekh-izelbo/data/stars/ten-star.csv";

    Reader reader = new FileReader(path);
    Parser NoColumnParser = new Parser(reader, new Creator());
    Search tenStarSearch = new Search();
    List<String[]> elements = NoColumnParser.getValues();

    String[][] foundPositionsArray = {{"1,1"}};

    // Search without specifying a column -  search across all columns
    tenStarSearch.CSVSearch(elements, "Sol", "");
    assertArrayEquals(tenStarSearch.getFoundPositionsArray(), foundPositionsArray);
    //
    //    reader = new FileReader(path);
    //    Parser NoColumnParser1 = new Parser(reader, new Creator());
    //    List<String[]> elements2 = NoColumnParser1.getValues();
    //
    //    tenStarSearch.CSVSearch(elements2, "Proxima Centauri", "");
    //    assertEquals(tenStarSearch.getOutput(), "Proxima Centauri was found in [6,1]");
    //
    //    // Search for a value not present
    //
    //    reader = new FileReader(path);
    //    Parser notPresentParser = new Parser(reader, new Creator());
    //    List<String[]> elements3 = notPresentParser.getValues();
    //    tenStarSearch.CSVSearch(elements3, "Sun", "");
    //    assertEquals(tenStarSearch.getOutput(), "The word Sun was not found in any row.");
    //
    //    // Searching for empty string across all columns should return empty positions in CSV
    //
    //    reader = new FileReader(path);
    //    Parser emptyParser = new Parser(reader, new Creator());
    //    List<String[]> elements4 = emptyParser.getValues();
    //    tenStarSearch.CSVSearch(elements4, "", "");
    //    assertEquals(tenStarSearch.getOutput(), " was found in [2,1], [3,1], [4,1], [10,1]");
    //
    //    // Searching within a specific column
    //
    //    reader = new FileReader(path);
    //    Parser specificParser = new Parser(reader, new Creator());
    //    List<String[]> elements5 = specificParser.getValues();
    //    tenStarSearch.CSVSearch(elements5, "Sol", "ProperName");
    //    assertEquals(tenStarSearch.getOutput(), "Sol was found in column ProperName at rows
    // [1].");
  }
}

//  /**
//   * Tests that search for values by index, by column name, and without a column identifier
//   *
//   * @throws FileNotFoundException if test file is not found in directory
//   */
//  @Test
//  public void testIdentifierSearch() throws FileNotFoundException {
//    String path =
//
//
// "/Users/ianzelbo/Desktop/CS32/server-paparekh-izelbo/data/census/dol_ri_earnings_disparity.csv";
//    Reader reader = new FileReader(path);
//    Parser parser = new Parser(reader, new Creator());
//    List<String[]> elements = parser.getValues();
//    Search search = new Search();
//
//    // Without specifying a column - search across all columns
//    search.CSVSearch(elements, "White", "");
//    assertEquals("White was found in [1,1]", search.getOutput());
//
//    search = new Search();
//
//    // With a column index
//    search.CSVSearch(elements, "Multiracial", "1"); // Assuming column index for "Data Type"
//
//    assertEquals("Multiracial was found in column 1 at rows [6].", search.getOutput());
//
//    search = new Search();
//
//    // With a column name
//    search.CSVSearch(elements, "6%", "Employed Percent");
//
//    assertEquals("6% was found in column Employed Percent at rows [2].", search.getOutput());
//
//    search = new Search();
//
//    search.CSVSearch(elements, "Multiracial", "0");
//    assertEquals("Word Multiracial not found in column 0.", search.getOutput());
//  }
//
//  @Test
//  public void testErrors() throws FileNotFoundException {
//    // Testing error when a non-existent column is searched
//    String path =
//
// "/Users/ianzelbo/Desktop/CS32/server-paparekh-izelbo/data/malformed/malformed_signs.csv";
//
//    Reader reader = new FileReader(path);
//    Parser parser = new Parser(reader, new Creator());
//    List<String[]> elements = parser.getValues();
//
//    Search search = new Search();
//
//    search.CSVSearch(elements, "Sophie", "NonExistentColumn");
//    assertEquals("Column NonExistentColumn not found.", search.getOutput());
//
//    // Column not found
//
//    search = new Search();
//
//    search.CSVSearch(elements, "Sophie", "NonExistentColumn");
//    assertEquals("Column NonExistentColumn not found.", search.getOutput());
//
//    // Word not found -- given a column
//
//    search = new Search();
//
//    search.CSVSearch(elements, "WordNotExist", "0");
//    assertEquals("Word WordNotExist not found in column 0.", search.getOutput());
//
//    // Word not found -- in any column
//
//    search = new Search();
//
//    search.CSVSearch(elements, "WordNotExist", "");
//    assertEquals("The word WordNotExist was not found in any row.", search.getOutput());
//
//    // Empty CSV
//    String emptyPath =
// "/Users/ianzelbo/Desktop/CS32/server-paparekh-izelbo/data/census/empty.csv";
//    reader = new FileReader(emptyPath);
//    parser = new Parser(reader, new Creator());
//    List<String[]> elements1 = parser.getValues();
//    search = new Search();
//
//    search.CSVSearch(elements1, "Word", "");
//    assertEquals("No data found in CSV", search.getOutput());
//  }
// }
