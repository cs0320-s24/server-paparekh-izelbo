package edu.brown.cs.student.main.csv;

import edu.brown.cs.student.main.datamodel.Header;
import edu.brown.cs.student.main.parser.CreatorFromRow;
import edu.brown.cs.student.main.parser.DataParser;
import edu.brown.cs.student.main.parser.FactoryFailureException;
import edu.brown.cs.student.main.search.SearchOptions;
import edu.brown.cs.student.main.search.SearchResult;
import edu.brown.cs.student.main.search.SearchType;
import edu.brown.cs.student.main.search.Searcher;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The CsvDataManager class that implements DataParser for the CSV data and Searcher for searching
 * within the parsed data. It parses the CSV rows and allows searching for specific text/Strings in
 * the parsed data
 *
 * @param <T> Generic T object
 */
public class CsvDataManager<T> implements DataParser<Reader>, Searcher {
  private CreatorFromRow<T> creator;
  private ArrayList<T> data = new ArrayList<>();
  private Header header = new Header();

  /**
   * Constructor for CsvDataManager class which initializes instance variables
   *
   * @param creator CreatorFromRow<T> object
   */
  public CsvDataManager(CreatorFromRow<T> creator) {
    this.creator = creator;
  }

  /**
   * Processes data from a given Reader, parsing CSV rows and creating objects based on the
   * specified type T
   *
   * @param reader Reader input data source
   * @param hasHeaders boolean for whether headers are present
   * @throws IOException when I/O error occurs from Reader
   */
  @Override
  public void processData(Reader reader, boolean hasHeaders)
      throws IOException, FactoryFailureException {
    String line;
    BufferedReader bufferedReader = new BufferedReader(reader);
    int columnCount = 0;

    if (hasHeaders) {
      this.header.initialize(
          new ArrayList<>(
              Arrays.asList(
                  bufferedReader.readLine().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1))));
      columnCount = this.header.getSize();
    }

    while ((line = bufferedReader.readLine()) != null) {
      List<String> row = Arrays.asList(line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1));
      // Initialize the columnCount if there are no headers with the
      // first row
      if (columnCount == 0) {
        columnCount = row.size();
      }

      T rowData = creator.create(new ArrayList<>(row));

      // Check for valid columns
      if (row.size() != columnCount) {
        throw new FactoryFailureException("Invalid number of columns", row);
      }

      data.add(rowData);
    }
  }

  /**
   * Performs a search on the data for a word based on the search options
   *
   * @param textToSearch String text to search for
   * @param searchOptions Specific search options
   * @return SearchResult object containing the row and column indices
   * @throws IllegalArgumentException if invalid column name/index
   */
  @Override
  public SearchResult search(String textToSearch, SearchOptions searchOptions)
      throws IllegalArgumentException {
    System.out.println(
        String.format(
            "Searching for textToSearch: '%s', searchOptions: %s", textToSearch, searchOptions));

    SearchResult searchResult = new SearchResult();

    List<Integer> columns = new ArrayList<>();
    if (searchOptions.getColumn() != null) {
      Integer columnIndex =
          this.header == null ? -1 : this.header.getColumnIndex(searchOptions.getColumn());
      if (columnIndex != -1) {
        columns.add(columnIndex);
      } else {
        throw new IllegalArgumentException("Invalid column name/index");
      }
    } else {
      // Search for all the columns
      int size = 0;
      if (this.header.getSize() == 0) {
        size = ((ArrayList) this.data.get(0)).size();
      } else {
        size = this.header.getSize();
      }
      for (int i = 0; i < size; i++) {
        columns.add(i);
      }
    }

    int rowIndex = 0;
    for (T rowData : data) {
      rowIndex++;

      for (Integer columnIndex : columns) {
        if (searchOptions.isType(SearchType.CASE_SENSITIVE)
            && searchOptions.isType(SearchType.EXACT)) {
          if (((ArrayList<?>) rowData).get(columnIndex).toString().compareTo(textToSearch) == 0) {
            searchResult.addResult(rowIndex, columnIndex);
          }
        } else if (searchOptions.isType(SearchType.CASE_INSENSITIVE)
            && searchOptions.isType(SearchType.EXACT)) {
          if (((ArrayList<?>) rowData).get(columnIndex).toString().compareToIgnoreCase(textToSearch)
              == 0) {
            searchResult.addResult(rowIndex, columnIndex);
          }
        } else if (searchOptions.isType(SearchType.CASE_SENSITIVE)
            && searchOptions.isType(SearchType.SUB_STRING)) {
          if (((ArrayList<?>) rowData).get(columnIndex).toString().contains(textToSearch)) {
            searchResult.addResult(rowIndex, columnIndex);
          }
        } else if (searchOptions.isType(SearchType.CASE_INSENSITIVE)
            && searchOptions.isType(SearchType.SUB_STRING)) {
          if (((ArrayList<?>) rowData)
              .get(columnIndex)
              .toString()
              .toLowerCase()
              .contains(textToSearch.toLowerCase())) {
            searchResult.addResult(rowIndex, columnIndex);
          }
        }
      }
    }

    return searchResult;
  }
}
