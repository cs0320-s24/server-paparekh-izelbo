package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.interfaces.Creator;
import edu.brown.cs.student.main.search.Parser;
import edu.brown.cs.student.main.search.Search;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoadHandler implements Route {

  private Parser parser;


  public Parser getParser() {
    if (this.parser == null) {
      System.out.println("Make sure to load your csv before parsing.");
    }
    return parser;
  }


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
