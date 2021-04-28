package io.quarkiverse.quarkus.azure.keyvault.runtime.config;

import static io.quarkiverse.quarkus.azure.keyvault.runtime.config.VaultCacheEntry.tryReturnLastKnownValue;
import static java.util.Collections.emptyMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import org.eclipse.microprofile.config.spi.ConfigSource;
import org.jboss.logging.Logger;

import io.quarkiverse.quarkus.azure.keyvault.runtime.client.VertxAKVClient;
import io.quarkus.arc.Arc;
import io.smallrye.mutiny.infrastructure.Infrastructure;

public class AzureKeyVaultConfigSource implements ConfigSource {

    private static final Logger log = Logger.getLogger(AzureKeyVaultConfigSource.class);

    private AzureKeyVaultBootstrapConfig config;

    private AtomicReference<VaultCacheEntry<Map<String, String>>> cache = new AtomicReference<>(null);

    public AzureKeyVaultConfigSource(AzureKeyVaultBootstrapConfig config) {
        this.config = config;
        log.debug("created azure key vault with config url=" + config.url.get());
    }

    @Override
    public String getName() {
        return AzureKeyVaultBootstrapConfig.NAME;
    }

    @Override
    public int getOrdinal() {
        return config.configOrdinal;
    }

    /**
     * always return an empty map to protect from accidental properties logging
     *
     * @return empty map
     */
    @Override
    public Map<String, String> getProperties() {
        return emptyMap();
    }

    @Override
    public Set<String> getPropertyNames() {
        return Collections.emptySet();
    }

    @Override
    public String getValue(String propertyName) {
        return getSecrets().get(propertyName);
    }

    private Map<String, String> getSecrets() {

        VaultCacheEntry<Map<String, String>> cacheEntry = cache.get();
        if (cacheEntry != null && cacheEntry.youngerThan(config.secretConfigCachePeriod)) {
            return cacheEntry.getValue();
        }

        if (!Infrastructure.canCallerThreadBeBlocked()) {
            // running in a non blocking thread, best effort to return cached values if any
            return cacheEntry != null ? cacheEntry.getValue() : Collections.emptyMap();
        }

        Map<String, String> properties = new HashMap<>();
        try {
            fetchSecrets(properties);
            log.debug("loaded " + properties.size() + " properties from azure key vault");
        } catch (RuntimeException e) {
            return tryReturnLastKnownValue(e, cacheEntry);
        }

        cache.set(new VaultCacheEntry(properties));
        return properties;
    }

    private void fetchSecrets(Map<String, String> properties) {

        String clientSecret = config.clientSecret.orElseThrow(missingProperty("client-secret"));
        String clientId = config.clientId.orElseThrow(missingProperty("client-id"));
        String tenantId = config.tenantId.orElseThrow(missingProperty("tenant-id"));

        VertxAKVClient client = getClient();
        String token = client.getToken(tenantId, clientId, clientSecret);
        properties.putAll(mapPrefix(client.getSecrets(token)));
    }

    private Map<String, String> mapPrefix(Map<String, String> secrets) {
        Optional<String> prefix = config.prefix;
        if (prefix.isPresent()) {
            Map<String, String> result = new HashMap<>(secrets.size());
            secrets.forEach((k, v) -> result.put(prefix.get() + "." + k, v));
            return result;
        } else {
            return secrets;
        }
    }

    private VertxAKVClient getClient() {
        return Arc.container().instance(VertxAKVClient.class).get();
    }

    private Supplier<RuntimeException> missingProperty(String propertyName) {
        return () -> new RuntimeException(propertyName + " property is required");
    }
}
