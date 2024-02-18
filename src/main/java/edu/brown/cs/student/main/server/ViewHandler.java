package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.search.Parser;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/** Handler for viewing loaded data that implements the Spark interface for incoming requests */
public class ViewHandler implements Route {

  private final LoadHandler loadHandler;

  /**
   * Constructs a ViewHandler with the input LoadHandler.
   *
   * @param loadHandler the LoadHandler for the data
   */
  public ViewHandler(LoadHandler loadHandler) {
    this.loadHandler = loadHandler;
  }

  /**
   * Handles the requests for viewing the data from a CSV file
   *
   * @param request the request object
   * @param response the response object
   * @return a Map containing the result of view
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    Map<String, Object> responseMap = new HashMap<>();

    try {
      Parser parser = this.loadHandler.getParser();

      if (parser == null) {
        responseMap.put("result", "You have not loaded your file yet.");
        return responseMap;
      }

      List<String[]> elements = parser.getValues();

      String serialized = Serialization.convertToJson(listToArray(elements));

      responseMap.put("result", "success");
      responseMap.put("data", serialized);

    } catch (Exception e) {
      responseMap.put("result", "error");
    }

    return responseMap;
  }

  /**
   * Converts a list of string arrays to a 2D string array.
   *
   * @param list the list of string arrays to convert.
   * @return a 2D string array containing the same data as the input list.
   */
  public String[][] listToArray(List<String[]> list) {
    String[][] array = new String[list.size()][];
    for (int i = 0; i < list.size(); i++) {
      array[i] = list.get(i);
    }
    return array;
  }
}
