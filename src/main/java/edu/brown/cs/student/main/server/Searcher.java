package edu.brown.cs.student.main.server;

/** The Searcher interface provides a template for search */
public interface Searcher {

  /**
   * Search method declaration
   *
   * @param textToSearch String to search
   * @param searchOptions String for options
   * @return SearchResult
   * @throws IllegalArgumentException
   */
  public SearchResult search(String textToSearch, SearchOptions searchOptions)
      throws IllegalArgumentException;
}
