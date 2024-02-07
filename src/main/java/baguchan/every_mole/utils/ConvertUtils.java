package baguchan.every_mole.utils;

import baguchan.every_mole.registry.Sacrifice;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import java.util.Optional;

public class ConvertUtils {
    @Nullable
    public static Sacrifice getSacrifice(EntityType<?> entityType) {
        Optional<Sacrifice> sacrifice = registryAccess().registryOrThrow(Sacrifice.REGISTRY_KEY).stream().filter(sacrifice2 -> entityType == sacrifice2.getEntityType()).findFirst();
        return sacrifice.orElse(null);
    }

    public static RegistryAccess registryAccess() {
        if (EffectiveSide.get().isServer()) {
            return ServerLifecycleHooks.getCurrentServer().registryAccess();
        }
        return Minecraft.getInstance().getConnection().registryAccess();
    }
}
