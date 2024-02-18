package edu.brown.cs.student.main.csv;

import static org.testng.Assert.assertEquals;

import edu.brown.cs.student.main.interfaces.Creator;
import edu.brown.cs.student.main.interfaces.Star;
import edu.brown.cs.student.main.interfaces.StarCreator;
import edu.brown.cs.student.main.search.Parser;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParseTest {

  public ParseTest() {}

  /**
   * Test basic parsing
   *
   * @throws FileNotFoundException
   */
  @Test
  public void testBasicParse() throws FileNotFoundException {
    FileReader file =
        new FileReader("/Users/ianzelbo/server-paparekh-izelbo/data/stars/ten-star.csv");
    Creator creator = new Creator();
    Parser<String[]> parser = new Parser<>(file, creator);

    List<String[]> parsedData = parser.parse();

    ArrayList<String> testData = new ArrayList<>(Arrays.asList("0", "Sol", "0", "0", "0"));

    assertEquals(
        Arrays.asList(parsedData.get(1)),
        testData,
        "The parsed data does not match the expected data.");
    assertEquals(parsedData.get(3)[3], "0.00285", "The specific value does not match.");
  }

  /** Tests StringReader object -- generic parser */
  @Test
  public void testStringReader() {

    StringReader stringReader =
        new StringReader("Black,Orange,Red\n" + "hamster,tomato,shirt\n" + "9,8,7");

    Creator creator = new Creator();
    Parser<String[]> parser = new Parser<>(stringReader, creator);

    List<String[]> parsedData = parser.parse();

    String input = "Black,Orange,Red\nhamster,tomato,shirt\n9,8,7";
    // Split the string into an array of strings using the newline character
    String[] items = input.split("\n");
    // Convert the array to an ArrayList
    ArrayList<String> testData = new ArrayList<>(Arrays.asList(items));

    assertEquals(parsedData.get(0)[2], "Red");

    // Loop for array comparison
    for (int i = 0; i < testData.size(); i++) {
      String[] expectedLine = testData.get(i).split(",");
      String[] parsedLine = parsedData.get(i);
      Assertions.assertArrayEquals(expectedLine, parsedLine, "Line " + i + " does not match.");
    }
  }

  /** Tests empty reader */
  @Test
  public void empty() {
    StringReader stringReader = new StringReader("");
    Creator creator = new Creator();
    Parser<String[]> parser = new Parser<>(stringReader, creator);
    List<String[]> parsedData = parser.parse();
    assertEquals(parsedData.size(), 0, "The parsed data is not empty.");
  }

  /**
   * Tests the CreatorFromRow interface
   *
   * @throws FileNotFoundException
   */
  @Test
  public void testCreatorFromRow() throws FileNotFoundException {
    java.io.Reader starReader =
        new FileReader("/Users/ianzelbo/server-paparekh-izelbo/data/stars/ten-star.csv");
    Parser<Star> starParser = new Parser(starReader, new StarCreator());

    Star expectedStar = new Star("3759", "96 G. Psc", "7.26388", "1.55643", "0.68697");
    Star actualStar = starParser.parse().get(5);
    assertEquals(actualStar.StarID(), expectedStar.StarID());
    assertEquals(actualStar.ProperName(), expectedStar.ProperName());
    assertEquals(actualStar.X(), expectedStar.X());
    assertEquals(actualStar.Y(), expectedStar.Y());
    assertEquals(actualStar.Z(), expectedStar.Z());
    assertEquals(actualStar.toString(), expectedStar.toString());
  }
}
