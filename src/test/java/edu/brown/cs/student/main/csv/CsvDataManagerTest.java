package edu.brown.cs.student.main.csv;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import edu.brown.cs.student.main.parser.CreatorFromRow;
import edu.brown.cs.student.main.parser.FactoryFailureException;
import edu.brown.cs.student.main.search.SearchOptions;
import edu.brown.cs.student.main.search.SearchResult;
import edu.brown.cs.student.main.search.SearchType;
import java.io.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for CsvDataManager */
class CsvDataManagerTest {

  private CsvDataManager<ArrayList<String>> dataManager;

  private String csvDataNoHeader =
      "Jason Tatum,Boston Celtics,Boston,25\n"
          + "Lebron James,LA Lakers,LA,39\n"
          + "Jason Preston,Utah Jazz,Utah,24\n"
          + "Steph Curry,Golden State Warriors,San "
          + "Francisco,35\n";

  private String csvDataWithHeader =
      "Name,Team,Location,Age\n"
          + "Jason Tatum,Boston Celtics,Boston, "
          + "25\n"
          + "Lebron James,LA Lakers,LA,39\n"
          + "Jason Preston,Utah Jazz,Utah,24\n"
          + "Steph Curry,Golden State Warriors,San"
          + " Francisco,35\n";

  private StringReader stringReaderNoHeader = new StringReader(csvDataNoHeader);
  private StringReader stringReaderWithHeader = new StringReader(csvDataWithHeader);

  private String fileNoHeader = "data/testNoHeader.csv";
  private String fileWithHeader = "data/testWithHeader.csv";
  private String fileWithInvalidFileName = "data/doesNotExist.csv";
  private String fileWithInvalidDirectory = "/home/data/doesNotExist.csv";

  private InputStream getFileAsIOStream(final String fileName) throws IllegalArgumentException {
    InputStream ioStream = this.getClass().getClassLoader().getResourceAsStream(fileName);

    if (ioStream == null) {
      throw new IllegalArgumentException(fileName + " is not found");
    }
    return ioStream;
  }

  /** Method called before each test */
  @BeforeEach
  void setUp() {
    // Create a CsvDataManager instance
    dataManager =
        new CsvDataManager<>(
            new CreatorFromRow<>() {
              @Override
              public ArrayList<String> create(java.util.List<String> row) {
                return (ArrayList<String>) row;
              }
            });
  }

  /** Method called after each test */
  @AfterEach
  void tearDown() {
    // Clean up resources
    dataManager = null;
  }

  /** Tests invalid directory */
  @Test
  void testInvalidDirectory() {

    // Process the CSV data
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              Reader reader = new InputStreamReader(getFileAsIOStream(fileWithInvalidDirectory));
              dataManager.processData(reader, false);
            });
  }

  /** Tests invalid file reader search with case sensitive and no header */
  @Test
  void testInvalidFileReaderSearchWithCaseSensitiveExactNoHeader() {
    // Process the CSV data
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              Reader reader = new InputStreamReader(getFileAsIOStream(fileWithInvalidFileName));
              dataManager.processData(reader, false);
            });
  }

  /**
   * Tests file reader search with case sensitive and no header
   *
   * @throws IOException
   */
  @Test
  void testFileReaderSearchWithCaseSensitiveExactNoHeader() throws IOException {
    // Create a sample CSV reader
    Reader reader = new InputStreamReader(getFileAsIOStream(fileNoHeader));

    // Process the CSV data
    try {
      dataManager.processData(reader, false);
    } catch (FactoryFailureException e) {
      fail("Exception not expected", e);
    }

    // Search options
    SearchOptions searchOptions = new SearchOptions();
    searchOptions.setTypeEnumSet(EnumSet.of(SearchType.CASE_SENSITIVE, SearchType.EXACT));

    // Perform search
    SearchResult searchResult = dataManager.search("Jason Tatum", searchOptions);

    // Validate search result
    assertEquals(1, searchResult.getTotalFound(), "Total found should be 1");

    // Retrieve search results
    HashMap<Integer, ArrayList<Integer>> results = searchResult.getResults();

    // Validate search results
    assertEquals(1, results.size(), "Number of results should be 1");
    assertEquals(0, (int) results.get(1).get(0), "Result should be found in column 0 (Name)");
  }

  /**
   * Tests file reader search with case sensitive (exact)
   *
   * @throws IOException
   */
  @Test
  void testFileReaderSearchWithCaseSensitiveExact() throws IOException {
    // Create a sample CSV reader
    Reader reader = new InputStreamReader(getFileAsIOStream(fileWithHeader));
    ;

    // Process the CSV data
    try {
      dataManager.processData(reader, true);
    } catch (FactoryFailureException e) {
      fail("Exception not expected", e);
    }

    // Search options
    SearchOptions searchOptions = new SearchOptions();
    searchOptions.setColumn("Name");
    searchOptions.setTypeEnumSet(EnumSet.of(SearchType.CASE_SENSITIVE, SearchType.EXACT));

    // Perform search
    SearchResult searchResult = dataManager.search("Jason Tatum", searchOptions);

    // Validate search result
    assertEquals(1, searchResult.getTotalFound(), "Total found should be 1");

    // Retrieve search results
    HashMap<Integer, ArrayList<Integer>> results = searchResult.getResults();

    // Validate search results
    assertEquals(1, results.size(), "Number of results should be 1");
    assertEquals(0, (int) results.get(1).get(0), "Result should be found in column 0 (Name)");
  }

  /**
   * Tests search with case sensitive (exact) no header
   *
   * @throws IOException
   */
  @Test
  void testSearchWithCaseSensitiveExactNoHeader() throws IOException {
    // Create a sample CSV reader
    Reader reader = stringReaderNoHeader;

    // Process the CSV data
    try {
      dataManager.processData(reader, false);
    } catch (FactoryFailureException e) {
      fail("Exception not expected", e);
    }

    // Search options
    SearchOptions searchOptions = new SearchOptions();
    searchOptions.setTypeEnumSet(EnumSet.of(SearchType.CASE_SENSITIVE, SearchType.EXACT));

    // Perform search
    SearchResult searchResult = dataManager.search("Jason Tatum", searchOptions);

    // Validate search result
    assertEquals(1, searchResult.getTotalFound(), "Total found should be 1");

    // Retrieve search results
    HashMap<Integer, ArrayList<Integer>> results = searchResult.getResults();

    // Validate search results
    assertEquals(1, results.size(), "Number of results should be 1");
    assertEquals(0, (int) results.get(1).get(0), "Result should be found in column 0 (Name)");
  }

  /**
   * Tests search with case sensitive (exact)
   *
   * @throws IOException
   */
  @Test
  void testSearchWithCaseSensitiveExact() throws IOException {
    // Create a sample CSV reader
    Reader reader = stringReaderWithHeader;

    // Process the CSV data
    try {
      dataManager.processData(reader, true);
    } catch (FactoryFailureException e) {
      fail("Exception not expected", e);
    }

    // Search options
    SearchOptions searchOptions = new SearchOptions();
    searchOptions.setColumn("Name");
    searchOptions.setTypeEnumSet(EnumSet.of(SearchType.CASE_SENSITIVE, SearchType.EXACT));

    // Perform search
    SearchResult searchResult = dataManager.search("Jason Tatum", searchOptions);

    // Validate search result
    assertEquals(1, searchResult.getTotalFound(), "Total found should be 1");

    // Retrieve search results
    HashMap<Integer, ArrayList<Integer>> results = searchResult.getResults();

    // Validate search results
    assertEquals(1, results.size(), "Number of results should be 1");
    assertEquals(0, (int) results.get(1).get(0), "Result should be found in column 0 (Name)");
  }

  /**
   * Tests search with case sensitive exact with column index
   *
   * @throws IOException
   */
  @Test
  void testSearchWithCaseSensitiveExactWithColumnIndex() throws IOException {
    // Create a sample CSV reader
    Reader reader = stringReaderWithHeader;

    // Process the CSV data
    try {
      dataManager.processData(reader, true);
    } catch (FactoryFailureException e) {
      fail("Exception not expected", e);
    }

    // Search options
    SearchOptions searchOptions = new SearchOptions();
    searchOptions.setColumn("2");
    searchOptions.setTypeEnumSet(EnumSet.of(SearchType.CASE_SENSITIVE, SearchType.EXACT));

    // Perform search
    SearchResult searchResult = dataManager.search("Utah", searchOptions);

    // Validate search result
    assertEquals(1, searchResult.getTotalFound(), "Total found should be 1");

    // Retrieve search results
    HashMap<Integer, ArrayList<Integer>> results = searchResult.getResults();

    // Validate search results
    assertEquals(1, results.size(), "Number of results should be 1");
    assertEquals(
        2, (int) results.get(3).get(0), "Result should be found in row 3 column 2 (Location)");
  }

  /**
   * Tests search with case in-sensitive (exact)
   *
   * @throws IOException
   */
  @Test
  void testSearchWithCaseInSensitiveExact() throws IOException {
    // Create a sample CSV reader
    Reader reader = stringReaderWithHeader;

    // Process the CSV data
    try {
      dataManager.processData(reader, true);
    } catch (FactoryFailureException e) {
      fail("Exception not expected", e);
    }

    // Search options
    SearchOptions searchOptions = new SearchOptions();
    searchOptions.setColumn("Name");
    searchOptions.setTypeEnumSet(EnumSet.of(SearchType.CASE_INSENSITIVE, SearchType.EXACT));

    // Perform search
    SearchResult searchResult = dataManager.search("steph curry", searchOptions);

    // Validate search result
    assertEquals(1, searchResult.getTotalFound(), "Total found should be 1");

    // Retrieve search results
    HashMap<Integer, ArrayList<Integer>> results = searchResult.getResults();

    // Validate search results
    assertEquals(1, results.size(), "Number of results should be 1");
    assertEquals(0, (int) results.get(4).get(0), "Result should be found in row 4 column 0 (Name)");
  }

  /**
   * Tests search with case sensitive and sub-string
   *
   * @throws IOException
   */
  @Test
  void testSearchWithCaseSensitiveSubString() throws IOException {
    // Create a sample CSV reader
    Reader reader = stringReaderWithHeader;

    // Process the CSV data
    try {
      dataManager.processData(reader, true);
    } catch (FactoryFailureException e) {
      fail("Exception not expected", e);
    }

    // Search options
    SearchOptions searchOptions = new SearchOptions();
    searchOptions.setColumn("Name");
    searchOptions.setTypeEnumSet(EnumSet.of(SearchType.CASE_SENSITIVE, SearchType.SUB_STRING));

    // Perform search
    SearchResult searchResult = dataManager.search("Jason", searchOptions);

    // Validate search result
    assertEquals(2, searchResult.getTotalFound(), "Total found should be 2");

    // Retrieve search results
    HashMap<Integer, ArrayList<Integer>> results = searchResult.getResults();

    // Validate search results
    assertEquals(2, results.size(), "Number of results should be 2");
    assertEquals(0, (int) results.get(1).get(0), "Result should be found in row 1 column 0 (Name)");
    assertEquals(0, (int) results.get(3).get(0), "Result should be found in row 3 column 0 (Name)");
  }

  /**
   * Tests search with case in-sensitive and sub-string
   *
   * @throws IOException
   */
  @Test
  void testSearchWithCaseInSensitiveSubString() throws IOException {
    // Create a sample CSV reader
    Reader reader = stringReaderWithHeader;

    // Process the CSV data
    try {
      dataManager.processData(reader, true);
    } catch (FactoryFailureException e) {
      fail("Exception not expected", e);
    }

    // Search options
    SearchOptions searchOptions = new SearchOptions();
    searchOptions.setColumn("Name");
    searchOptions.setTypeEnumSet(EnumSet.of(SearchType.CASE_INSENSITIVE, SearchType.SUB_STRING));

    // Perform search
    SearchResult searchResult = dataManager.search("jason", searchOptions);

    // Validate search result
    assertEquals(2, searchResult.getTotalFound(), "Total found should be 2");

    // Retrieve search results
    HashMap<Integer, ArrayList<Integer>> results = searchResult.getResults();

    // Validate search results
    assertEquals(2, results.size(), "Number of results should be 2");
    assertEquals(0, (int) results.get(1).get(0), "Result should be found in row 1 column 0 (Name)");
    assertEquals(0, (int) results.get(3).get(0), "Result should be found in row 3 column 0 (Name)");
  }

  /**
   * Tests search with invalid CSV
   *
   * @throws IOException
   */
  @Test
  void testSearchWithBadCsv() {
    // Sample CSV data
    String csvData =
        "Jason Tatum, Boston Celtics, Boston, 25\n"
            + "Lebron James, LA Lakers, LA, 39\n"
            + "Jason Preston, Utah Jazz\n"
            + "Steph Curry, Golden State Warriors, San Francisco, 35\n";

    // Create a sample CSV reader
    Reader reader = new StringReader(csvData);

    // Process the CSV data
    Exception exception =
        assertThrows(
            FactoryFailureException.class,
            () -> {
              dataManager.processData(reader, false);
            });
  }
}
