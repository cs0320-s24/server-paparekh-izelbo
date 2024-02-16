package edu.brown.cs.student.main.search;

import static org.testng.AssertJUnit.*;

import java.util.EnumSet;
import org.junit.jupiter.api.Test;

/** SearchOptionTest test class */
class SearchOptionsTest {

  /** Tests default search options */
  @Test
  public void testDefaultSearchOptions() {
    SearchOptions options = new SearchOptions();
    assertEquals(
        "Default search type should be CASE_SENSITIVE",
        true,
        options.isType(SearchType.CASE_SENSITIVE).booleanValue());
    assertNull("Default column should be null", options.getColumn());
  }

  /** Tests set and get column */
  @Test
  public void testSetAndGetColumn() {
    SearchOptions options = new SearchOptions();
    options.setColumn("testColumn");
    assertEquals("Column should be set to 'testColumn'", "testColumn", options.getColumn());
  }

  /** Tests set and get search types */
  @Test
  public void testSetAndGetSearchType() {
    SearchOptions options = new SearchOptions();
    options.setTypeEnumSet(EnumSet.of(SearchType.CASE_INSENSITIVE, SearchType.EXACT));
    assertTrue(
        "Search type should contain CASE_INSENSITIVE", options.isType(SearchType.CASE_INSENSITIVE));
    assertTrue("Search type should contain EXACT", options.isType(SearchType.EXACT));
    assertFalse(
        "Search type should not contain CASE_SENSITIVE", options.isType(SearchType.CASE_SENSITIVE));
  }
}
