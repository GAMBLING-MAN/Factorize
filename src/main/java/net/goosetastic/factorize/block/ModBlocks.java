package net.goosetastic.factorize.block;

import net.goosetastic.factorize.Factorize;
import net.goosetastic.factorize.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Factorize.MODID);

    public static final RegistryObject<Block> BLUEPRINT_MAKER = registerBlock("blueprint_maker",
            () -> new Block(defaultProperties()),
            null);


    public static BlockBehaviour.Properties defaultProperties() {
        return BlockBehaviour.Properties.of(Material.STONE).strength(6f).requiresCorrectToolForDrops();
    }
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
        if (tab == null) {
            return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), ModItems.defaultProperties()));
        } else {
            return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
        }
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
