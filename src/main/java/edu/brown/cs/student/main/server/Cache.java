package edu.brown.cs.student.main.server;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/** Cache for storing and retrieving broadband data by using Google's Guava LoadingCache */
public class Cache {
  private BroadbandHandler handler;
  private LoadingCache<String, String> cache;

  /**
   * Constructs a Cache object with a specified BroadbandHandler
   *
   * @param handlerInput the BroadbandHandler to be used for sending/retrieving requests and data
   */
  public Cache(BroadbandHandler handlerInput) {
    this.handler = handlerInput;
    this.cache =
        CacheBuilder.newBuilder()
            .maximumSize(10)
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .recordStats()
            .build(
                new CacheLoader<>() {
                  @Override
                  public String load(String key)
                      throws URISyntaxException, IOException, InterruptedException {
                    String[] keyValue = key.split(",");
                    System.out.println("No data Cached for: " + Arrays.toString(keyValue));
                    return handler.sendRequest(keyValue[0], keyValue[1]);
                  }
                });
  }

  /**
   * Searches for cached broadband data based on input state and county IDs
   *
   * @param stateID the state ID
   * @param countyID the county ID
   * @return the cached broadband data
   */
  public String search(String stateID, String countyID) {
    String unchecked = cache.getUnchecked(stateID + "," + countyID);
    System.out.println(cache.stats());
    return unchecked;
  }
}
