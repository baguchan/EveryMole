package baguchan.every_mole.utils;

import baguchan.every_mole.registry.Sacrifice;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import java.util.Optional;

public class ConvertUtils {
    @Nullable
    public static Sacrifice getSacrifice(EntityType<?> entityType) {
        Optional<Sacrifice> sacrifice = registryAccess().registryOrThrow(Sacrifice.REGISTRY_KEY).stream().filter(sacrifice2 -> entityType == sacrifice2.getEntityType()).limit(1).findFirst();
        return sacrifice.orElse(null);
    }

    public static RegistryAccess registryAccess() {
        if (EffectiveSide.get().isServer()) {
            return ServerLifecycleHooks.getCurrentServer().registryAccess();
        }
        return Minecraft.getInstance().getConnection().registryAccess();
    }

    public static CompoundTag getData(Entity entity) {
        return NbtPredicate.getEntityTagToCompare(entity);
    }

    public static void mergeData(Entity entity, CompoundTag compoundTag) {
        CompoundTag compoundTag1 = NbtPredicate.getEntityTagToCompare(entity).copy().merge(compoundTag);
        entity.load(compoundTag1);
    }

    public static boolean findTagMatcher(
            CompoundTag tag,
            CompoundTag target
    ) {
        return tag.getAsString().contains(target.getAsString().replace("{", "").replace("}", "").replace("\\", ""));
    }

}
