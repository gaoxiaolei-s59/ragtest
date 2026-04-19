package org.puregxl.site.infra.model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.puregxl.site.infra.enums.ModelCapability;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class ModelRoutingExecutor {
    private final ModelHealthStore healthStore;

}
