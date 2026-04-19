package org.puregxl.site.infra.convention;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ChatRequest {

    /**
     * 上下文
     */
    @Builder.Default
    private List<ChatMessage> messages = new ArrayList<>();

    /**
     * 采样温度参数，取值通常为 0～2
     */
    private Double temperature;

    /**
     * topP
     */
    private Double topP;


    /**
     * Top-K 采样参数
     */
    private Integer topK;

    /**
     * 最大输出token
     */
    private Integer maxTokens;


    /**
     * 是否采用工具调用
     */
    private Boolean enableTools;

    /**
     * 思考模式
     */
    private Boolean thinking;


}
