package com.inswave.appplatform.util.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TimeoutCache {

    private LoadingCache<String, String> cache;

    public TimeoutCache(int recordTimeoutDurationSeconds, int maximumCacheSize) {
        CacheLoader<String, String> loader;
        loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                return key;
            }
        };

        cache = CacheBuilder.newBuilder()
                            .maximumSize(maximumCacheSize)
//                            .expireAfterAccess(recordTimeoutDurationSeconds, TimeUnit.SECONDS)
                            .expireAfterWrite(recordTimeoutDurationSeconds, TimeUnit.SECONDS)
                            .build(loader);
    }

    public void put(String key, String value) {
        cache.put(key, value);
    }

    public String get(String key) throws ExecutionException {
        String result = cache.get(key);
        if(result.equals(key)) {
            return null;
        }
        return result;
    }
}
