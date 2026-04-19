package org.puregxl.site.infra.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.puregxl.site.infra.config.AIModelProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelTarget {
    private String id;

    private AIModelProperties.ModelCandidate candidate;

    private AIModelProperties.ProviderConfig provider;


}
