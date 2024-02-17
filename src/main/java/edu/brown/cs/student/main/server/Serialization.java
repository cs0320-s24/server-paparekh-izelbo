package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;

/** Allows for JSON serialization and deserialization functionality using Moshi */
public class Serialization {

  /**
   * Converts JSON string to a 2D string array
   *
   * @param countyJson the JSON string
   * @return a 2D JSON string array
   */
  public static String[][] convertToArray(String countyJson) {
    try {
      JsonAdapter<String[][]> jsonArrayAdapter = moshiBuilder();
      String[][] jsonArray = jsonArrayAdapter.fromJson(countyJson);
      return jsonArray;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Converts a 2D string array to a JSON string
   *
   * @param countyString the 2D string array
   * @return a JSON string
   * @throws IOException if an I/O error occurs during conversion
   */
  public static String convertToJson(String[][] countyString) throws IOException {
    JsonAdapter<String[][]> jsonArrayAdapter = moshiBuilder();
    String data = jsonArrayAdapter.toJson(countyString);
    return data;
  }

  /**
   * Builds a Moshi JSON adapter for a 2D string array.
   *
   * @return a Moshi JSON adapter for a 2D string array.
   * @throws IOException if an I/O error occurs during adapter creation.
   */
  public static JsonAdapter<String[][]> moshiBuilder() throws IOException {
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<String[][]> jsonAdapter = moshi.adapter(String[][].class);
    return jsonAdapter;
  }
}
