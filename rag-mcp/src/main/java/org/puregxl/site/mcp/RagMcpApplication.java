package org.puregxl.site.mcp;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RagMcpApplication {

    public static void main(String[] args) {
        SpringApplication.run(RagMcpApplication.class, args);
    }

    @Bean
    ToolCallbackProvider ragToolCallbackProvider(RagToolService ragToolService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(ragToolService)
                .build();
    }
}
