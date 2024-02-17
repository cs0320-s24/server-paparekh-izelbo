package edu.brown.cs.student.main.csv;

import edu.brown.cs.student.main.interfaces.Creator;
import edu.brown.cs.student.main.search.Parser;
import edu.brown.cs.student.main.search.Search;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/** Command Line Interface - Powers user input and calls to parse and search */
public class CLI {

  public CLI() {

    titleCard();

    promptUser();
  }

  /** Calls necessary prompting methods */
  public static void promptUser() {
    Scanner userPrompt = new Scanner(System.in);

    try {
      String filePath = filePath(userPrompt);
      if (filePath == null) return;

      String wordToFind = searchWord(userPrompt);
      if (wordToFind == null) return;

      String columnToSearch = columnToSearch(userPrompt);
      if (columnToSearch == null) return;

      performSearch(filePath, wordToFind, columnToSearch);
    } catch (Exception e) {
      System.out.println("There was an error: " + e.getMessage());
    }
  }

  /**
   * The method prompts the user to input a file path, validates that the path starts with "data/"
   * and ends with ".csv", and checks if the file exists.
   *
   * @param userPrompt A Scanner object for reading the user input.
   * @return The validated file path as a String
   * @exception IllegalArgumentException if the file path does not start with "data/" or does not
   *     end with ".csv".
   * @exception IOException if an I/O error occurs when opening the file.
   */
  public static String filePath(Scanner userPrompt) {
    while (true) {
      System.out.println(
          "Enter the file path of the CSV file you would like to search (starting with data/): ");
      String path = userPrompt.nextLine();
      if (handleSpecialCommands(path)) {
        return null; // Special command handled
      }
      try {
        if (path.startsWith("data/") && path.toLowerCase().endsWith(".csv")) {
          new FileReader(path).close(); // Just to check if file exists
          return path;
        }
      } catch (IllegalArgumentException | IOException e) {
        logToFile(
            "Invalid file path. Please ensure the path starts with data/ and has a .csv extension.");
        System.out.println(
            "Invalid file path. Please ensure the path starts with data/ and has a .csv extension.");
      }
    }
  }


  /**
   * Prompts the user to enter a word to search for. Continuously prompts the user until a non-empty
   * word is entered or a special command is handled.
   *
   * @param userPrompt A Scanner object for reading the user input.
   * @return The word entered by the user to search for. Returns the word if a non-empty string is
   *     entered or the result of handling a special command.
   */
  public static String searchWord(Scanner userPrompt) {
    while (true) {
      System.out.println("Enter the word you would like to search for: ");
      String wordToFind = userPrompt.nextLine();
      if (handleSpecialCommands(wordToFind) || !wordToFind.equals("")) {
        return wordToFind;
      } else {
        System.out.println("You must enter a word to search for.");
      }
    }
  }

  /**
   * Prompts the user to specify a column to search in. The user can enter a column name to search
   * specifically in that column or press enter to search across all columns.
   *
   * @param userPrompt A Scanner object for reading the user input.
   * @return The name of the column to search in as specified by the user, or null if a special
   *     command is handled. If the user presses enter without typing anything, an empty string is
   *     returned, indicating that the search should consider all columns.
   */
  public static String columnToSearch(Scanner userPrompt) {
    System.out.println(
        "Enter the column you would like to search in, or press enter to search all columns: ");
    String columnToSearch = userPrompt.nextLine();
    if (handleSpecialCommands(columnToSearch)) {
      return null; // Special command handled
    }
    return columnToSearch;
  }

  /**
   * Performs a search for a specified word within a given file. If a column name is provided, the
   * search is limited to that column; otherwise, it searches across all columns. This method
   * initializes the necessary objects for reading the file and performing the search, then executes
   * the search operation.
   *
   * @param filePath The path to the CSV file where the search is to be performed.
   * @param wordToFind The word to search for within the file.
   * @param columnToSearch The name of the column to limit the search to. An empty string indicates
   *     that the search should consider all columns.
   * @exception FileNotFoundException if the specified file cannot be found.
   */
  public static void performSearch(String filePath, String wordToFind, String columnToSearch) {
    System.out.println("Searching...");

    try {
      FileReader file = new FileReader(filePath);
      Creator creator = new Creator(); // Direct instantiation without specifying the interface
      Search search = new Search();
      search.CSVSearch(new Parser<>(file, creator), wordToFind, columnToSearch);
    } catch (FileNotFoundException e) {
      System.out.println("File not found: " + e.getMessage());
    }
  }

  /**
   * Handles special commands entered by the user. Supports "exit" to terminate the application and
   * "restart" to restart the user input prompt process.
   *
   * @param input The user input to be evaluated for special commands.
   * @return true if a special command is recognized and handled, false otherwise.
   */
  private static boolean handleSpecialCommands(String input) {
    if ("exit".equalsIgnoreCase(input)) {
      System.exit(0);
      return true;
    } else if ("restart".equalsIgnoreCase(input)) {
      promptUser(); // Restart the prompt
      return true;
    }
    return false;
  }

  /**
   * Displays the title card for the CSV Search Utility. This method uses ANSI escape codes to
   * format the text output to the console, including bold, underline, and italic styles.
   */
  private void titleCard() {

    // ANSI escape code for text formatting

    final String BOLD = "\033[1m";

    final String RESET_STYLE = "\033[0m";

    final String UNDERLINE = "\033[4m";

    final String ITALIC = "\033[3m";

    System.out.println(BOLD + "CSV Search Utility" + RESET_STYLE + " by Ian Zelbo");

    System.out.println("Assumes " + UNDERLINE + "capitalization does not matter." + RESET_STYLE);
    System.out.println(
        "Type "
            + ITALIC
            + "exit"
            + RESET_STYLE
            + " to quit at any time. "
            + "Type "
            + ITALIC
            + "restart"
            + RESET_STYLE
            + " to restart the prompt.");
  }

  /**
   * Logs a message to a file named "error_log.txt". The log includes the class name and line number
   * from where the log method was called, along with the provided message. This method
   * automatically appends the new log message to the end of the file, creating the file if it does
   * not exist.
   *
   * @param message The message to be logged.
   */
  public static void logToFile(String message) {
    // Get the current stack trace
    StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
    // The 2nd element in the stack trace represents the caller
    StackTraceElement caller = stackTraceElements[2];

    String logMessage =
        String.format(
            "Class: %s, Line: %d, Message: %s",
            caller.getClassName(), caller.getLineNumber(), message);

    try (FileWriter fw = new FileWriter("error_log.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw)) {
      out.println(logMessage);
    } catch (IOException e) {
      System.err.println("Error writing to log file: " + e.getMessage());
    }
  }
}
