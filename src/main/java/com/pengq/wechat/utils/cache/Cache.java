package com.pengq.wechat.utils.cache;

import java.util.Date;

/**
 * Created by pengq on 2018/9/6 20:56.
 */
public interface Cache {
    String get(String key);
    String get(String key,String defaultValue);
    void set(String key, String value);
    void set(String key, String value, Date expireAt);
    void set(String key, String value, long expireIn);
    void clear(String key);
}
