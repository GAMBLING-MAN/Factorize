package net.goosetastic.factorize.item;

import net.goosetastic.factorize.Factorize;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Factorize.MODID);

    public static final RegistryObject<Item> CARBATTERY = ITEMS.register("car_battery",
            () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> RAMSTICK = ITEMS.register("stick_of_ram",
            () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> TECHCHIP = ITEMS.register("tech_chip",
            () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> TECHCARD = ITEMS.register("tech_card",
            () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> CIRCUITBOARD = ITEMS.register("circuit_board",
            () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> ADVANCEDPINSYSTEM = ITEMS.register("advanced_pin_system",
            () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> SILICONBOARD = ITEMS.register("silicon_board",
            () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> AABATTERY = ITEMS.register("double_a_battery",
            () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> COPPERWIRE = ITEMS.register("copper_wire",
            () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> CARBONPLATE = ITEMS.register("carbon_plate",
            () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> CARBON = ITEMS.register("carbon",
            () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> SILICON = ITEMS.register("silicon",
            () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> PLASTICPOLYMER = ITEMS.register("plastic_polymer",
            () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> ADVANCEDPLASTIC = ITEMS.register("advanced_plastic",
            () -> new Item(defaultProperties()));
    public static final RegistryObject<Item> GRAPHENE = ITEMS.register("graphene",
            () -> new Item(defaultProperties()));

    public static Item.Properties defaultProperties() {
        return new Item.Properties().tab(Factorize.TAB);
    }
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
