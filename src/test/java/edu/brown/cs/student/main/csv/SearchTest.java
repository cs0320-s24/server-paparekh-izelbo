package edu.brown.cs.student.main.csv;

import static org.testng.AssertJUnit.assertEquals;

import edu.brown.cs.student.main.interfaces.Creator;
import edu.brown.cs.student.main.search.Parser;
import edu.brown.cs.student.main.search.Search;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Arrays;
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

    String path = "/Users/ianzelbo/server-paparekh-izelbo/data/stars/ten-star.csv";

    Reader reader = new FileReader(path);
    Parser NoColumnParser = new Parser(reader, new Creator());
    NoColumnParser.parse();
    Search tenStarSearch = new Search();
    List elements = NoColumnParser.getValues();
    // List elements = NoColumnParser.getValues();

    String[][] foundPositionsArray = {{"1, 1"}};

    // Search without specifying a column -  search across all columns
    tenStarSearch.CSVSearch(elements, "Sol", "");
    assertEquals(
        Arrays.deepToString(tenStarSearch.getFoundPositionsArray()),
        Arrays.deepToString(foundPositionsArray));

    reader = new FileReader(path);
    Parser NoColumnParser1 = new Parser(reader, new Creator());
    NoColumnParser1.parse();
    List elements2 = NoColumnParser1.getValues();
    String[][] foundPositionsArray1 = {{"6, 1"}};
    tenStarSearch.CSVSearch(elements2, "Proxima Centauri", "");
    assertEquals(
        Arrays.deepToString(tenStarSearch.getFoundPositionsArray()),
        Arrays.deepToString(foundPositionsArray1));

    // Search for a value not present

    reader = new FileReader(path);
    Parser notPresentParser = new Parser(reader, new Creator());
    notPresentParser.parse();
    List elements3 = notPresentParser.getValues();
    String[][] foundPositionsArray2 = {};
    tenStarSearch.CSVSearch(elements3, "Sun", "");
    assertEquals(
        Arrays.deepToString(tenStarSearch.getFoundPositionsArray()),
        Arrays.deepToString(foundPositionsArray2));

    // Searching for empty string across all columns should return empty positions in CSV

    reader = new FileReader(path);
    Parser emptyParser = new Parser(reader, new Creator());
    emptyParser.parse();
    List elements4 = emptyParser.getValues();
    String[][] foundPositionsArray3 = {{"2, 1"}, {"3, 1"}, {"4, 1"}, {"10, 1"}};
    tenStarSearch.CSVSearch(elements4, "", "");
    assertEquals(
        Arrays.deepToString(tenStarSearch.getFoundPositionsArray()),
        Arrays.deepToString(foundPositionsArray3));

    // Searching within a specific column

    reader = new FileReader(path);
    Parser specificParser = new Parser(reader, new Creator());
    specificParser.parse();
    List elements5 = specificParser.getValues();
    String[][] foundPositionsArray4 = {{"1, 1"}};
    tenStarSearch.CSVSearch(elements5, "Sol", "ProperName");
    assertEquals(
        Arrays.deepToString(tenStarSearch.getFoundPositionsArray()),
        Arrays.deepToString(foundPositionsArray4));
  }

  /**
   * Tests that search for values by index, by column name, and without a column identifier
   *
   * @throws FileNotFoundException if test file is not found in directory
   */
  @Test
  public void testIdentifierSearch() throws FileNotFoundException {
    String path =
        "/Users/ianzelbo/server-paparekh-izelbo/data/census/dol_ri_earnings_disparity.csv";
    Reader reader = new FileReader(path);
    Parser parser = new Parser(reader, new Creator());
    parser.parse();
    List elements = parser.getValues();
    Search search = new Search();
    String[][] foundPositionsArray = {{"1, 1"}};

    // Without specifying a column - search across all columns
    search.CSVSearch(elements, "White", "");
    assertEquals(
        Arrays.deepToString(search.getFoundPositionsArray()),
        Arrays.deepToString(foundPositionsArray));

    search = new Search();

    // With a column index
    search.CSVSearch(elements, "Multiracial", "1");
    String[][] foundPositionsArray1 = {{"6, 1"}};

    assertEquals(
        Arrays.deepToString(search.getFoundPositionsArray()),
        Arrays.deepToString(foundPositionsArray1));

    search = new Search();

    // With a column name
    search.CSVSearch(elements, "6%", "Employed Percent");

    String[][] foundPositionsArray2 = {{"2, 5"}};

    assertEquals(
        Arrays.deepToString(search.getFoundPositionsArray()),
        Arrays.deepToString(foundPositionsArray2));

    search = new Search();

    search.CSVSearch(elements, "Multiracial", "0");

    String[][] foundPositionsArray4 = {};

    assertEquals(
        Arrays.deepToString(search.getFoundPositionsArray()),
        Arrays.deepToString(foundPositionsArray4));
  }

  @Test
  public void testErrors() throws FileNotFoundException {
    // Testing error when a non-existent column is searched
    String path = "/Users/ianzelbo/server-paparekh-izelbo/data/malformed/malformed_signs.csv";

    Reader reader = new FileReader(path);
    Parser parser = new Parser(reader, new Creator());
    List elements = parser.getValues();

    Search search = new Search();

    search.CSVSearch(elements, "Sophie", "NonExistentColumn");
    String[][] foundPositionsArray = {};

    assertEquals(
        Arrays.deepToString(search.getFoundPositionsArray()),
        Arrays.deepToString(foundPositionsArray));

    // Column not found

    search = new Search();

    search.CSVSearch(elements, "Sophie", "NonExistentColumn");
    assertEquals(
        Arrays.deepToString(search.getFoundPositionsArray()),
        Arrays.deepToString(foundPositionsArray));

    // Word not found -- given a column

    search = new Search();

    search.CSVSearch(elements, "WordNotExist", "0");
    assertEquals(
        Arrays.deepToString(search.getFoundPositionsArray()),
        Arrays.deepToString(foundPositionsArray));

    // Word not found -- in any column

    search = new Search();

    search.CSVSearch(elements, "WordNotExist", "");
    assertEquals(
        Arrays.deepToString(search.getFoundPositionsArray()),
        Arrays.deepToString(foundPositionsArray));

    // Empty CSV
    String emptyPath = "/Users/ianzelbo/server-paparekh-izelbo/data/census/empty.csv";
    reader = new FileReader(emptyPath);
    parser = new Parser(reader, new Creator());
    List elements1 = parser.getValues();
    search = new Search();

    search.CSVSearch(elements1, "Word", "");
    String[][] noFoundArray = {};

    assertEquals(
        Arrays.deepToString(search.getFoundPositionsArray()), Arrays.deepToString(noFoundArray));
  }
}
