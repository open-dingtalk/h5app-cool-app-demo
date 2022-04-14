package com.dingtalk.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Configuration
public class AppConfig {

    @Value("${app.app_key}")
    private String appKey;

    @Value("${app.app_secret}")
    private String appSecret;

    @Value("${app.corp_id}")
    private String corpId;

    @Value("${app.agent_id}")
    private Long agentId;

    private Map<String, String> conversationIdMap = new HashMap<>();

    private String robotCode;

    public void putConversation(String conversationId, String conversationTitle) {
        conversationIdMap.put(conversationId, conversationTitle);
    }
}