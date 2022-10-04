package com.inswave.appplatform.deployer.cache;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.util.Crypto;
import com.inswave.appplatform.util.FileUtil;
import com.inswave.appplatform.util.cache.TimeoutCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class DeploySegmentCache {
    private TimeoutCache timeoutCache;

    public DeploySegmentCache(@Value("${wdeployer.cache.deploySegmentCache.recordTimeoutDurationSeconds:600}") int recordTimeoutDurationSeconds,
                              @Value("${wdeployer.cache.deploySegmentCache.maximumCacheSize:1000}") int maximumCacheSize) {
        this.timeoutCache = new TimeoutCache(recordTimeoutDurationSeconds, maximumCacheSize);
    }

    public String get(Long deployId, int pos) {
        String result = null;
        String key = generateKey(deployId, pos);
        try {
            result = _get(key);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (result == null) {
            Path targetFile = Paths.get(Config.getInstance().getDeployerRepoPath(), deployId.toString());

            try {
                result = Crypto.base64Encode(FileUtil.getBytes(targetFile.toFile(), (long) pos * Constants.TAG_DEPLOYER_CHUNK_BYTE_SIZE, Constants.TAG_DEPLOYER_CHUNK_BYTE_SIZE));
            } catch (IOException e) {
                e.printStackTrace();
            }
            _put(key, result);
        }

        return result;
    }

    public void put(Long deployId, int pos, String data) {
        _put(generateKey(deployId, pos), data);
    }

    private String _get(String key) throws ExecutionException {
        return timeoutCache.get(key);
    }

    private void _put(String key, String value) {
        timeoutCache.put(key, value);
    }

    private String generateKey(Long deployId, int pos) {
        return String.format("%d-%d", deployId, pos);
    }
}
