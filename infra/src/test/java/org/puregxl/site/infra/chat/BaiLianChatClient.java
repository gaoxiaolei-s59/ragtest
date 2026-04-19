package org.puregxl.site.infra.chat;

import okhttp3.OkHttpClient;
import org.puregxl.site.infra.convention.ChatRequest;
import org.puregxl.site.infra.model.ModelTarget;
import org.springframework.stereotype.Service;

@Service
public class BaiLianChatClient extends AbstractChatClient{

    protected BaiLianChatClient(OkHttpClient httpClient) {
        super(httpClient);
    }

    @Override
    public String provider() {
        return "";
    }

    @Override
    public String chat(ChatRequest request, ModelTarget target) {
        return doChat(request, target);
    }
}
