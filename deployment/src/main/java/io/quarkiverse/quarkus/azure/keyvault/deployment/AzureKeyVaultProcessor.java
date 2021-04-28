package io.quarkiverse.quarkus.azure.keyvault.deployment;

import org.jboss.logging.Logger;

import io.quarkiverse.quarkus.azure.keyvault.runtime.AzureKeyVaultRecorder;
import io.quarkiverse.quarkus.azure.keyvault.runtime.client.AzureKeyVaultVertxClient;
import io.quarkiverse.quarkus.azure.keyvault.runtime.config.AzureKeyVaultBootstrapConfig;
import io.quarkiverse.quarkus.azure.keyvault.runtime.config.AzureKeyVaultConfigHolder;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.Feature;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.ExtensionSslNativeSupportBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.RunTimeConfigurationSourceValueBuildItem;
import io.quarkus.deployment.builditem.SslNativeConfigBuildItem;

class AzureKeyVaultProcessor {

    private static final String FEATURE = "azure-key-vault";

    private static final Logger log = Logger.getLogger(AzureKeyVaultProcessor.class);

    @BuildStep
    void build(
            BuildProducer<FeatureBuildItem> feature,
            SslNativeConfigBuildItem sslNativeConfig,
            BuildProducer<ExtensionSslNativeSupportBuildItem> sslNativeSupport) {

        feature.produce(new FeatureBuildItem(Feature.VAULT));
        sslNativeSupport.produce(new ExtensionSslNativeSupportBuildItem(Feature.VAULT));
    }

    @BuildStep
    AdditionalBeanBuildItem registerAdditionalBeans() {
        return new AdditionalBeanBuildItem.Builder()
                .setUnremovable()
                .addBeanClass(AzureKeyVaultConfigHolder.class)
                .addBeanClass(AzureKeyVaultVertxClient.class)
                .build();
    }

    @Record(ExecutionTime.RUNTIME_INIT)
    @BuildStep
    RunTimeConfigurationSourceValueBuildItem init(AzureKeyVaultRecorder recorder,
            AzureKeyVaultBootstrapConfig config) {
        return new RunTimeConfigurationSourceValueBuildItem(recorder.configure(config));
    }
}
