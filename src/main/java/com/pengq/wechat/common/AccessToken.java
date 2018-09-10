package com.pengq.wechat.common;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.pengq.wechat.common.WeChatCache;
import com.pengq.wechat.utils.client.HttpRequest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by pengq on 2018/9/6 22:11.
 */
public class AccessToken implements WeChatCache {
    private static final String API_TOKEN_GET = "https://api.weixin.qq.com/cgi-bin/token";
    private static final String CACHE_KEY = "wechat.access_token";
    protected String cacheKey;
    private String appId;
    private String appSecret;

    public AccessToken(String appId, String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.cacheKey = String.join(".", CACHE_KEY, appId);
    }

    public String get() throws IOException, URISyntaxException {
        String accessToken = get(cacheKey);
        if (accessToken != null && !accessToken.isEmpty()) {
            return accessToken;
        }
        HashMap<String, String> params = new HashMap<String, String>() {{
            put("appid", appId);
            put("secret", appSecret);
            put("grant_type", "client_credential");
        }};
        String response = HttpRequest.httpGet(API_TOKEN_GET, params);
        HashMap<String, Object> responseMap = JSONObject.parseObject(response, new TypeReference<HashMap<String, Object>>() {});
        if (responseMap.containsKey("access_token")) {
            accessToken = String.valueOf(responseMap.get("access_token"));
        }

        long expire_in = Long.parseLong(String.valueOf(responseMap.get("expires_in")));
        Date expireAt = new Date((new Date()).getTime() + ((expire_in - 1500) * 1000));
        set(this.cacheKey, accessToken, expireAt);
        return accessToken;
    }

    public void clearCache(){
        clear(cacheKey);
    }
}
