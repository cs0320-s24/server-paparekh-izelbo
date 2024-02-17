package edu.brown.cs.student.main.server;

import java.io.IOException;
import java.net.URISyntaxException;

/** Interface for sending broadband data requests */
public interface Broadbands {
  String sendRequest(String stateID, String countyID)
      throws URISyntaxException, IOException, InterruptedException;
}
