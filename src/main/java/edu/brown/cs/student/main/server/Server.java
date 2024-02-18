package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import spark.Spark;

/** Starts the server and configures the server's routes */
public class Server {

  /**
   * Main method to start the server, initialize Spark, and start the server on the port
   *
   * @param args
   */
  public static void main(String[] args) {
    int port = 3232;
    Spark.port(port);
    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    LoadHandler load = new LoadHandler();
    Spark.get("loadcsv", load);
    Spark.get("viewcsv", new ViewHandler(load));
    Spark.get("searchcsv", new SearchHandler(load));

    Spark.get("broadband", new BroadbandHandler());

    Spark.init();
    Spark.awaitInitialization();

    System.out.println("Server started at http://localhost:" + port);
    System.out.println("How to use the Server program:");
    System.out.println("http://localhost:3232/[insert type of handler]?[filepath=][CSV data]");
    System.out.println(
        "Example: http://localhost:3232/loadcsv?filepath=data/census/dol_ri_earnings_disparity.csv");
  }
}
