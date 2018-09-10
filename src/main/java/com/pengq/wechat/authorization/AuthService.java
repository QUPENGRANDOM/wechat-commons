package com.pengq.wechat.authorization;

import com.pengq.wechat.common.WeChatService;
import com.pengq.wechat.exception.WeChatException;
import com.pengq.wechat.model.WeChatUser;
import com.pengq.wechat.utils.client.HttpRequest;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pengq on 2018/9/10 20:56.
 */
public class AuthService extends WeChatService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private static final String API_USER = "https://api.weixin.qq.com/sns/userinfo";
    private static final String API_TOKEN_GET = "https://api.weixin.qq.com/sns/oauth2/access_token";
    private static final String API_TOKEN_REFRESH = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
    private static final String API_TOKEN_VALIDATE = "https://api.weixin.qq.com/sns/auth";
    private static final String API_URL = "https://open.weixin.qq.com/connect/oauth2/authorize";
    private Map<String, Object> lastPermission;
    private WeChatUser authorizedUser;

    public AuthService(String appId, String appSecret) {
        super(appId, appSecret);
    }

    public boolean checkAuthorize(String code, String state) {
        return code != null && !code.isEmpty() && state != null && !state.isEmpty();
    }

    public WeChatUser authorize(HttpServletRequest request, HttpServletResponse response) throws URISyntaxException, IOException, WeChatException {
        return this.authorize(request, response, "snsapi_userinfo", "STATE");
    }

    public WeChatUser authorize(HttpServletRequest request, HttpServletResponse response, String scope, String state) throws URISyntaxException, IOException, WeChatException {
        assert request != null && response != null;

        String codeParam = request.getParameter("code");
        String stateParam = request.getParameter("state");
        if (stateParam == null) {
            URIBuilder builder = new URIBuilder(request.getRequestURL().toString());
            Map<String, String[]> parameterMap = request.getParameterMap();

            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String[] arr = parameterMap.get(entry.getKey());
                for (String value : arr) {
                    builder.addParameter(entry.getKey(), value);
                }
            }

            String redirectUrl = this.getOAuthURL(builder.build().toString(), scope, state);
            logger.info("redirectUrl:{} " , redirectUrl);

            try {
                response.sendRedirect(redirectUrl);
            } catch (IOException e) {
                logger.error("send redirect fail");
            }
        } else if (codeParam != null) {
            logger.info("Loading Authorized User");
            return this.getAuthorizedUser(codeParam);
        }

        logger.info("Something happened, returning null");
        return null;
    }

    public String getOAuthURL(String redirectURL) throws URISyntaxException {
        return this.getOAuthURL(redirectURL, "snsapi_userinfo", "STATE");
    }

    public String getOAuthURL(String redirectURL, String scope, String state) throws URISyntaxException {
        return (new URIBuilder(API_URL)).addParameter("appid", this.appId).addParameter("redirect_uri", redirectURL).addParameter("response_type", "code").addParameter("scope", scope).addParameter("state", state).setFragment("wechat_redirect").build().toString();
    }

    public WeChatUser getAuthorizedUser(String code) throws WeChatException, IOException, URISyntaxException {
        logger.info("getAuthorizedUser: code is " + code);
        Map<String, Object> permission = this.getAccessPermission(code);
        if (permission == null) {
            return null;
        } else {
            if (!permission.get("scope").equals("snsapi_userinfo")) {
                this.authorizedUser = new WeChatUser(permission);
            } else {
                this.authorizedUser = this.getUser(permission.get("openid").toString(), permission.get("access_token").toString());
            }

            return this.authorizedUser;
        }
    }

    public boolean accessTokenIsValid(String openId, String accessToken) throws URISyntaxException, IOException {
        HashMap<String,String> param = new HashMap<String, String>(){{
            put("openid", openId);
            put("access_token", accessToken);
        }};
        String response = HttpRequest.httpGet(API_TOKEN_VALIDATE,param);

        try {
            Map<String, Object> entity = this.validateResponse(response);
            return entity.containsKey(ERROR_CODE_KEY) && entity.get(ERROR_CODE_KEY) instanceof Integer && (Integer)entity.get(ERROR_CODE_KEY) == 0;
        } catch (WeChatException var5) {
            return false;
        }
    }

    public void refreshAccessToken(String refreshToken) throws WeChatException, IOException, URISyntaxException {
        HashMap<String,String> param = new HashMap<String, String>(){{
            put("appid", appId);
            put("grant_type", "refresh_token");
            put("refresh_token", refreshToken);
        }};
        String response = HttpRequest.httpGet(API_TOKEN_REFRESH,param);
        Map<String, Object> entity = this.validateResponse(response);
        this.lastPermission.putAll(entity);
    }

    private WeChatUser getUser(String openId, String access_token) throws WeChatException, IOException, URISyntaxException {
        HashMap<String,String> param = new HashMap<String, String>(){{
            put("access_token", access_token);
            put("openid", openId);
            put("lang", "zh_CN");
        }};
        String response = HttpRequest.httpGet(API_USER,param);
        Map<String, Object> entity = this.validateResponse(response);
        return new WeChatUser(entity);
    }

    private Map<String, Object> getAccessPermission(String code) throws WeChatException, IOException, URISyntaxException {
        HashMap<String,String> param = new HashMap<String, String>(){{
            put("appid", appId);
            put("secret", appSecret);
            put("code", code);
            put("grant_type", "authorization_code");
        }};

        String response = HttpRequest.httpGet(API_TOKEN_GET,param);
        Map<String, Object> entity = this.validateResponse(response);
        return this.lastPermission = entity;
    }
}
