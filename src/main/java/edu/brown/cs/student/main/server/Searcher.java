package edu.brown.cs.student.main.server;

/** The Searcher interface provides a template for search */
public interface Searcher {
  public SearchResult search(String textToSearch, SearchOptions searchOptions)
      throws IllegalArgumentException;
}
