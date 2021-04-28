package io.quarkiverse.quarkus.azure.key.vault.deployment;

import io.quarkiverse.quarkus.azure.key.vault.runtime.AKVRecorder;
import io.quarkiverse.quarkus.azure.key.vault.runtime.config.AKVBootstrapConfig;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.RunTimeConfigurationSourceValueBuildItem;

class AKVProcessor {

    private static final String FEATURE = "azure-key-vault";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem registerAdditionalBeans() {
        return new AdditionalBeanBuildItem.Builder()
                .setUnremovable()
                //                .addBeanClass()
                .build();
    }

    @Record(ExecutionTime.RUNTIME_INIT)
    @BuildStep
    RunTimeConfigurationSourceValueBuildItem init(AKVRecorder recorder, AKVBootstrapConfig akvBootstrapConfig) {
        return new RunTimeConfigurationSourceValueBuildItem(recorder.configure(akvBootstrapConfig));
    }
}
