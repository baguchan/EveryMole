package baguchan.every_mole.registry;

import baguchan.every_mole.EveryMole;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public class Sacrifice {
    public static final Codec<Sacrifice> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("target").forGetter(instance2 -> instance2.getEntityType()),
                    BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("convert").forGetter(instance2 -> instance2.getConvertEntityType()))
            .apply(instance, Sacrifice::new)
    );

    public static final ResourceKey<Registry<Sacrifice>> REGISTRY_KEY = ResourceKey
            .createRegistryKey(new ResourceLocation(EveryMole.MODID, "sacrifice"));
    private final EntityType<?> entityType;
    private final EntityType<?> convertType;

    public Sacrifice(EntityType<?> entityType, EntityType<?> convertType) {
        this.entityType = entityType;
        this.convertType = convertType;
    }

    public EntityType<?> getEntityType() {
        return entityType;
    }

    public EntityType<?> getConvertEntityType() {
        return convertType;
    }
}