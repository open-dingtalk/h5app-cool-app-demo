package com.dingtalk.service;

import com.alibaba.fastjson.JSON;
import com.aliyun.tea.*;
import com.aliyun.teautil.*;
import com.aliyun.teautil.models.*;
import com.aliyun.dingtalkim_1_0.*;
import com.aliyun.dingtalkim_1_0.models.*;
import com.aliyun.teaopenapi.*;
import com.aliyun.teaopenapi.models.*;

import com.aliyun.dingboot.common.token.ITokenManager;
import com.aliyun.dingtalkim_1_0.Client;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.dingtalk.config.AppConfig;
import com.dingtalk.constant.UrlConstant;
import com.taobao.api.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;


/**
 * 主业务service，编写你的代码
 */
@Service
public class BizManager {

    @Autowired
    private ITokenManager tokenManager;

    @Autowired
    private AppConfig appConfig;

    /**
     * bot发送text消息
     * @return
     */
    public void sendText(String content, String openConversationId, String robotCode) throws ApiException {
        // 根据content的内容进行不同的回复，此处省略

        String accessToken = tokenManager.getAccessToken(appConfig.getAppKey(), appConfig.getAppSecret());
        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.SEND_MSG_URL);
        OapiImChatScencegroupMessageSendV2Request req = new OapiImChatScencegroupMessageSendV2Request();
        req.setMsgTemplateId("inner_app_template_text");
        req.setIsAtAll(false);
        req.setMsgParamMap("{\"content\":"+ content +"}");
        req.setTargetOpenConversationId(openConversationId);
        req.setRobotCode(robotCode);
        OapiImChatScencegroupMessageSendV2Response rsp = client.execute(req, accessToken);
        System.out.println(rsp.getBody());
        if(!rsp.isSuccess()){
            System.out.println("send text error" + rsp.getErrmsg());
        }
    }

    /**
     * bot发送互动卡片消息
     * @return
     */
    public void sendCard(String cardData, String callbackUrl, String cardTemplateId, String conversationId, String atUserListJson) throws Exception {
        // 根据content的内容进行不同的回复，此处省略
        Client client = createClient();
        SendTemplateInteractiveCardHeaders sendTemplateInteractiveCardHeaders = new SendTemplateInteractiveCardHeaders();

        sendTemplateInteractiveCardHeaders.xAcsDingtalkAccessToken = tokenManager.getAccessToken(appConfig.getAppKey(), appConfig.getAppSecret());
        SendTemplateInteractiveCardRequest.SendTemplateInteractiveCardRequestSendOptions sendOptions = new SendTemplateInteractiveCardRequest.SendTemplateInteractiveCardRequestSendOptions();
        sendOptions.setAtUserListJson(atUserListJson);
        SendTemplateInteractiveCardRequest sendTemplateInteractiveCardRequest = new SendTemplateInteractiveCardRequest()
                .setCardTemplateId(cardTemplateId)
                .setOpenConversationId(conversationId)
                .setOutTrackId(UUID.randomUUID().toString())
                .setRobotCode(appConfig.getRobotCode())
                .setCallbackUrl(callbackUrl)
                .setCardData(cardData)
                .setSendOptions(sendOptions);
        try {
            System.out.println(JSON.toJSONString(sendTemplateInteractiveCardRequest));
            SendTemplateInteractiveCardResponse cardResponse = client.sendTemplateInteractiveCardWithOptions(sendTemplateInteractiveCardRequest, sendTemplateInteractiveCardHeaders, new RuntimeOptions());
            System.out.println("cardResponse: " + JSON.toJSONString(cardResponse));
        } catch (TeaException err) {
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
                err.printStackTrace();
                System.out.println(err.code + err.message);
            }

        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
                _err.printStackTrace();
            }

        }
    }

    public static Client createClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new Client(config);
    }

    /**
     * 获取jsapi_ticket
     *
     * @return
     */
    public String getJsapiTicket() throws ApiException {
        String accessToken = tokenManager.getAccessToken(appConfig.getAppKey(), appConfig.getAppSecret());

        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.GET_JSAPI_TICKET);
        OapiGetJsapiTicketRequest req = new OapiGetJsapiTicketRequest();
        req.setHttpMethod("GET");
        OapiGetJsapiTicketResponse rsp = client.execute(req, accessToken);
        System.out.println(rsp.getBody());
        if(!rsp.isSuccess()){
            System.out.println("getJsapiTicket err " + rsp.getErrmsg());
            return null;
        }
        return rsp.getTicket();
    }

    /**
     * 注册卡片回调
     *
     * @return
     */
    public String cardRegister(String domain) throws ApiException {
        String accessToken = tokenManager.getAccessToken(appConfig.getAppKey(), appConfig.getAppSecret());

        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.CARD_CALLBACK_REGISTER);
        OapiImChatScencegroupInteractivecardCallbackRegisterRequest req = new OapiImChatScencegroupInteractivecardCallbackRegisterRequest();
        req.setCallbackUrl(domain + "/coolappbot/card_callback");
        req.setApiSecret("123456");
        OapiImChatScencegroupInteractivecardCallbackRegisterResponse rsp = client.execute(req, accessToken);
        System.out.println(rsp.getBody());
        if(!rsp.isSuccess()){
            System.out.println("cardRegister err " + rsp.getErrmsg());
            return null;
        }
        appConfig.setCardRegister(true);
        return rsp.getBody();
    }

    public OapiV2UserGetResponse.UserGetResponse getUsernameByUserid(String userId) throws ApiException {
        String accessToken = tokenManager.getAccessToken(appConfig.getAppKey(), appConfig.getAppSecret());

        DingTalkClient client = new DefaultDingTalkClient(UrlConstant.USER_GET_URL);
        OapiV2UserGetRequest req = new OapiV2UserGetRequest();
        req.setUserid(userId);
        req.setLanguage("zh_CN");
        OapiV2UserGetResponse oapiV2UserGetResponse = (OapiV2UserGetResponse)client.execute(req, accessToken);
        if (oapiV2UserGetResponse.isSuccess()) {
            return oapiV2UserGetResponse.getResult();
        } else {
            throw new ApiException(oapiV2UserGetResponse.getErrorCode(), oapiV2UserGetResponse.getErrmsg());
        }
    }
}
