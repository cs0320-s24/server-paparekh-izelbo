package edu.brown.cs.student.main;

import edu.brown.cs.student.main.csv.CsvDataManager;
import edu.brown.cs.student.main.datamodel.StringListCreator;
import edu.brown.cs.student.main.parser.FactoryFailureException;
import edu.brown.cs.student.main.search.SearchOptions;
import edu.brown.cs.student.main.search.SearchResult;
import edu.brown.cs.student.main.search.SearchType;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.apache.commons.cli.*;

/** The Main class of our project. This is where execution begins. */
public final class Main {
  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args);
  }

  /**
   * Constructor for the Main class which performs the command line parsing
   *
   * @param args
   */
  private Main(String[] args) {
    Options options = new Options();

    // Initialize command line options
    options.addOption(
        Option.builder()
            .longOpt("ignore-case")
            .desc("Perform case-insensitive search (Default: case-sensitive)")
            .option("i")
            .build());
    options.addOption(
        Option.builder()
            .longOpt("match-substring")
            .desc("Match substrings (Default: exact-match)")
            .option("s")
            .build());
    options.addOption(
        Option.builder()
            .longOpt("no-headers")
            .desc("File does not contain headers (Default: file contain headers)")
            .option("nh")
            .build());
    options.addOption(Option.builder().longOpt("help").option("h").build());

    String header = "\nFile search utility\n\n Options:\n";
    String footer = "\n";

    HelpFormatter formatter = new HelpFormatter();
    CommandLineParser parser = new DefaultParser();

    try {
      CommandLine cmd = parser.parse(options, args);

      if (cmd.hasOption("help")) {
        formatter.printHelp(
            "main [options] <filename> <text-to-search> [column-name|column-index]",
            header,
            options,
            footer);
        System.exit(0);
      }

      List<String> arguments = cmd.getArgList();
      if (arguments.size() < 2 || arguments.size() > 3) {
        throw new ParseException("Missing required arguments");
      }

      // Configure search options based on command-line options
      SearchOptions searchOptions = new SearchOptions();
      EnumSet<SearchType> searchTypeEnumSet = EnumSet.noneOf(SearchType.class);

      if (cmd.hasOption("ignore-case")) {
        searchTypeEnumSet.add(SearchType.CASE_INSENSITIVE);
      } else {
        searchTypeEnumSet.add(SearchType.CASE_SENSITIVE);
      }

      if (cmd.hasOption("match-substring")) {
        searchTypeEnumSet.add(SearchType.SUB_STRING);
      } else {
        searchTypeEnumSet.add(SearchType.EXACT);
      }

      searchOptions.setTypeEnumSet(searchTypeEnumSet);

      boolean hasHeaders = !cmd.hasOption("no-headers");

      String fileName = arguments.get(0);
      String textToSearch = arguments.get(1);

      if (arguments.size() == 3) {
        searchOptions.setColumn(arguments.get(2));
      }

      CsvDataManager<ArrayList<String>> dataParser = new CsvDataManager<>(new StringListCreator());
      InputStream is = getFileAsIOStream(fileName);
      dataParser.processData(new InputStreamReader(is), hasHeaders);

      System.out.println(
          String.format(
              "Arguments - FileName: %s, SearchString: '%s', column: %s",
              fileName, textToSearch, searchOptions.getColumn()));

      SearchResult searchResult = dataParser.search(textToSearch, searchOptions);
      searchResult.printResults();

    } catch (ParseException e) {
      // Handle ParseException case
      System.err.println(e.getMessage());

      formatter.printHelp(
          "main [options] <filename> <text-to-search> [column-name|column-index]",
          header,
          options,
          footer);

      System.exit(1);
    } catch (IOException | FactoryFailureException e) {
      // Handle IOException case
      System.err.println(e.getMessage());
      System.err.println(
          "Exception thrown due to "
              + "invalid file/path (must start with "
              + "\"data/\" and end with \".csv\")");
      formatter.printHelp(
          "main [options] <filename> <text-to-search> [column-name|column-index]",
          header,
          options,
          footer);
      System.exit(1);
    }
  }

  /**
   * Utility function to get the file as an IO Stream to ensure security of the CSV files
   *
   * @param fileName String file name
   * @return InputStream
   */
  private InputStream getFileAsIOStream(final String fileName) {
    InputStream ioStream = this.getClass().getClassLoader().getResourceAsStream(fileName);

    if (ioStream == null) {
      throw new IllegalArgumentException(fileName + " is not found");
    }
    return ioStream;
  }
}
