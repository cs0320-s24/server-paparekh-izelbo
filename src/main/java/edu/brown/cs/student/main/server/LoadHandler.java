package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.interfaces.Creator;
import edu.brown.cs.student.main.search.Parser;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/** Handler for loading CSV files and parsing data by implementing the Route interface */
public class LoadHandler implements Route {

  private Parser parser;

  /**
   * Getter method for the Parser object
   *
   * @return the Parser object
   */
  public Parser getParser() {
    if (this.parser == null) {
      System.out.println("Make sure to load your csv before parsing.");
    }
    return parser;
  }

  /**
   * Handles the requests for loading CSV files and parsing the data
   *
   * @param request the request object
   * @param response the response object
   * @return a Map containing result information
   */
  @Override
  public Object handle(Request request, Response response) {
    Map<String, Object> responseMap = new HashMap<>();

    try {

      String filepath = request.queryParams("filepath");
      filepath = filepath.replaceAll(" ", "").toLowerCase();

      Creator creator = new Creator();
      FileReader file = new FileReader(filepath);
      this.parser = new Parser<>(file, creator);
      this.parser.parse();

      responseMap.put("result", "success");

    } catch (FileNotFoundException e) {
      responseMap.put("result", "File not found.");
    }

    return responseMap;
  }
}
