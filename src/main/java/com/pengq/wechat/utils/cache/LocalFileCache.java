package com.pengq.wechat.utils.cache;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Created by pengq on 2018/9/6 21:06.
 */
public class LocalFileCache implements Cache {
    private static final Logger logger = LoggerFactory.getLogger(LocalFileCache.class);
    private static final String CACHE_FILE_CHILD = "wechat";
    private static final String EXPIRE_PREFIX = "EXPIRE:";

    @Override
    public String get(String key) {
        return get(key,null);
    }

    @Override
    public String get(String key, String defaultValue) {
        File file = new File(getCacheFile(key));
        if (!file.exists()) {
            return defaultValue;
        }

        BufferedReader reader = null;
        boolean isExpired = false;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(EXPIRE_PREFIX)) {
                    long expireAtTime = Long.parseLong(line.substring(7).trim());
                    isExpired = expireAtTime != 0L && (new Date(expireAtTime)).before(new Date());
                } else {
                    sb.append(line);
                }
            }
            return isExpired ? defaultValue : sb.toString();

        } catch (IOException e) {
            logger.error("get cache fail:[key:{}]",key);
            return defaultValue;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException var16) {
                    var16.printStackTrace();
                }
            }
            if (isExpired) {
                file.delete();
            }
        }
    }

    @Override
    public void set(String key, String value) {
        set(key, value, null);
    }

    @Override
    public void set(String key, String value, Date expireAt) {
        File file = new File(getCacheFile(key));
        if (file.exists()) {
            file.delete();
        }

        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
            writer.write(EXPIRE_PREFIX + (expireAt == null ? 0L : expireAt.getTime()) + "\n");
            writer.write(value);
        } catch (IOException e) {
            logger.error("write cache fail:[key:{},value:{},expireAt:{}]",key,value,expireAt == null ? 0L : expireAt.getTime());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.error("close stream fail");
                }
            }
        }
    }

    @Override
    public void set(String key, String value, long expireIn) {
        set(key, value, new Date(System.currentTimeMillis() + expireIn * 3600L * 1000L));
    }

    @Override
    public void clear(String key) {
        File file = new File(getCacheFile(key));
        if (file.exists()) {
            file.delete();
        }
    }

    private String getCacheFile(String key) {
        File tempDir = new File(System.getProperty("java.io.tmpdir"), CACHE_FILE_CHILD);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }

        String encodedKey = DigestUtils.md5Hex(key);
        File tempFile = new File(tempDir, encodedKey);
        return tempFile.toString();
    }
}
