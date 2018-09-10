package com.pengq.wechat.utils;

import com.pengq.wechat.common.WeChatService;

/**
 * Created by pengq on 2018/9/6 22:04.
 */
public class WeChat extends WeChatService {
    public WeChat(String appId, String appSecret) {
        super(appId, appSecret);
    }

    @Override
    public String get(String key) {
        return "asasd";
    }
}
