// package edu.brown.cs.student.main.server;
//
// import java.util.logging.Level;
// import java.util.logging.Logger;
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.BeforeEach;
// import spark.Spark;
//
// public class BroadBandTest {
//
//  @BeforeAll
//  public static void setup_before_everything() {
//    Spark.port(0);
//    Logger.getLogger("").setLevel(Level.WARNING);
//  }
//
//  /**
//   * Shared state for all tests. We need to be able to mutate it (adding recipes etc.) but never
//   * need to replace the reference itself. We clear this state out after every test runs.
//   */
//  @BeforeEach
//  public void setup() {
//    // Re-initialize state, etc. for _every_ test method run
//    //    this.menu.clear();
//
//    // In fact, restart the entire Spark server for every test!
//    Spark.get("broadband", new BroadbandHandler());
//    Spark.init();
//    Spark.awaitInitialization();
//  }
//
//  @AfterEach
//  public void teardown() {
//    // Gracefully stop Spark listening on both endpoints after each test
//    Spark.unmap("broadband");
//    Spark.awaitStop();
//  }
// }
//
//  /**
//   * Helper to start a connection to a specific API endpoint/params
//   *
//   * @param apiCall the call string, including endpoint (NOTE: this would be better if it had more
//   *     structure!)
//   * @return the connection for the given URL, just after connecting
//   * @throws IOException if the connection fails for some reason
//   */
////  private static HttpURLConnection tryRequest(String apiCall) throws IOException {
////    // Configure the connection (but don't actually send the request yet)
////    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
////    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
////
////    // The default method is "GET", which is what we're using here.
////    // If we were using "POST", we'd need to say so.
////    clientConnection.setRequestMethod("GET");
////
////    clientConnection.connect();
////    return clientConnection;
////  }
//
//  // mock test
//
////  @Test
////  public void validAPITest() throws IOException {
////    HttpURLConnection clientConnection = tryRequest("broadband?state=california&county=013");
////    // Get an OK response (the *connection* worked, the *API* provides an error response)
////    assertEquals(200, clientConnection.getResponseCode());
////
////    // Now we need to see whether we've got the expected Json response.
////    // SoupAPIUtilities handles ingredient lists, but that's not what we've got here.
////    Moshi moshi = new Moshi.Builder().build();
//////     We'll use okio's Buffer class here
////
////        Response response =
////            moshi
////                .adapter(BroadbandHandler.BroadbandSuccessResponse.class)
////                .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
////
////        String data = (String)response.getMap().get("data");
////    //
////    //    System.out.println(response);
////    // ^ If that succeeds, we got the expected response. Notice that this is *NOT* an exception,
//// but
////    // a real Json reply.
////
////    // assertEquals(data, clientConnection.getInputStream().toString());
////
////    clientConnection.disconnect();
////  }
//// }
//
/// **
// * @Test public void testBroadbandWorking() throws IOException { HttpURLConnection loadConnection
// =
// * tryRequest("loadcsv?filepath=data/dol_ri_earnings_disparity.csv");
// *
// * <p>// tests if successful connection assertEquals(200, loadConnection.getResponseCode());
// *
// * <p>// this calls handle(...) method inside load loadConnection.getInputStream();
// *
// * <p>HttpURLConnection broadConnection = tryRequest("broadband?state=california&county=031");
// *
// * <p>Moshi moshi = new Moshi.Builder().build();
// *
// * <p>SuccessResponse response = moshi .adapter(SuccessResponse.class) .fromJson(new
// * Buffer().readFrom(broadConnection.getInputStream()));
// *
// * <p>String jsonData = (String)response.getMap().get("data");
// *
// * <p>Assert.assertEquals(jsonData, MockBroadbandData.ExpectedData);
// *
// * <p>loadConnection.disconnect(); broadConnection.disconnect(); } }
// */
//
/// **
// * //mock test @Test public void mock() throws IOException { HttpURLConnection clientConnection =
// * tryRequest("api call"); // Get an OK response (the *connection* worked, the *API* provides an
// * error response) assertEquals(200, clientConnection.getResponseCode());
// *
// * <p>// Now we need to see whether we've got the expected Json response. // SoupAPIUtilities
// * handles ingredient lists, but that's not what we've got here. Moshi moshi = new
// * Moshi.Builder().build(); // We'll use okio's Buffer class here HANDLER.FAILURERESPONSE response
// =
// * moshi .adapter(HANDLER.SoupNoRecipesFailureResponse.class) .fromJson(new
// * Buffer().readFrom(clientConnection.getInputStream()));
// *
// * <p>System.out.println(response); // ^ If that succeeds, we got the expected response. Notice
// that
// * this is *NOT* an exception, but // a real Json reply.
// *
// * <p>clientConnection.disconnect(); }
// */
