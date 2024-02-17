package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class Serialization {

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

  public static String convertToJson(String[][] countyString) throws IOException {
    JsonAdapter<String[][]> jsonArrayAdapter = moshiBuilder();
    String data = jsonArrayAdapter.toJson(countyString);
    return data;
  }

  public static JsonAdapter<String[][]> moshiBuilder() throws IOException {
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<String[][]> jsonAdapter = moshi.adapter(String[][].class);
    return jsonAdapter;
  }
}
