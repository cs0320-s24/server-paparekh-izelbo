package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.util.List;

public class CountyDataUtilities {

  // This class makes an API call to get the list of list of strings of the county data
  // We then convert the data to a map
  // helper method getCountyMap(); returns the map

  public static Activity deserializeCounty(String jsonActivity) {
    try {
      // Initializes Moshi
      Moshi moshi = new Moshi.Builder().build();

      // Initializes an adapter to an Activity class then uses it to parse the JSON.
      JsonAdapter<Activity> adapter = moshi.adapter(Activity.class);

      Activity activity = adapter.fromJson(jsonActivity);

      return activity;
    }
    // Returns an empty activity... Probably not the best handling of this error case...
    // Notice an alternative error throwing case to the one done in OrderHandler. This catches
    // the error instead of pushing it up.
    catch (IOException e) {
      e.printStackTrace();
      return new Activity();
    }
  }
}