package io.quarkiverse.it.cxf.properties;

import io.quarkus.runtime.Startup;
import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "app")
public interface AppProperties {
    String username();
    String password();
}