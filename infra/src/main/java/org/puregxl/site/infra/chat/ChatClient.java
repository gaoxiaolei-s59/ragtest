package org.puregxl.site.infra.chat;

import org.puregxl.site.infra.convention.ChatRequest;
import org.puregxl.site.infra.enums.ModelProvider;

/**
 * Chat接口
 */
public interface ChatClient {

    /**
     * 获取服务提供商名称
     *
     * @return 服务提供商标识：{@link ModelProvider}
     */
    String provider();

    /**
     * 对话(同步聊天)
     * @return
     */
    String chat(ChatRequest request);




}
