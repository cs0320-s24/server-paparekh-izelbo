package edu.brown.cs.student.main.server;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
//import com.google.common.cache.CacheBuilder;
//import com.google.common.cache.CacheLoader;
//import com.google.common.cache.LoadingCache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import edu.brown.cs.student.main.server.BroadbandHandler;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Cache {
    private BroadbandHandler handler;
    private LoadingCache<String, String> cache;

    private Cache(BroadbandHandler handlerInput) {
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

    public String search(String stateID, String countyID) {
        String unchecked = cache.getUnchecked(stateID + "," + countyID);
        System.out.println(cache.stats());
        return unchecked;
    }
}






