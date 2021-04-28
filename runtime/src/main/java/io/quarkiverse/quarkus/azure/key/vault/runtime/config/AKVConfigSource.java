package io.quarkiverse.quarkus.azure.key.vault.runtime.config;

import static java.util.Collections.emptyMap;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.eclipse.microprofile.config.spi.ConfigSource;
import org.jboss.logging.Logger;

import com.azure.core.exception.ResourceNotFoundException;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;

public class AKVConfigSource implements ConfigSource {

    private static final Logger log = Logger.getLogger(AKVConfigSource.class);

    private AKVBootstrapConfig akvBootstrapConfig;

    public AKVConfigSource(AKVBootstrapConfig akvBootstrapConfig) {
        this.akvBootstrapConfig = akvBootstrapConfig;
    }

    @Override
    public String getName() {
        return AKVBootstrapConfig.NAME;
    }

    @Override
    public int getOrdinal() {
        return akvBootstrapConfig.configOrdinal;
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
        if (akvBootstrapConfig.url.isPresent()) {

            if (propertyName.contains(".")) {
                return null;
            }

            String clientSecret = akvBootstrapConfig.clientSecret.orElseThrow(missingProperty("client-secret"));
            String clientId = akvBootstrapConfig.clientId.orElseThrow(missingProperty("client-id"));
            String tenantId = akvBootstrapConfig.tenantId.orElseThrow(missingProperty("tenant-id"));

            SecretClient secretClient = new SecretClientBuilder()
                    .vaultUrl(akvBootstrapConfig.url.get())
                    .credential(new ClientSecretCredentialBuilder()
                            .clientSecret(clientSecret)
                            .clientId(clientId)
                            .tenantId(tenantId)
                            .build())
                    .buildClient();

            try {
                KeyVaultSecret secret = secretClient.getSecret(propertyName);
                String value = secret.getValue();
                log.info("received secret " + value + " for property " + propertyName);
                return value;
            } catch (ResourceNotFoundException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    private Supplier<RuntimeException> missingProperty(String propertyName) {
        return () -> new RuntimeException(propertyName + " property is required if url is set");
    }
}
