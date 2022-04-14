package com.dingtalk.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Configuration
public class AppConfig {

    SimpleDateFormat format = new SimpleDateFormat("HH点mm分");

    @Value("${app.app_key}")
    private String appKey;

    @Value("${app.app_secret}")
    private String appSecret;

    @Value("${app.corp_id}")
    private String corpId;

    @Value("${app.agent_id}")
    private Long agentId;

    private Map<String, String> conversationIdMap = new HashMap<>();

    private Map<String, String> logMap = new HashMap<>();

    private String robotCode;

    private boolean cardRegister;

    public void putConversation(String conversationId, String conversationTitle) {
        conversationIdMap.put(conversationId, conversationTitle);
    }

    public void putLog(long timeMs, String logContent) {
        Date date = new Date(timeMs);
        String time = this.format.format(date);
        logMap.put(time, logContent);
    }
}