package appeng.core;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.ConfigHolder;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.format.ConfigFormats;
import org.jetbrains.annotations.ApiStatus;

@Config(id = AppEng.MOD_ID)
public class AEConfig {
    @Configurable
    public ClientConfig client = new ClientConfig();
    @Configurable
    public CommonConfig common = new CommonConfig();

    public static AEConfig instance;

    // Default Energy Conversion Rates
    private static final double DEFAULT_FE_EXCHANGE = 0.5;

    private static final Object LOCK = new Object();
    @ApiStatus.Internal
    public static ConfigHolder<AEConfig> internalInstance;

    public static void init() {
        synchronized (LOCK) {
            if (instance == null || internalInstance == null) {
                internalInstance = Configuration.registerConfig(AEConfig.class, ConfigFormats.YAML);
                instance = internalInstance.getConfigInstance();
            }
        }
    }

    public static AEConfig getInstance() {
        init();
        return instance;
    }

    @Config(id = AppEng.MOD_ID, filename = "ae2.client.toml")
    public static class ClientConfig {

    }

    @Config(id = AppEng.MOD_ID, filename = "ae2.common.toml")
    public static class CommonConfig {

    }
}
