package work.lclpnet.mmocontent.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import javax.annotation.Nonnull;

public class Env {

    private static EnvType currentEnv = null;

    @Nonnull
    public static EnvType currentEnv() {
        return currentEnv == null ? currentEnv = FabricLoader.getInstance().getEnvironmentType() : currentEnv;
    }

    public static boolean isClient() {
        return currentEnv() == EnvType.CLIENT;
    }
}
