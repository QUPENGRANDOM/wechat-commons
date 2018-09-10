package com.pengq.wechat.common;

import com.pengq.wechat.utils.cache.Cache;
import com.pengq.wechat.utils.cache.LocalFileCache;

import java.util.Date;

/**
 * Created by pengq on 2018/9/6 21:56.
 */
public interface WeChatCache {

    default String get(String key){
        Cache cache = new LocalFileCache();
        return cache.get(key);
    }

    default String get(String key,String defaultValue){
        Cache cache = new LocalFileCache();
        return cache.get(key,defaultValue);
    }

    default void set(String key,String value){
        Cache cache = new LocalFileCache();
        cache.set(key,value);
    }

    default void set(String key,String value,long expireIn){
        Cache cache = new LocalFileCache();
        cache.set(key,value,expireIn);
    }

    default void set(String key, String value, Date expireAt){
        Cache cache = new LocalFileCache();
        cache.set(key,value,expireAt);
    }

    default void clear(String key){
        Cache cache = new LocalFileCache();
        cache.clear(key);
    }
}
