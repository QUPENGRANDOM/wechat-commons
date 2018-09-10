package com.pengq.wechat.model;

import java.util.Date;
import java.util.Map;

/**
 * Created by pengq on 2018/9/10 21:00.
 */
public class WeChatUser {
    private static final String SUBSCRIBE_KEY = "subscribe";
    private static final String OPENID_KEY = "openid";
    private static final String NICKNAME_KEY = "nickname";
    private static final String SEX_KEY = "sex";
    private static final String LANGUAGE_KEY = "language";
    private static final String CITY_KEY = "city";
    private static final String PROVINCE_KEY = "province";
    private static final String COUNTRY_KEY = "country";
    private static final String HEAD_IMAGE_URL_KEY = "headimgurl";
    private static final String SUBSCRIBE_TIME_KEY = "subscribe_time";
    private static final String UNION_ID_KEY = "unionid";
    private static final String REMARK_KEY = "remark";
    private static final String GROUP_ID_KEY = "groupid";
    private boolean isSubscribed;
    private String openId;
    private String nickName;
    private int sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headImageUrl;
    private Date subscribedTime;
    private String unionId;
    private String remark;
    private int groupId;

    public WeChatUser(Map<String, Object> data) {
        if (data.containsKey(SUBSCRIBE_KEY)) {
            this.isSubscribed = Integer.parseInt(data.get(SUBSCRIBE_KEY).toString()) == 1;
        }

        if (data.containsKey(OPENID_KEY)) {
            this.openId = data.get(OPENID_KEY).toString();
        }

        if (data.containsKey(NICKNAME_KEY)) {
            this.nickName = data.get(NICKNAME_KEY).toString();
        }

        if (data.containsKey(SEX_KEY)) {
            this.sex = (Integer)data.get(SEX_KEY);
        }

        if (data.containsKey(LANGUAGE_KEY)) {
            this.language = data.get(LANGUAGE_KEY).toString();
        }

        if (data.containsKey(CITY_KEY)) {
            this.city = data.get(CITY_KEY).toString();
        }

        if (data.containsKey(PROVINCE_KEY)) {
            this.province = data.get(PROVINCE_KEY).toString();
        }

        if (data.containsKey(COUNTRY_KEY)) {
            this.country = data.get(COUNTRY_KEY).toString();
        }

        if (data.containsKey(HEAD_IMAGE_URL_KEY)) {
            this.headImageUrl = data.get(HEAD_IMAGE_URL_KEY).toString();
        }

        if (data.containsKey(SUBSCRIBE_TIME_KEY)) {
            this.subscribedTime = new Date((long)(Integer)data.get(SUBSCRIBE_TIME_KEY) * 1000L);
        }

        if (data.containsKey(UNION_ID_KEY)) {
            this.unionId = data.get(UNION_ID_KEY).toString();
        }

        if (data.containsKey(REMARK_KEY)) {
            this.remark = data.get(REMARK_KEY).toString();
        }

        if (data.containsKey(GROUP_ID_KEY)) {
            this.groupId = (int)data.get(GROUP_ID_KEY);
        }

    }

    public boolean isSubscribed() {
        return this.isSubscribed;
    }

    public void setIsSubscribed(boolean isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    public String getOpenId() {
        return this.openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getSex() {
        return this.sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadImageUrl() {
        return this.headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public Date getSubscribedTime() {
        return this.subscribedTime;
    }

    public void setSubscribedTime(Date subscribedTime) {
        this.subscribedTime = subscribedTime;
    }

    public String getUnionId() {
        return this.unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getGroupId() {
        return this.groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
