package io.quarkiverse.quarkus.azure.keyvault.runtime.client;

import org.jboss.logging.Logger;

import io.quarkiverse.quarkus.azure.keyvault.runtime.config.AzureKeyVaultBootstrapConfig;
import io.quarkus.runtime.TlsConfig;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;

public class MutinyVertxClientFactory {

    private static final Logger log = Logger.getLogger(MutinyVertxClientFactory.class.getName());

    public static WebClient createHttpClient(Vertx vertx, AzureKeyVaultBootstrapConfig config,
            TlsConfig tlsConfig) {

        WebClientOptions options = new WebClientOptions()
                .setConnectTimeout((int) config.connectTimeout.toMillis())
                .setIdleTimeout((int) config.readTimeout.getSeconds());

        boolean trustAll = config.tls.skipVerify.orElseGet(() -> tlsConfig.trustAll);
        if (trustAll) {
            skipVerify(options);
        } else {
            config.tls.caCert.ifPresent(s -> cacert(options, s));
        }

        return WebClient.create(vertx, options);
    }

    private static void cacert(WebClientOptions options, String cacert) {
        log.debug("configure tls with " + cacert);
        options.setTrustOptions(new PemTrustOptions().addCertPath(cacert));
    }

    private static void skipVerify(WebClientOptions options) {
        log.debug("configure tls with skip-verify");
        options.setTrustAll(true);
        options.setVerifyHost(false);
    }
}
