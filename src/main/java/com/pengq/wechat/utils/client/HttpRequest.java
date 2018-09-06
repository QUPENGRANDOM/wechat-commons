package com.pengq.wechat.utils.client;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pengq on 2018/9/3 22:04.
 */
public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    public static <T> T httpGet(String url, HashMap<String, String> parameters, Class<T> clazz) throws URISyntaxException, IOException {
        URIBuilder builder = new URIBuilder(url);
        if (parameters != null && !parameters.isEmpty()) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                builder.addParameter(entry.getKey(), entry.getValue());
            }
        }
        logger.info("URL:{}", builder.toString());
        HttpEntity entity = Request.Get(builder.build()).execute().returnResponse().getEntity();
        String response = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        logger.info("RESPONSE:{}", response);

        return JSON.parseObject(response, clazz);
    }

    public static <T> T httpPost(String url, HashMap<String, String> body, Class<T> clazz) throws URISyntaxException, IOException {
        URIBuilder builder = new URIBuilder(url);
        logger.info("URL:{}", builder.toString());
        HttpEntity entity = Request.Post(builder.build()).bodyString(JSON.toJSONString(body), ContentType.APPLICATION_JSON).execute().returnResponse().getEntity();
        String response = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        logger.info("RESPONSE:{}", response);

        return JSON.parseObject(response, clazz);
    }
}
