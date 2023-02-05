package net.goosetastic.factorize.block.entity;

import com.mojang.serialization.Decoder;
import net.goosetastic.factorize.etc.RecipeResult;
import net.goosetastic.factorize.item.ModItems;
import net.goosetastic.factorize.screen.BlueprintMakerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("removal")
public class BlueprintMakerBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(5) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> LazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 80;

    public BlueprintMakerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BLUEPRINT_MAKER.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> BlueprintMakerBlockEntity.this.progress;
                    case 1 -> BlueprintMakerBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                  case 0 -> BlueprintMakerBlockEntity.this.progress = value;
                  case 1 -> BlueprintMakerBlockEntity.this.maxProgress = value;
                };
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Blueprint Table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new BlueprintMakerMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return LazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        LazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        LazyItemHandler.invalidate();
    }

    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("Inventory", itemHandler.serializeNBT());

        super.saveAdditional(nbt);
    }

    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("Inventory"));
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, BlueprintMakerBlockEntity pEntity) {
       if(level.isClientSide()) {
           return;
       }

       if (getRecipe(pEntity).hasRecipe) {
           pEntity.progress++;
           setChanged(level,blockPos,blockState);

           if (pEntity.progress >= pEntity.maxProgress) {
               CraftItem(pEntity);
           }
       } else {
           pEntity.resetProgress();
           setChanged(level,blockPos,blockState);
       }
        // https://youtu.be/jo0BTisGpJk?t=812
        // https://youtu.be/jo0BTisGpJk?t=2313
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static void CraftItem(BlueprintMakerBlockEntity pEntity) {
        RecipeResult recipe = getRecipe(pEntity);
        if (recipe.hasRecipe) {
            pEntity.itemHandler.extractItem(1,1,false);
            pEntity.itemHandler.setStackInSlot(2,recipe.result);
        }
    }

    private static RecipeResult getRecipe(BlueprintMakerBlockEntity entity) {
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }
        boolean hasRecipe = entity.itemHandler.getStackInSlot(1).getItem() == Items.PAPER && canOutput(inventory);

        ItemStack result;
        if (!hasRecipe) {
            result = null;
        } else {
            result = new ItemStack(ModItems.BLUEPRINT.get(),1);
        }

        return new RecipeResult(hasRecipe, result);
    }

    private static boolean canOutput(SimpleContainer inventory) {
        return inventory.getItem(2).isEmpty();
    }


}
