package baguchan.every_mole;

import baguchan.every_mole.registry.Sacrifice;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DataPackRegistryEvent;
import net.minecraftforge.resource.PathPackResources;

import java.nio.file.Path;
import java.util.function.UnaryOperator;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EveryMole.MODID)
public class EveryMole
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "every_mole";
    public EveryMole()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::packSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void packSetup(AddPackFindersEvent event) {
        // Resource Packs
        this.setupVannilaPack(event);
    }

    static PackSource create(final UnaryOperator<Component> decorator, final boolean shouldAddAutomatically) {
        return new PackSource() {
            public Component decorate(Component component) {
                return decorator.apply(component);
            }

            public boolean shouldAddAutomatically() {
                return shouldAddAutomatically;
            }
        };
    }

    private void setupVannilaPack(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.SERVER_DATA) {
            Path resourcePath = ModList.get().getModFileById(MODID).getFile().findResource("pack/vanilla_sacrifice");
            PathPackResources pack = new net.minecraftforge.resource.PathPackResources(ModList.get().getModFileById(MODID).getFile().getFileName() + ":" + resourcePath, true, resourcePath);
            PackMetadataSection metadata = new PackMetadataSection(Component.translatable("pack.every_mole.vanilla_sacrifice.description"), SharedConstants.getCurrentVersion().getPackVersion(PackType.SERVER_DATA));
            event.addRepositorySource((source) ->
                    source.accept(Pack.create(
                            "builtin/every_mole_vanilla_sacrifice",
                            Component.translatable("pack.every_mole.vanilla_sacrifice.title"),
                            false,
                            (string) -> pack,
                            new Pack.Info(metadata.getDescription(), metadata.getPackFormat(PackType.SERVER_DATA), metadata.getPackFormat(PackType.CLIENT_RESOURCES), FeatureFlagSet.of(), pack.isHidden()),
                            PackType.SERVER_DATA,
                            Pack.Position.TOP,
                            false,
                            create((name) -> Component.translatable("pack.nameAndSource", name, Component.translatable("pack.source.builtin")), false))
                    )
            );
        }
    }

    private void commonSetup(final DataPackRegistryEvent.NewRegistry event)
    {
        event.dataPackRegistry(Sacrifice.REGISTRY_KEY, Sacrifice.CODEC, Sacrifice.CODEC);
    }
}
