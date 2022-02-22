package dhyces.dinnerplate.block;

import dhyces.dinnerplate.Constants;
import dhyces.dinnerplate.bite.IBitable;
import dhyces.dinnerplate.block.api.AbstractDinnerBlock;
import dhyces.dinnerplate.blockentity.PlateBlockEntity;
import dhyces.dinnerplate.item.MockFoodItem;
import dhyces.dinnerplate.registry.ItemRegistry;
import dhyces.dinnerplate.registry.SoundRegistry;
import dhyces.dinnerplate.sound.DinnerSoundTypes;
import dhyces.dinnerplate.util.BlockHelper;
import dhyces.dinnerplate.util.MathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.ForgeSoundType;

public class PlateBlock extends AbstractDinnerBlock<PlateBlockEntity> {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final int MIN_LEVEL = 1;
	public static final int MAX_LEVEL = 8;
	public static final IntegerProperty PLATES = IntegerProperty.create("plates", MIN_LEVEL, MAX_LEVEL);

	public PlateBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(PLATES, 1).setValue(FACING, Direction.NORTH));
	}

	@Override
	public InteractionResult rightClick(BlockState state, PlateBlockEntity plateEntity, Level level, BlockPos pos,
											Player player, InteractionHand hand, BlockHitResult res, boolean isClient) {
		if (plateEntity.hasItem()) {
			var mockFoodOptional = plateEntity.getMockFood();
			if (mockFoodOptional.isPresent() && player.canEat(mockFoodOptional.get().canAlwaysEat())) {
					if (!mockFoodOptional.get().isFast() && plateEntity.getBiteCount() < Constants.MAX_ITEM_CHEW_COUNT) {
						plateEntity.bite(player);
					} else {
						plateEntity.eat(player);
					}
				return InteractionResult.sidedSuccess(isClient);
			}
		} else {
			var preferredItem = getPreferredItemOtherwise(player, InteractionHand.OFF_HAND);
			if (preferredItem.isEmpty() || player.getMainHandItem().is(Items.DEBUG_STICK))
				return InteractionResult.PASS;
			var levelProperty = state.getValue(PLATES);
			if (preferredItem.sameItem(ItemRegistry.PLATE_ITEM.get().getDefaultInstance())) {
				if (canAddPlate(state, preferredItem)) {
					if (!isClient) {
						var itemStateTag = BlockHelper.getBlockStateTag(preferredItem);
						var itemPlates = getPlatesFromStack(preferredItem);
						level.setBlock(pos, setPlates(state, Mth.clamp(levelProperty+itemPlates, MIN_LEVEL, MAX_LEVEL)), 11);
						if (!player.getAbilities().instabuild) {
							var overflowedPlates = MathHelper.additionOverflow(levelProperty+itemPlates, MAX_LEVEL);
							if (overflowedPlates == 0)
								preferredItem.shrink(1);
							else
								BlockHelper.setPropertyInTag(PLATES, overflowedPlates, itemStateTag);
						}
					}
				} else {
					return InteractionResult.PASS;
				}
			} else if (levelProperty == 1) {
				if (!isClient) {
					var itemToSet = preferredItem.copy().split(1);
					plateEntity.setItem(itemToSet.isEdible() && !(itemToSet.getItem() instanceof IBitable) ? MockFoodItem.mockFoodStack(itemToSet, 0) : itemToSet);
					if (!player.getAbilities().instabuild)
						preferredItem.shrink(1);
				}
			} else {
				return InteractionResult.PASS;
			}
			level.playSound((Player)null, pos, SoundRegistry.PLATE_STEP.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
			return InteractionResult.sidedSuccess(isClient);
		}
		return super.rightClick(state, plateEntity, level, pos, player, hand, res, isClient);
	}

	@Override
	public InteractionResult shiftRightClick(BlockState state, PlateBlockEntity plateEntity, Level level, BlockPos pos,
												Player player, InteractionHand hand, BlockHitResult res, boolean isClient) {
		var platesProperty = state.getValue(PLATES);
		if (plateEntity.hasItem()) {
			// TODO: may need to split this back up due to the difficulty of making it work sided
			var stack = plateEntity.removeLastItem();
			insertInvOrSpawn(level, pos, -0.25, player.getInventory(), stack);
			return InteractionResult.sidedSuccess(isClient);
		} else if (platesProperty > 1 && !player.getMainHandItem().is(Items.DEBUG_STICK)) {
			var stack = asItem().getDefaultInstance().copy();
			insertInvOrSpawn(level, pos, .5, player.getInventory(), stack);
			level.setBlock(pos, growPlates(state, -1), 3);
			return InteractionResult.sidedSuccess(isClient);
		}
		return super.shiftRightClick(state, plateEntity, level, pos, player, hand, res, isClient);
	}

	private boolean canAddPlate(BlockState pState, ItemStack stackToCheck) {
		return 	pState.getValue(PLATES) < MAX_LEVEL &&
				stackToCheck.getOrCreateTag().getCompound(Constants.TAG_BLOCK_ENTITY).getCompound(Constants.TAG_SINGLE_ITEM).isEmpty();
	}

	public int getPlatesFromStack(ItemStack stack) {
		return BlockHelper.getPropertyFromTag(PLATES, BlockHelper.getBlockStateTag(stack));
	}

	public BlockState setPlates(BlockState pState, int pInt) {
		return pState.setValue(PLATES, pInt);
	}

	public BlockState growPlates(BlockState pState, int pAmount) {
		return pState.setValue(PLATES, pState.getValue(PLATES) + pAmount);
	}

	public double topPlateHeight(BlockState pState) {
		return (pState.getValue(PLATES)-1) * 0.09375;
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return Shapes.box(.125, 0, .125, .875, .078125 + topPlateHeight(pState), .875);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		return defaultBlockState().setValue(FACING, pContext.getHorizontalDirection());
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
		pBuilder.add(FACING, PLATES);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new PlateBlockEntity(pPos, pState);
	}

	@Override
	public ForgeSoundType getSoundType(BlockState state, LevelReader world, BlockPos pos, Entity entity) {
		return DinnerSoundTypes.PLATE_SOUND_TYPE;
	}
}
