package org.puregxl.site.infra.chat;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.puregxl.site.infra.convention.ChatRequest;
import org.puregxl.site.infra.enums.ModelCapability;
import org.puregxl.site.infra.model.ModelHealthStore;
import org.puregxl.site.infra.model.ModelRoutingExecutor;
import org.puregxl.site.infra.model.ModelSelector;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *  LLM 服务实现类
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Primary
public class RoutingLLMService implements LLMService{

    private final ModelSelector selector;
    private final ModelHealthStore healthStore;
    private final ModelRoutingExecutor executor;
    private final Map<String, ChatClient> clientsByProvider;

    public RoutingLLMService(
            ModelSelector selector,
            ModelHealthStore healthStore,
            ModelRoutingExecutor executor,
            List<ChatClient> clients) {
        this.selector = selector;
        this.healthStore = healthStore;
        this.executor = executor;
        this.clientsByProvider = clients.stream()
                .collect(Collectors.toMap(ChatClient::provider, Function.identity()));
    }

    @Override
    public String chat(ChatRequest request) {
        return executor.executeWithFallback(
                ModelCapability.CHAT,
                selector.selectChatCandidates(Boolean.TRUE.equals(request.getThinking())),
                target -> clientsByProvider.get(target.candidate().getProvider()),
                (client, target) -> client.chat(request, target)
        );
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
