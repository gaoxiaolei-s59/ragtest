package org.puregxl.site.infra.model;

import lombok.RequiredArgsConstructor;
import org.puregxl.site.infra.config.AIModelProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 模型健康状态存储器
 * 用于管理和跟踪各个 AI 模型的健康状况，实现断路器模式
 */
@Component
@RequiredArgsConstructor
public class ModelHealthStore {

    private final AIModelProperties properties;

    private final Map<String, ModelHealth> healthMap = new ConcurrentHashMap<>();

    /**
     * 是否可以访问
     * @return
     */
    public boolean isUnavailable(String id) {
        ModelHealth health = healthMap.get(id);
        if (health == null) {
            return false;
        }
        if (health.state == State.OPEN && health.openUntil > System.currentTimeMillis()) {
            return true;
        }
        return health.state == State.HALF_OPEN && health.halfOpenInFlight;
    }

    public boolean allowCall(String id) {
        ModelHealth modelHealth = healthMap.get(id);
        if (modelHealth == null) {
            return false;
        }

        long now = System.currentTimeMillis();
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        healthMap.compute(id, (k, v) ->{
            if (v == null) {
                v = new ModelHealth();
            }

            if (v.state == State.OPEN) {
                if (v.openUntil > now) {
                    return v;
                }
                v.state = State.HALF_OPEN;
                v.halfOpenInFlight = true;
                atomicBoolean.set(true);
                return v;
            }

            if (v.state == State.HALF_OPEN) {
                if (v.halfOpenInFlight) {
                    return v;
                }
                v.halfOpenInFlight = true;
                atomicBoolean.set(true);
                return v;
            }

            atomicBoolean.set(true);
            return v;
        });

        return atomicBoolean.get();
    }

    /**
     * 成功
     * @param id
     */
    public void markSuccess(String id) {
        if (id == null) {
            return;
        }
        healthMap.compute(id, (k, v) ->{
            if (v == null) {
                return new ModelHealth();
            }
            v.state = State.CLOSED;
            v.halfOpenInFlight = false;
            v.openUntil = 0L;
            v.consecutiveFailures = 0;
            return v;
        });
    }

    /**
     * 失败
     * @param id
     */
    public void markFailure(String id) {
        if (id == null) {
            return;
        }
        healthMap.compute(id, (k, v) ->{
            if (v == null) {
                v = new ModelHealth();
            }

            if (v.state == State.HALF_OPEN) {
                v.state = State.OPEN;
                v.openUntil = System.currentTimeMillis() + properties.getSelection().getOpenDurationMs();
                v.consecutiveFailures = 0;
                v.halfOpenInFlight = false;
                return v;
            }

            v.consecutiveFailures++;
            if (v.consecutiveFailures >= properties.getSelection().getFailureThreshold()) {
                v.state = State.OPEN;
                v.openUntil = System.currentTimeMillis() + properties.getSelection().getOpenDurationMs();
                v.consecutiveFailures = 0;
            }
            return v;
        });
    }



    private static class ModelHealth{
        private int consecutiveFailures; //连续失败次数
        private long openUntil; //截断时间
        private boolean halfOpenInFlight; //是否有探测请求
        private State state; //状态位

        private ModelHealth() {
            this.consecutiveFailures = 0;
            this.openUntil = 0L;
            this.halfOpenInFlight = false;
            this.state = State.CLOSED;
        }
    }

    /**
     * 定义了熔断器的状态
     */
    private enum State {
        CLOSED,
        HALF_OPEN,
        OPEN;
    }
}
