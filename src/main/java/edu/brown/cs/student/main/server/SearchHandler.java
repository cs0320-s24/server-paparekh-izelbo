package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.interfaces.Creator;
import edu.brown.cs.student.main.search.Parser;
import edu.brown.cs.student.main.search.Search;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

      //So we don't have to parse more than once

      Parser parser = this.loadHandler.getParser();

      if (parser == null) {
        responseMap.put("result", "You have not loaded your file yet.");
        return responseMap;
      }

      List<String[]> elements = parser.getValues();
      Search search = new Search();
      search.CSVSearch(elements, word, column);
      StringBuilder matches = search.getMatches();

      String JsonSerialized = Serialization.ArrayToJson(matches);

    } catch (Exception e) {
      responseMap.put("result", "error");
    }

    return responseMap;
  }
}










//
//
//
//
//      Set<String> participants = request.queryParams();
//      String filepath = request.queryParams("filepath");
//      filepath = filepath.replaceAll(" ", "").toLowerCase();
//
//      Creator creator = new Creator();
//      FileReader file = new FileReader(filepath);
//      this.parser = new Parser<>(file, creator);
//
//      responseMap.put("result", "success");
//
//    } catch (FileNotFoundException e) {
//      responseMap.put("result", "File not found.");


    }

    return responseMap;

  }
}
