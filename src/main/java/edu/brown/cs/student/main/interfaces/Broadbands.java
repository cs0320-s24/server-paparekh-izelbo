package edu.brown.cs.student.main.interfaces;

import java.io.IOException;
import java.net.URISyntaxException;

/** Interface for sending broadband data requests */
public interface Broadbands {

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
  String sendRequest(String stateID, String countyID)
      throws URISyntaxException, IOException, InterruptedException;
}
