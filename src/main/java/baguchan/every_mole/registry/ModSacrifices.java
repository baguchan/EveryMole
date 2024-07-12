package baguchan.every_mole.registry;

import baguchan.every_mole.EveryMole;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class ModSacrifices {
    public static final DeferredRegister<Sacrifice> SACRIFICE_DEFERRED_REGISTER = DeferredRegister.create(Sacrifice.REGISTRY_KEY,
            EveryMole.MODID);
    public static final Supplier<IForgeRegistry<Sacrifice>> CONVERT_REGISTRY = SACRIFICE_DEFERRED_REGISTER.makeRegistry(RegistryBuilder::new);
    public static ResourceKey<Sacrifice> key(ResourceLocation name) {
        return ResourceKey.create(Sacrifice.REGISTRY_KEY, name);
    }

}