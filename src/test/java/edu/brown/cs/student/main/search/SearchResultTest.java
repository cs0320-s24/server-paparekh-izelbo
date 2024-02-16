package edu.brown.cs.student.main.search;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

/** SearchResult test class */
class SearchResultTest {

  /** Tests add result and get result */
  @Test
  public void testAddResultAndGetResults() {
    SearchResult searchResult = new SearchResult();

    // Adding results
    searchResult.addResult(1, 1);
    searchResult.addResult(1, 2);
    searchResult.addResult(2, 3);
    searchResult.addResult(3, 1);

    // Retrieving results
    HashMap<Integer, ArrayList<Integer>> results = searchResult.getResults();

    // Validating results
    assertEquals("Total map size should be 3", 3, results.size());

    // Validating results
    assertEquals("Total found should be 3", 3, searchResult.getTotalFound());

    // Validating results for each row
    assertEquals(Arrays.asList(1, 2), results.get(1));
    assertEquals(Arrays.asList(3), results.get(2));
    assertEquals(Arrays.asList(1), results.get(3));

    // Testing an invalid row
    assertNull("Results for non-existing row should be null", results.get(4));
  }
}
