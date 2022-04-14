package com.dingtalk.constant;

/**
 * 钉钉开放接口网关常量
 */
public class UrlConstant {

    /**
     * 获取access_token url
     */
    public static final String GET_ACCESS_TOKEN_URL = "https://oapi.dingtalk.com/gettoken";

    /**
     * 通过免登授权码获取用户信息 url
     */
    public static final String GET_USER_INFO_URL = "https://oapi.dingtalk.com/topapi/v2/user/getuserinfo";
    /**
     * 根据用户id获取用户详情 url
     */
    public static final String USER_GET_URL = "https://oapi.dingtalk.com/topapi/v2/user/get";
    /**
     * 发送消息 url
     */
    public static final String SEND_MSG_URL = "https://oapi.dingtalk.com/topapi/im/chat/scencegroup/message/send_v2";
    /**
     * 获取jsapi_ticket url
     */
    public static final String GET_JSAPI_TICKET = "https://oapi.dingtalk.com/get_jsapi_ticket";
    /**
     * 注册卡片回调 url
     */
    public static final String CARD_CALLBACK_REGISTER = "https://oapi.dingtalk.com/topapi/im/chat/scencegroup/interactivecard/callback/register";
}
