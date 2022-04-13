package com.dingtalk.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.config.AppConfig;
import com.dingtalk.model.RpcServiceResult;
import com.dingtalk.service.BizManager;
import com.taobao.api.ApiException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * 酷应用机器人消息接收器
 * @author nannanness
 */
@RestController
@RequestMapping("/coolappbot")
public class CoolAppBotMsgController {

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private BizManager bizManager;

    /**
     * 推送的消息
     * @param request
     * @return
     */
    @PostMapping("/msg")
    public String message(HttpServletRequest request, @RequestBody String paramStr) {
        System.out.println("push msg param " + paramStr);

        String timestamp = request.getHeader("timestamp");
        String sign = request.getHeader("sign");
        long now = System.currentTimeMillis();
        if(now - Long.parseLong(timestamp) > 60 * 60 * 1000){
            System.out.println("消息已超时");
        }
        String appSecret = appConfig.getAppSecret();
        String stringToSign = timestamp + "\n" + appSecret;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(appSecret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            String sign2 = new String(Base64.encodeBase64(signData));
            if(sign.equals(sign2)){
                System.out.println("校验通过");
                JSONObject paramJSONObject = JSON.parseObject(paramStr, JSONObject.class);
                String conversationId = paramJSONObject.getString("conversationId");
                String robotCode = paramJSONObject.getString("robotCode");
                appConfig.setConversationId(conversationId);
                appConfig.setRobotCode(robotCode);
                // 接收消息内容
                JSONObject textJSONObjcet = paramJSONObject.getJSONObject("text");
                // 可根据content自定义回复内容
                String content = textJSONObjcet.getString("content");
                // 机器人回消息
                Map map = new HashMap();
                Map contentMap = new HashMap();
                contentMap.put("content", "你好");
                map.put("text", contentMap);
                map.put("msgtype", "text");
                System.out.println(JSON.toJSONString(map));
                return JSON.toJSONString(map);
            } else {
                System.out.println("校验失败! \nsign: " + sign + "\nsign2: " + sign2);
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }
}
