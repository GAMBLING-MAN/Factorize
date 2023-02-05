package net.goosetastic.factorize.block.custom;

import net.goosetastic.factorize.block.entity.BlueprintMakerBlockEntity;
import net.goosetastic.factorize.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class BlueprintMaker extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BlueprintMaker(Properties properties) {
        super(properties);
    }

    // life is pain i hate myself
    private static VoxelShape getVoxelShape() {
        VoxelShape shape = Shapes.join(Block.box(2, 0, 2, 4, 14, 4),
                Block.box(12, 0, 2, 14, 14, 4),
                BooleanOp.OR);
        shape = Shapes.join(shape,
                Block.box(4, 12, 2, 12, 14, 4),
                BooleanOp.OR);
        shape = Shapes.join(shape,
                Block.box(4, 12, 12, 12, 14, 14),
                BooleanOp.OR);
        shape = Shapes.join(shape,
                Block.box(2, 12, 4, 4, 14, 12),
                BooleanOp.OR);
        shape = Shapes.join(shape,
                Block.box(12, 12, 4, 14, 14, 12),
                BooleanOp.OR);
        shape = Shapes.join(shape,
                Block.box(0, 14, 0, 16, 16, 16),
                BooleanOp.OR);
        shape = Shapes.join(shape,
                Block.box(12, 0, 12, 14, 14, 14),
                BooleanOp.OR);
        shape = Shapes.join(shape,
                Block.box(2, 0, 12, 4, 14, 14),
                BooleanOp.OR);

        return shape;
    }
    private VoxelShape SHAPE = getVoxelShape();

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    // block entity


    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof BlueprintMakerBlockEntity) {
                ((BlueprintMakerBlockEntity) blockEntity).drops();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if (entity instanceof BlueprintMakerBlockEntity) {
                NetworkHooks.openScreen(((ServerPlayer)pPlayer), (BlueprintMakerBlockEntity)entity, pPos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }



        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlueprintMakerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.BLUEPRINT_MAKER.get(),
                BlueprintMakerBlockEntity::tick);
    }
}
