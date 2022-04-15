package com.dingtalk.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.config.AppConfig;
import com.dingtalk.model.RpcServiceResult;
import com.dingtalk.service.BizManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主业务Controller，编写你的代码
 */
@RestController
@RequestMapping("/biz")
public class BizController {

    @Autowired
    private BizManager bizManager;

    @Autowired
    private AppConfig appConfig;

    @RequestMapping("/pushAnnouncement")
    public RpcServiceResult pushAnnouncement(@RequestBody String paramStr) throws Exception {
        System.out.println("paramStr " + paramStr);
        JSONObject paramObject = JSON.parseObject(paramStr);
        String title = paramObject.getString("title");
        String content = paramObject.getString("content");
        String conversationId = paramObject.getString("cid");
        String img = paramObject.getString("img");
        String domain = paramObject.getString("domain");
        //解码
        String cid = URLDecoder.decode(conversationId, "UTF-8");
        System.out.println("cid: " + cid);
        if(!appConfig.isCardRegister()){
            String s = bizManager.cardRegister(domain);
        }
        String cardData = conAnnouncementCardData(title, content, img);
        bizManager.sendCard(cardData, null, "TuWenCard02", cid, null);
        return RpcServiceResult.getSuccessResult(null);
    }

    @RequestMapping("/syncSchedule")
    public RpcServiceResult syncSchedule(@RequestBody String paramStr) throws Exception {
        System.out.println("paramStr " + paramStr);
        JSONObject paramObject = JSON.parseObject(paramStr);
        String title = paramObject.getString("title");
        String date = paramObject.getString("date");
        String address = paramObject.getString("address");
        String conversationId = paramObject.getString("cid");
        String domain = paramObject.getString("domain");
        JSONArray users = paramObject.getJSONArray("users");
        JSONArray departments = paramObject.getJSONArray("departments");
        List<Map> userList = new ArrayList<>();
        for (Object user : users){
            JSONObject userObj = (JSONObject) JSON.toJSON(user);
            Map<String, String> option = new HashMap<>();
            option.put("nickName", userObj.getString("name"));
            option.put("userId", userObj.getString("emplId"));
            userList.add(option);
        }
        System.out.println("userList : " + JSON.toJSONString(userList));
        //解码
        String cid = URLDecoder.decode(conversationId, "UTF-8");
        System.out.println("cid: " + cid);
        if(!appConfig.isCardRegister()){
            String s = bizManager.cardRegister(domain);
        }
        String cardData = conScheduleCardData(title, date, address);
        bizManager.sendCard(cardData, null, "TuWenCard02", cid, JSON.toJSONString(userList));
        return RpcServiceResult.getSuccessResult(null);
    }

    private String conScheduleCardData(String title, String date, String address){
        // 轻量级互动卡片消息，参考文档：https://open.dingtalk.com/document/group/lightweight-access-document-of-interactive-cards
        String templateCard = "{\"header\":{\"icon\":{\"light\":\"https://img.alicdn.com/imgextra/i2/O1CN01s9affE1erEufIzQpl_!!6000000003924-2-tps-20-20.png\",\"dark\":\"https://img.alicdn.com/imgextra/i2/O1CN01s9affE1erEufIzQpl_!!6000000003924-2-tps-20-20.png\"},\"text\":{\"zh_Hans\":\"日程\"},\"color\":{\"light\":\"#00B853\",\"dark\":\"#00B853\"}},\"contents\":[{\"text\":{\"zh_Hans\":\"\"},\"type\":\"TITLE\"},{\"text\":{\"zh_Hans\":\"\"},\"type\":\"PARAGRAPH\",\"icon\":{\"light\":\"https://img.alicdn.com/imgextra/i4/O1CN01fqHCiZ1gJvKmWLO1o_!!6000000004122-2-tps-20-20.png\",\"dark\":\"https://img.alicdn.com/imgextra/i4/O1CN01fqHCiZ1gJvKmWLO1o_!!6000000004122-2-tps-20-20.png\"}},{\"text\":{\"zh_Hans\":\"\"},\"type\":\"PARAGRAPH\",\"icon\":{\"light\":\"https://img.alicdn.com/imgextra/i3/O1CN01nRFcgA1FtCCpnxuAd_!!6000000000544-2-tps-20-20.png\",\"dark\":\"https://img.alicdn.com/imgextra/i3/O1CN01nRFcgA1FtCCpnxuAd_!!6000000000544-2-tps-20-20.png\"}}],\"actions\":[{\"id\":\"1\",\"text\":{\"zh_Hans\":\"参加\"},\"afterClickText\":{\"zh_Hans\":\"已参加\"},\"icon\":{\"light\":\"https://img.alicdn.com/imgextra/i4/O1CN01LsnTCi1DM6M9RH0bL_!!6000000000201-2-tps-21-20.png\"},\"status\":\"NORMAL\",\"actionType\":\"LWP\"},{\"id\":\"2\",\"text\":{\"zh_Hans\":\"拒绝\"},\"afterClickText\":{\"zh_Hans\":\"已拒绝\"},\"icon\":{\"light\":\"https://img.alicdn.com/imgextra/i3/O1CN01v0i8RN1QjXdSs5Crr_!!6000000002012-2-tps-20-20.png\"},\"status\":\"NORMAL\",\"actionType\":\"LWP\"}],\"actionDirection\":\"HORIZONTAL\"}";
        JSONObject templateObject = JSON.parseObject(templateCard);
        JSONArray contents = templateObject.getJSONArray("contents");
        JSONObject contentObject = contents.getJSONObject(0);
        JSONObject contentText = contentObject.getJSONObject("text");
        contentText.put("zh_Hans", title);
        JSONObject contentObject1 = contents.getJSONObject(1);
        JSONObject contentText1 = contentObject1.getJSONObject("text");
        contentText1.put("zh_Hans", date);
        JSONObject contentObject2 = contents.getJSONObject(2);
        JSONObject contentText2 = contentObject2.getJSONObject("text");
        contentText2.put("zh_Hans", address);
        System.out.println(templateObject.toJSONString());
        return templateObject.toJSONString();
    }

    private String conAnnouncementCardData(String title, String content, String img){
        // 轻量级互动卡片消息，参考文档：https://open.dingtalk.com/document/group/lightweight-access-document-of-interactive-cards
        String templateCard = "{\"contents\":[{\"icon\":{\"light\":\"https://img.alicdn.com/imgextra/i2/O1CN010hPrWX1W3lC8FHiZy_!!6000000002733-2-tps-20-20.png\",\"dark\":\"https://img.alicdn.com/imgextra/i2/O1CN010hPrWX1W3lC8FHiZy_!!6000000002733-2-tps-20-20.png\"},\"text\":{\"zh_Hans\":\"大家按照这个格式填写下，每周我会做一个统计和公布哈，和大家同步下我们的进展\"},\"type\":\"PARAGRAPH\"},{\"markdown\":\"![](https://img.alicdn.com/imgextra/i4/O1CN01WytKUw1YHA0FFUOW3_!!6000000003033-2-tps-80-84.png)\",\"type\":\"MARKDOWN\"}],\"actionDirection\":\"HORIZONTAL\",\"header\":{\"color\":{\"light\":\"#00B853\",\"dark\":\"#00B853\"},\"icon\":{\"light\":\"https://img.alicdn.com/imgextra/i1/O1CN01yIywvJ1MtNgCBowHr_!!6000000001492-2-tps-20-20.png\",\"dark\":\"https://img.alicdn.com/imgextra/i1/O1CN01yIywvJ1MtNgCBowHr_!!6000000001492-2-tps-20-20.png\"},\"text\":{\"zh_Hans\":\"公告\"}},\"actions\":[{\"actionType\":\"LWP\",\"afterClickText\":{\"zh_Hans\":\"已接收\"},\"icon\":{\"light\":\"https://img.alicdn.com/imgextra/i2/O1CN01lRTg0a1kRUKyjxpSw_!!6000000004680-2-tps-20-20.png\"},\"id\":\"1\",\"text\":{\"zh_Hans\":\"收到\"},\"status\":\"NORMAL\"}]}";
        JSONObject templateObject = JSON.parseObject(templateCard);
        JSONObject header = templateObject.getJSONObject("header");
        JSONObject headerText = header.getJSONObject("text");
        headerText.put("zh_Hans", title);
        JSONArray contents = templateObject.getJSONArray("contents");
        JSONObject contentObject = contents.getJSONObject(0);
        JSONObject contentText = contentObject.getJSONObject("text");
        contentText.put("zh_Hans", content);
        JSONObject contentObject2 = contents.getJSONObject(1);
        contentObject2.put("markdown", "![](" + img + ")");
        System.out.println(templateObject.toJSONString());
        return templateObject.toJSONString();
    }

}
