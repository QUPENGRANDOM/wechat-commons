package com.pengq.wechat;

import com.pengq.wechat.utils.cache.Cache;
import com.pengq.wechat.utils.cache.LocalFileCache;

/**
 * Created by pengq on 2018/9/6 21:41.
 */
public class Test {
    public static void main(String[] args){
        String key = "wechat_access_token";
        long t = 3*1000*60;
        String value = "haha";

        Cache cache = new LocalFileCache();
//        cache.set(key,value,t);

        cache.get(key);
    }
}
