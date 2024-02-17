package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.search.Parser;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class ViewHandler implements Route {

  private final LoadHandler loadHandler;

  public ViewHandler(LoadHandler loadHandler) {
    this.loadHandler = loadHandler;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    Map<String, Object> responseMap = new HashMap<>();

    try {

      // So we don't have to parse more than once

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

    // success?
    return responseMap;
  }

  public String[][] listToArray(List<String[]> list) {
    String[][] array = new String[list.size()][];
    for (int i = 0; i < list.size(); i++) {
      array[i] = list.get(i);
    }
    return array;
  }
}
