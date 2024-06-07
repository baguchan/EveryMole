package baguchan.every_mole.registry;

import baguchan.every_mole.EveryMole;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.Optional;

public record Sacrifice(EntityType<?> entityType, EntityType<?> convertType, Optional<CompoundTag> targetTag,
                        Optional<CompoundTag> convertTag) {
    public static final Codec<Sacrifice> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("target").forGetter(instance2 -> instance2.getEntityType()),
                    BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("convert").forGetter(instance2 -> instance2.getConvertEntityType()),
                    CompoundTag.CODEC.optionalFieldOf("target_nbt").forGetter(instance2 -> instance2.targetTag()),
                    CompoundTag.CODEC.optionalFieldOf("convert_nbt").forGetter(instance2 -> instance2.convertTag()))
            .apply(instance, Sacrifice::new)
    );

    public static final ResourceKey<Registry<Sacrifice>> REGISTRY_KEY = ResourceKey
            .createRegistryKey(new ResourceLocation(EveryMole.MODID, "sacrifice"));


    public EntityType<?> getEntityType() {
        return entityType;
    }

    public EntityType<?> getConvertEntityType() {
        return convertType;
    }
}