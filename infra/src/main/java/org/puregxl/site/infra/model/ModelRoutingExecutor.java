package org.puregxl.site.infra.model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.puregxl.site.infra.enums.ModelCapability;
import org.puregxl.site.infra.exception.RemoteException;
import org.puregxl.site.infra.exception.errorcode.BaseErrorCode;
import org.springframework.context.annotation.Configuration;
import java.util.List;
import java.util.function.Function;

/**
 * 模型路由调度器 - 负责在多个模型候选者之间调度执行
 */
@RequiredArgsConstructor
@Configuration
@Slf4j
public class ModelRoutingExecutor {
    private final ModelHealthStore healthStore;

    public <C, T> T executeWithFallback(
            ModelCapability capability,
            List<ModelTarget> targets,
            Function<ModelTarget, C> clientResolver,
            ModelCaller<C, T> caller) {
        String label = capability.getDisplayName();
        if (targets == null || targets.isEmpty()) {
            throw new RemoteException("No " + label + " model candidates available");
        }

        Throwable last = null;
        for (ModelTarget target : targets) {
            C client = clientResolver.apply(target);
            if (client == null) {
                log.warn("{} provider client missing: provider={}, modelId={}", label, target.candidate().getProvider(), target.id());
                continue;
            }
            if (!healthStore.allowCall(target.id())) {
                continue;
            }

            try {
                T response = caller.call(client, target);
                healthStore.markSuccess(target.id());
                return response;
            } catch (Exception e) {
                last = e;
                healthStore.markFailure(target.id());
                log.warn("{} model failed, fallback to next. modelId={}, provider={}", label, target.id(), target.candidate().getProvider(), e);
            }
        }

        throw new RemoteException(
                "All " + label + " model candidates failed: " + (last == null ? "unknown" : last.getMessage()),
                last,
                BaseErrorCode.REMOTE_ERROR
        );
    }
}
