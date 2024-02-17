package edu.brown.cs.student.main.csv;

import edu.brown.cs.student.main.server.Server;

/** The Main class of our project. This is where execution begins. */
public final class Main {
  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private Main(String[] args) {}

  /** Starts the CLI */
  private void run() {
    Server server = new Server();
  }
}
