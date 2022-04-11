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
import com.dingtalk.api.request.OapiImChatScencegroupMessageSendV2Request;
import com.dingtalk.api.request.OapiV2UserGetuserinfoRequest;
import com.dingtalk.api.response.OapiImChatScencegroupMessageSendV2Response;
import com.dingtalk.api.response.OapiV2UserGetuserinfoResponse;
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
    public void sendCard(String cardData, String callbackUrl, String cardTemplateId) throws Exception {
        // 根据content的内容进行不同的回复，此处省略
        Client client = createClient();
        SendTemplateInteractiveCardHeaders sendTemplateInteractiveCardHeaders = new SendTemplateInteractiveCardHeaders();

        sendTemplateInteractiveCardHeaders.xAcsDingtalkAccessToken = tokenManager.getAccessToken(appConfig.getAppKey(), appConfig.getAppSecret());
        SendTemplateInteractiveCardRequest sendTemplateInteractiveCardRequest = new SendTemplateInteractiveCardRequest()
                .setCardTemplateId(cardTemplateId)
                .setOpenConversationId(appConfig.getConversationId())
                .setOutTrackId(UUID.randomUUID().toString())
                .setRobotCode(appConfig.getRobotCode())
                .setCallbackUrl(callbackUrl)
                .setCardData(cardData)
                .setSendOptions(null);
        try {
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


}
