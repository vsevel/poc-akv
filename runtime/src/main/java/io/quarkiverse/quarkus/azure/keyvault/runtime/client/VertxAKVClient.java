package io.quarkiverse.quarkus.azure.keyvault.runtime.client;

import static io.vertx.core.http.HttpMethod.GET;
import static io.vertx.core.http.HttpMethod.POST;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PreDestroy;
import javax.inject.Singleton;

import org.jboss.logging.Logger;

import io.quarkiverse.quarkus.azure.keyvault.runtime.config.AzureKeyVaultBootstrapConfig;
import io.quarkiverse.quarkus.azure.keyvault.runtime.config.AzureKeyVaultConfigHolder;
import io.quarkus.runtime.TlsConfig;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.ext.web.client.WebClient;

@Singleton
public class VertxAKVClient {

    private static final Logger log = Logger.getLogger(VertxAKVClient.class.getName());

    private static final String OAUTH_URL = "https://login.microsoftonline.com/";
    private static final String API_VERSION = "api-version=7.1";
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer";

    private final Vertx vertx;
    private final TlsConfig tlsConfig;
    private final AzureKeyVaultConfigHolder configHolder;
    private WebClient webClient;

    public VertxAKVClient(AzureKeyVaultConfigHolder configHolder, TlsConfig tlsConfig) {
        this.configHolder = configHolder;
        this.tlsConfig = tlsConfig;
        this.vertx = Vertx.vertx();
    }

    public void init() {
        AzureKeyVaultBootstrapConfig config = configHolder.getConfig();
        this.webClient = MutinyVertxClientFactory.createHttpClient(vertx, config, tlsConfig);
    }

    public String getToken(String tenantId, String clientId, String clientSecret) {

        String body = "grant_type=client_credentials"
                + "&client_id=" + clientId
                + "&client_secret=" + clientSecret
                + "&resource=https%3A%2F%2Fvault.azure.net";

        return webClient
                .getAbs(OAUTH_URL + tenantId + "/oauth2/token")
                .method(POST)
                .sendBuffer(Buffer.buffer(body))
                .await().atMost(getRequestTimeout())
                .bodyAsJsonObject()
                .getString("access_token");
    }

    public Map<String, String> getSecrets(String token) {

        Map<String, String> secrets = new HashMap<>();

        webClient
                .getAbs(getUrl() + "secrets/?" + API_VERSION)
                .method(GET)
                .putHeader(AUTHORIZATION, getAuthorization(token))
                .send()
                .await().atMost(getRequestTimeout())
                .bodyAsJsonObject()
                .getJsonArray("value")
                .stream()
                .map(element -> ((JsonObject) element).getString("id"))
                .forEach(secretUrl -> secrets.put(getKey(secretUrl), getValue(token, secretUrl)));

        return secrets;
    }

    private String getUrl() {
        String url = configHolder.getConfig().url.get();
        if (!url.endsWith("/")) {
            url += "/";
        }
        return url;
    }

    private String getAuthorization(String token) {
        return BEARER + " " + token;
    }

    private String getKey(String secretUrl) {
        return secretUrl.substring(secretUrl.lastIndexOf("/") + 1);
    }

    private String getValue(String token, String secretUrl) {
        return webClient
                .getAbs(secretUrl + "/?" + API_VERSION)
                .method(GET)
                .putHeader(AUTHORIZATION, getAuthorization(token))
                .send()
                .await().atMost(getRequestTimeout())
                .bodyAsJsonObject()
                .getString("value");
    }

    private Duration getRequestTimeout() {
        return configHolder.getConfig().readTimeout;
    }

    @PreDestroy
    public void close() {
        try {
            if (webClient != null) {
                webClient.close();
            }
        } finally {
            vertx.closeAndAwait();
        }
    }
}
