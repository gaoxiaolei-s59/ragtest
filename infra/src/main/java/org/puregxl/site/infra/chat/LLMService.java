package org.puregxl.site.infra.chat;

import org.puregxl.site.infra.convention.ChatMessage;
import org.puregxl.site.infra.convention.ChatRequest;

import java.util.List;

/**
 * 通用大语言模型（LLM）访问接口
 */
public interface LLMService {

    /**
     * 默认方法
     * @return
     */
    default String chat(String userPrompt) {
        ChatRequest chatRequest = ChatRequest.builder()
                .messages(List.of(new ChatMessage(ChatMessage.Role.USER, userPrompt)))
                .build();
        return chat(chatRequest);
    }

    /**
     * 大模型调用通用方法
     * @param chatRequest
     * @return
     */
    String chat(ChatRequest chatRequest);

    /**
     * 同步调用（指定模型）
     * <p>
     **/
    default String chat(ChatRequest request, String modelId) {
        return chat(request);
    }
}
