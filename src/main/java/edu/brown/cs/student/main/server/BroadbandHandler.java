package edu.brown.cs.student.main.server;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;
import java.util.Set;



public class BroadbandHandler implements Route, Broadbands{


  // Storing state, cached broadband, and state-number mapping
  private Map<String, String> stateData = new HashMap<>();
  // TODO: Implement caching functionality
  private Cache cachedBroadband;
  private String[][] stateMapping = {
      {"Alabama", "01"},
      {"Alaska", "02"},
      {"Arizona", "04"},
      {"Arkansas", "05"},
      {"California", "06"},
      {"Louisiana", "22"},
      {"Kentucky", "21"},
      {"Colorado", "08"},
      {"Connecticut", "09"},
      {"Delaware", "10"},
      {"District of Columbia", "11"},
      {"Florida", "12"},
      {"Georgia", "13"},
      {"Hawaii", "15"},
      {"Idaho", "16"},
      {"Illinois", "17"},
      {"Indiana", "18"},
      {"Iowa", "19"},
      {"Kansas", "20"},
      {"Maine", "23"},
      {"Maryland", "24"},
      {"Massachusetts", "25"},
      {"Michigan", "26"},
      {"Minnesota", "27"},
      {"Mississippi", "28"},
      {"Missouri", "29"},
      {"Montana", "30"},
      {"Nebraska", "31"},
      {"Nevada", "32"},
      {"New Hampshire", "33"},
      {"New Jersey", "34"},
      {"New Mexico", "35"},
      {"New York", "36"},
      {"North Carolina", "37"},
      {"North Dakota", "38"},
      {"Ohio", "39"},
      {"Oklahoma", "40"},
      {"Oregon", "41"},
      {"Pennsylvania", "42"},
      {"Rhode Island", "44"},
      {"South Carolina", "45"},
      {"South Dakota", "46"},
      {"Tennessee", "47"},
      {"Texas", "48"},
      {"Utah", "49"},
      {"Vermont", "50"},
      {"Virginia", "51"},
      {"Washington", "53"},
      {"West Virginia", "54"},
      {"Wisconsin", "55"},
      {"Wyoming", "56"},
      {"Puerto Rico", "72"}
  };

  @Override
  public Object handle(Request request, Response response) {
    broadbandInitializer();

    Set<String> params = request.queryParams();
    String StateID = request.queryParams("state");
    String countyID = request.queryParams("county");
    StateID = StateID.replaceAll(" ", "").toLowerCase();

    //    System.out.println(StateID);

    // variables ? City = SanFran
    //    for (int i = 0; i < params.size(); i++) {
    //      System.out.println("Parameter (" + i + "): " + params.toArray()[i].toString());
    //    }

    // Creates a hashmap to store the results of the request
    Map<String, Object> responseMap = new HashMap<>();

    try {
      // Sends a request to the API and receives JSON back
      String countyJson = this.cache.search(StateID, countyID);

      // Adds results to the responseMap
      responseMap.put("result", "success");
      responseMap.put("time retrieved", LocalTime.now());

      String[][] CountyData = SerializeUtility.JsonToArray(countyJson);

      int x = CountyData.length;
      int y = CountyData[0].length;
      String[][] SerializedData = new String[x - 1][y - 2];

      // formats output data
      for (int i = 0; i < SerializedData.length; i++) {
        for (int j = 0; j < SerializedData[i].length; j++) {
          SerializedData[i][j] = CountyData[i + 1][j];

        }
      }

      String JsonData = SerializeUtility.ArrayToJson(SerializedData);

      responseMap.put("data", JsonData);

      return responseMap;
      // return new SuccessResponse(responseMap).serialize(countyJson);
    } catch (Exception e) {
      e.printStackTrace();

      // This is a relatively unhelpful exception message. An important part of this sprint will be
      // in learning to debug correctly by creating your own informative error messages where Spark
      // falls short.
      responseMap.put("result", "Exception");
    }

    return responseMap;
  }

  private void broadbandInitializer() {
    this.stateData = new HashMap<>();

    for (String[] item : stateMapping) {
      if (item.length >= 2) {
        item[0] = item[0].replaceAll(" ", "").toLowerCase();
        this.stateData.put(item[0], item[1]);
      }
    }
  }

  @Override
  public String sendRequest(String stateID, String countyID)
      throws URISyntaxException, IOException, InterruptedException {
    return null;
  }

  public record BroadbandSuccessResponse(String responseType, Map<String, Object> responseMap) {

    public BroadbandSuccessResponse(Map<String, Object> responseMap) {
      this("success", responseMap);
    }

    String serialize(String countyJson) {

      String[][] counties = Serialization.convertToArray(countyJson);

      int columnLength = counties.length;
      int rowLength = counties[0].length;
      String[][] SerializedData = new String[columnLength - 1][rowLength - 2];

      for (int r = 0; r < SerializedData.length; r++) {
        for (int c = 0; c < SerializedData[c].length; c++) {
          SerializedData[c][c] = counties[c + 1][c];
        }
      }

      String jsonData;
      try {
        jsonData = Serialization.convertToJson(SerializedData);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      responseMap.put("data", jsonData);

      return jsonData;
    }
  }
}
