package org.puregxl.site.infra.chat;

import org.puregxl.site.infra.convention.ChatRequest;

/**
 *  LLM 服务实现类
 */
public class RoutingLLMService implements LLMService{


    @Override
    public String chat(ChatRequest chatRequest) {
        return "";
    }

    @Override
    public String chat(String userPrompt) {
        return LLMService.super.chat(userPrompt);
    }

    @Override
    public String chat(ChatRequest request, String modelId) {
        return LLMService.super.chat(request, modelId);
    }
}
