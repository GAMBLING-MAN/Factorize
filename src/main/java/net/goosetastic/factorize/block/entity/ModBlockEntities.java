package net.goosetastic.factorize.block.entity;

import net.goosetastic.factorize.Factorize;
import net.goosetastic.factorize.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Factorize.MODID);

    public static final RegistryObject<BlockEntityType<BlueprintMakerBlockEntity>> BLUEPRINT_MAKER =
            BLOCK_ENTITIES.register("blueprint_maker", () ->
                    BlockEntityType.Builder.of(BlueprintMakerBlockEntity::new,
                            ModBlocks.BLUEPRINT_MAKER.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
