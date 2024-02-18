package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
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
  private String[][] stateMapping;

  public BroadbandHandler() {
    populateStateData();
  }

  public void populateStateData() {
    try {
      // Create the HTTP request
      HttpRequest stateAPIRequest =
          HttpRequest.newBuilder()
              .uri(new URI("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*"))
              .GET()
              .build();

      // Send the request
      HttpClient client = HttpClient.newHttpClient();
      HttpResponse<String> response =
          client.send(stateAPIRequest, HttpResponse.BodyHandlers.ofString());

      // Use Moshi to parse the JSON response
      Moshi moshi = new Moshi.Builder().build();
      Type type = Types.newParameterizedType(List.class, List.class);
      JsonAdapter<List<List<String>>> jsonAdapter = moshi.adapter(type);

      List<List<String>> states = jsonAdapter.fromJson(response.body());

      if (states != null) {
        this.stateMapping =
            new String[states.size() - 1][2]; // -1 to skip header, and 2 for state name and code

        for (int i = 1; i < states.size(); i++) {
          List<String> stateInfo = states.get(i);
          this.stateMapping[i - 1][0] = stateInfo.get(0); // State name
          this.stateMapping[i - 1][1] = stateInfo.get(1); // State code
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

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

    return new BroadbandFailureResponse();
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

    Map getMap() {
      if (responseMap != null) {
        return responseMap;
      }
      return null;
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

  /**
   * Failure response for Broadband Hanlder
   *
   * @param response_type String representing the response type
   */
  public record BroadbandFailureResponse(String response_type) {

    public BroadbandFailureResponse() {
      this("failure with response type");
    }
  }
}
