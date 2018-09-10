package com.pengq.wechat.common;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.pengq.wechat.exception.WeChatException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pengq on 2018/9/6 21:53.
 */
public class WeChatService implements WeChatCache {
    protected static final String ACCESS_TOKEN_KEY = "access_token";
    protected static final String ERROR_CODE_KEY = "errcode";
    private static final int ACCESS_TOKEN_EXPIRE_RETRY_COUNT = 3;
    protected String appId;
    protected String appSecret;
    protected AccessToken accessToken;

    public WeChatService(String appId, String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.accessToken = new AccessToken(appId, appSecret);
    }

    protected Map<String, Object> validateResponse(String content) throws WeChatException {
        HashMap<String, Object> responseMap = JSONObject.parseObject(content, new TypeReference<HashMap<String, Object>>() {});
        this.validateResponse(responseMap);
        return responseMap;
    }

    protected void validateResponse(Map<String, Object> content) throws WeChatException {
        int errorCode;
        if (content.containsKey(ERROR_CODE_KEY) && content.get(ERROR_CODE_KEY) instanceof Integer && (errorCode = (Integer)content.get(ERROR_CODE_KEY)) != 0) {
            if (errorCode == 40001 || errorCode == 40014 || errorCode == 42001) {
                this.accessToken.clearCache();
            }

            throw new WeChatException(errorCode);
        }
    }
}
