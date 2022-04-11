package com.dingtalk.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.model.RpcServiceResult;
import com.dingtalk.service.BizManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 主业务Controller，编写你的代码
 */
@RestController
@RequestMapping("/biz")
public class BizController {

    @Autowired
    BizManager bizManager;

    @RequestMapping("/pushAnnouncement")
    public RpcServiceResult pushAnnouncement(@RequestBody String paramStr) throws Exception {
        System.out.println("paramStr " + paramStr);
        JSONObject paramObject = JSON.parseObject(paramStr);
        String title = paramObject.getString("title");
        String content = paramObject.getString("content");
        String cardData = conAnnouncementCardData(title, content);
        bizManager.sendCard(cardData, null, "TuWenCard01");
        return RpcServiceResult.getSuccessResult(null);
    }

    @RequestMapping("/syncSchedule")
    public RpcServiceResult syncSchedule(@RequestBody String paramStr) throws Exception {
        System.out.println("paramStr " + paramStr);
        JSONObject paramObject = JSON.parseObject(paramStr);
        String title = paramObject.getString("title");
        String date = paramObject.getString("date");
        String address = paramObject.getString("address");
        String cardData = conScheduleCardData(title, date, address);
        bizManager.sendCard(cardData, null, "TuWenCard02");
        return RpcServiceResult.getSuccessResult(null);
    }

    private String conScheduleCardData(String title, String date, String address){
        // 轻量级互动卡片消息，参考文档：https://open.dingtalk.com/document/group/lightweight-access-document-of-interactive-cards
        String templateCard = "{\"header\":{\"icon\":{\"light\":\"https://img.alicdn.com/imgextra/i2/O1CN01s9affE1erEufIzQpl_!!6000000003924-2-tps-20-20.png\",\"dark\":\"https://img.alicdn.com/imgextra/i2/O1CN01s9affE1erEufIzQpl_!!6000000003924-2-tps-20-20.png\"},\"text\":{\"zh_Hans\":\"\"},\"color\":{\"light\":\"#00B853\",\"dark\":\"#00B853\"}},\"contents\":[{\"text\":{\"zh_Hans\":\"\"},\"type\":\"PARAGRAPH\",\"icon\":{\"light\":\"https://img.alicdn.com/imgextra/i4/O1CN01fqHCiZ1gJvKmWLO1o_!!6000000004122-2-tps-20-20.png\",\"dark\":\"https://img.alicdn.com/imgextra/i4/O1CN01fqHCiZ1gJvKmWLO1o_!!6000000004122-2-tps-20-20.png\"}},{\"text\":{\"zh_Hans\":\"\"},\"type\":\"PARAGRAPH\",\"icon\":{\"light\":\"https://img.alicdn.com/imgextra/i3/O1CN01nRFcgA1FtCCpnxuAd_!!6000000000544-2-tps-20-20.png\",\"dark\":\"https://img.alicdn.com/imgextra/i3/O1CN01nRFcgA1FtCCpnxuAd_!!6000000000544-2-tps-20-20.png\"}}],\"actions\":[{\"id\":\"1\",\"text\":{\"zh_Hans\":\"钉钉网站\"},\"icon\":{\"light\":\"@lALPDeREVttTpCrNA6rNA6o\"},\"status\":\"NORMAL\",\"actionType\":\"URL\",\"actionUrl\":{\"android\":\"https://open.dingtalk.com\",\"ios\":\"https://open.dingtalk.com\",\"pc\":\"https://open.dingtalk.com\"}}],\"actionDirection\":\"HORIZONTAL\"}";
        JSONObject templateObject = JSON.parseObject(templateCard);
        JSONObject header = templateObject.getJSONObject("header");
        JSONObject headerText = header.getJSONObject("text");
        headerText.put("zh_Hans", title);
        JSONArray contents = templateObject.getJSONArray("contents");
        JSONObject contentObject = contents.getJSONObject(0);
        JSONObject contentText = contentObject.getJSONObject("text");
        contentText.put("zh_Hans", date);
        JSONObject contentObject1 = contents.getJSONObject(1);
        JSONObject contentText1 = contentObject1.getJSONObject("text");
        contentText1.put("zh_Hans", address);
        System.out.println(templateObject.toJSONString());
        return templateObject.toJSONString();
    }

    private String conAnnouncementCardData(String title, String content){
        // 轻量级互动卡片消息，参考文档：https://open.dingtalk.com/document/group/lightweight-access-document-of-interactive-cards
        String templateCard = "{\"header\":{\"icon\":{\"light\":\"https://img.alicdn.com/imgextra/i1/O1CN01yIywvJ1MtNgCBowHr_!!6000000001492-2-tps-20-20.png\",\"dark\":\"https://img.alicdn.com/imgextra/i1/O1CN01yIywvJ1MtNgCBowHr_!!6000000001492-2-tps-20-20.png\"},\"text\":{\"zh_Hans\":\"\"},\"color\":{\"light\":\"#00B853\",\"dark\":\"#00B853\"}},\"contents\":[{\"text\":{\"zh_Hans\":\"\"},\"type\":\"PARAGRAPH\",\"icon\":{\"light\":\"https://img.alicdn.com/imgextra/i2/O1CN010hPrWX1W3lC8FHiZy_!!6000000002733-2-tps-20-20.png\",\"dark\":\"https://img.alicdn.com/imgextra/i2/O1CN010hPrWX1W3lC8FHiZy_!!6000000002733-2-tps-20-20.png\"}}],\"actions\":[{\"id\":\"1\",\"text\":{\"zh_Hans\":\"钉钉网站\"},\"icon\":{\"light\":\"@lALPDeREVttTpCrNA6rNA6o\"},\"status\":\"NORMAL\",\"actionType\":\"URL\",\"actionUrl\":{\"android\":\"https://open.dingtalk.com\",\"ios\":\"https://open.dingtalk.com\",\"pc\":\"https://open.dingtalk.com\"}}],\"actionDirection\":\"HORIZONTAL\"}";
        JSONObject templateObject = JSON.parseObject(templateCard);
        JSONObject header = templateObject.getJSONObject("header");
        JSONObject headerText = header.getJSONObject("text");
        headerText.put("zh_Hans", title);
        JSONArray contents = templateObject.getJSONArray("contents");
        JSONObject contentObject = contents.getJSONObject(0);
        JSONObject contentText = contentObject.getJSONObject("text");
        contentText.put("zh_Hans", content);
        System.out.println(templateObject.toJSONString());
        return templateObject.toJSONString();
    }

}
