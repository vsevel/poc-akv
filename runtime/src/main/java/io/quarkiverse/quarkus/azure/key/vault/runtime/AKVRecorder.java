package io.quarkiverse.quarkus.azure.key.vault.runtime;

import static java.util.Collections.emptyList;

import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.ConfigSourceProvider;
import org.jboss.logging.Logger;

import io.quarkiverse.quarkus.azure.key.vault.runtime.config.AKVBootstrapConfig;
import io.quarkiverse.quarkus.azure.key.vault.runtime.config.AKVConfigSourceProvider;
import io.quarkus.arc.Arc;
import io.quarkus.arc.ArcContainer;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class AKVRecorder {

    private static final Logger log = Logger.getLogger(AKVRecorder.class);

    private static final EmptyConfigSourceProvider EMPTY = new EmptyConfigSourceProvider();

    private static class EmptyConfigSourceProvider implements ConfigSourceProvider {
        @Override
        public Iterable<ConfigSource> getConfigSources(ClassLoader classLoader) {
            return emptyList();
        }
    }

    public RuntimeValue<ConfigSourceProvider> configure(AKVBootstrapConfig akvBootstrapConfig) {
        ConfigSourceProvider configSourceProvider = EMPTY;
        if (akvBootstrapConfig.url.isPresent()) {
            ArcContainer container = Arc.container();
            // container.instance(VaultConfigHolder.class).get().setVaultBootstrapConfig(vaultBootstrapConfig);
            // container.instance(VertxVaultClient.class).get().init();
            configSourceProvider = new AKVConfigSourceProvider(akvBootstrapConfig);
        }
        return new RuntimeValue<>(configSourceProvider);
    }
}
