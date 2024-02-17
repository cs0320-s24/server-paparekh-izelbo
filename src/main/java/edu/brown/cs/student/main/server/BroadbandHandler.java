package edu.brown.cs.student.main.server;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * The BroadbandHandler class implements the Route and Broadbands interface. It handles all requests
 * concerning broadband data Handles requests related to broadband data
 */
public class BroadbandHandler implements Route, Broadbands {

  // Storing state, cached broadband, and state-number mapping
  private Map<String, String> stateData = new HashMap<>();
  private Cache cache = new Cache(this);
  private final String[][] stateMapping = {
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

  /**
   * Handles HTTP requests related to broadband data by retrieving the state and county ID from the
   * request
   *
   * @param request the request object
   * @param response the response object
   * @return a Map containing request-specific information
   */
  @Override
  public Object handle(Request request, Response response) {
    broadbandInitializer();

    // Creating queries
    Set<String> queryParams = request.queryParams();
    String stateID = request.queryParams("state");
    String countyID = request.queryParams("county");
    stateID = stateID.replaceAll(" ", "").toLowerCase();

    Map<String, Object> responseMap = new HashMap<>();

    // Sending a request to the API
    try {
      String countyJson = this.cache.search(stateID, countyID);

      responseMap.put("result", "success");
      responseMap.put("time retrieved", LocalTime.now());

      String[][] countyData = Serialization.convertToArray(countyJson);

      int numRows = countyData.length;
      int numCols = countyData[0].length;
      String[][] serializedData = new String[numRows - 1][numCols - 2];

      serializedData = dataFormatter(serializedData, countyData);

      String JsonData = Serialization.convertToJson(serializedData);

      responseMap.put("data", JsonData);

      return responseMap;
    } catch (Exception e) {
      e.printStackTrace();

      responseMap.put("result", "Exception (" + e + ") encountered");
    }

    return responseMap;
  }

  /**
   * Formats data retrieved from the API for serialization
   *
   * @param serializedData the array to store the serialized data
   * @param countyData the array containing the retrieved data
   * @return 2D array containing serialized data
   */
  private String[][] dataFormatter(String[][] serializedData, String[][] countyData) {
    for (int i = 0; i < serializedData.length; i++) {
      for (int j = 0; j < serializedData[i].length; j++) {
        serializedData[i][j] = countyData[i + 1][j];
      }
    }
    return serializedData;
  }

  /** Contains the initializer logic for the broadband data. */
  private void broadbandInitializer() {
    this.stateData = new HashMap<>();

    for (String[] item : stateMapping) {
      if (item.length >= 2) {
        item[0] = item[0].replaceAll(" ", "").toLowerCase();
        this.stateData.put(item[0], item[1]);
      }
    }
  }

  /**
   * Sends a request to the Census API for data retrieval
   *
   * @param stateID the state ID
   * @param countyID the county ID
   * @return the response body
   * @throws URISyntaxException if the URI is invalid
   * @throws IOException if an I/O error occurs
   * @throws InterruptedException if interrupted
   */
  @Override
  public String sendRequest(String stateID, String countyID)
      throws URISyntaxException, IOException, InterruptedException {

    System.out.println(stateID);
    HttpRequest censusApiRequest =
        HttpRequest.newBuilder()
            .uri(
                new URI(
                    "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
                        + countyID
                        + "&in=state:"
                        + this.stateData.get(stateID.trim().toLowerCase())))
            .GET()
            .build();

    HttpResponse<String> sentCensusApiResponse =
        HttpClient.newBuilder()
            .build()
            .send(censusApiRequest, HttpResponse.BodyHandlers.ofString());

    return sentCensusApiResponse.body();
  }

  /** A success response to a specific broadband request */
  public record BroadbandSuccessResponse(String responseType, Map<String, Object> responseMap) {

    /**
     * Constructs a BroadbandSuccessResponse with a default success response
     *
     * @param responseMap the response map containing data.
     */
    public BroadbandSuccessResponse(Map<String, Object> responseMap) {
      this("success", responseMap);
    }

    /**
     * Serialization of JSON data by converting county JSON data into a JSON string
     *
     * @param countyJson String containing JSON data
     * @return serialized JSON data
     */
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
      // Error handling
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
