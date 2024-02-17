package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.search.Parser;
import edu.brown.cs.student.main.search.Search;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class SearchHandler implements Route {

  private LoadHandler loadHandler;

  public SearchHandler(LoadHandler loadHandler) {
    this.loadHandler = loadHandler;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    Map<String, Object> responseMap = new HashMap<>();

    try {

      String word = request.queryParams("word");
      String column = request.queryParams("column");

      // So we don't have to parse more than once

      Parser parser = this.loadHandler.getParser();

      if (parser == null) {
        responseMap.put("result", "You have not loaded your file yet.");
        return responseMap;
      }

      List<String[]> elements = parser.getValues();
      Search search = new Search();
      search.CSVSearch(elements, word, column);
      String[][] matches = search.getFoundPositionsArray();

      String serialized = Serialization.convertToJson(matches);

      responseMap.put("result", "success");
      responseMap.put("data", serialized);

    } catch (Exception e) {
      responseMap.put("result", "error");
    }

    // success?
    return responseMap;
  }
}
//  /** Success response after completing search */
//  public record SearchSuccessResponse(String responseType, Map<String, Object> responseMap) {
//
//    /**
//     * Constructs a SearchSuccessResponse with a default success response type.
//     *
//     * @param responseMap the response map
//     */
//    public SearchSuccessResponse(Map<String, Object> responseMap) {
//      this("success", responseMap);
//    }
//
//    /**
//     * Serializes the county JSON data
//     *
//     * @param countyJson the JSON data
//     * @return the output serialized JSON data
//     */
//    String serialize(String countyJson) {
//
//      String[][] counties = Serialization.convertToArray(countyJson);
//
//      int columnLength = counties.length;
//      int rowLength = counties[0].length;
//      String[][] SerializedData = new String[columnLength - 1][rowLength - 2];
//
//      for (int r = 0; r < SerializedData.length; r++) {
//        for (int c = 0; c < SerializedData[c].length; c++) {
//          SerializedData[c][c] = counties[c + 1][c];
//        }
//      }
//
//      String jsonData;
//      try {
//        jsonData = Serialization.convertToJson(SerializedData);
//      } catch (IOException e) {
//        throw new RuntimeException(e);
//      }
//      responseMap.put("data", jsonData);
//
//      return jsonData;
//    }
//  }
