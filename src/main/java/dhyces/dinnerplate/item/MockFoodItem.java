package dhyces.dinnerplate.item;

import dhyces.dinnerplate.bite.IBitableItem;
import dhyces.dinnerplate.bite.IBite;
import dhyces.dinnerplate.capability.CapabilityEventSubscriber;
import dhyces.dinnerplate.capability.bitten.IMockFoodProvider;
import dhyces.dinnerplate.capability.bitten.MockFoodCapability;
import dhyces.dinnerplate.client.render.item.MockFoodItemRenderer;
import dhyces.dinnerplate.registry.ItemRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.List;
import java.util.function.Consumer;

public class MockFoodItem extends BitableItem {

	public MockFoodItem() {
		super(c -> c, new Item.Properties().stacksTo(1));
	}

	public static ItemStack mockFoodStack(ItemStack mockStack, LivingEntity entity, int chewCount) {
		if (mockStack.getItem() instanceof MockFoodItem)
			return mockStack;
		if (mockStack.isEmpty() || !mockStack.isEdible())
			throw new IllegalStateException("ItemStack: " + mockStack + " is not a qualifying item. Either empty or not edible.");
		var stack = ItemRegistry.MOCK_FOOD_ITEM.get().getDefaultInstance().copy();
		var optional = stack.getCapability(CapabilityEventSubscriber.MOCK_FOOD_CAPABILITY);
		optional.ifPresent(c -> c.initialize(mockStack, entity, chewCount));
		return stack;
	}

	@Override
	public int getBiteCount(ItemStack stack) {
		return getCapabilityLowest(stack).getBiteCount();
	}

	@Override
	public int getMaxBites(ItemStack stack, LivingEntity entity) {
		return getCapabilityLowest(stack).getMaxBites();
	}

	@Override
	public boolean incrementBiteCount(ItemStack stack, LivingEntity entity) {
		return getCapabilityLowest(stack).incrementBiteCount();
	}

	@Override
	public void setBiteCount(ItemStack stack, LivingEntity entity, int count) {
		getCapabilityLowest(stack).setBiteCount(count);
	}

	@Override
	public IBite getBite(ItemStack stack, LivingEntity entity, int chew) {
		return getCapabilityLowest(stack).getBite(chew);
	}

	@Override
	public boolean canAlwaysEat(ItemStack stack, LivingEntity entity) {
		return getCapabilityLowest(stack).canAlwaysEat();
	}

	@Override
	public boolean canBeFast(ItemStack stack, LivingEntity entity) {
		return getCapabilityLowest(stack).canBeFast();
	}

	@Override
	public boolean isMeat(ItemStack stack, LivingEntity entity) {
		return getCapabilityLowest(stack).isMeat();
	}

	@Override
	public ItemStack finish(ItemStack stack, Level level, LivingEntity livingEntity) {
		return getCapabilityLowest(stack).finish(stack, level, livingEntity);
	}

	@Override
	public boolean isBarVisible(ItemStack pStack) {
		return pStack.isEmpty() ? false : true;
	}

	@Override
	public int getBarWidth(ItemStack pStack) {
		var chewCount = getCapabilityLowest(pStack).getBiteCount();
		return 13 - chewCount * 13 / 3;
	}

	@Override
	public Component getName(ItemStack pStack) {
		var hiddenStack = getCapabilityLowest(pStack).getRealStack();
		return hiddenStack.getHoverName();
	}

	@Override
	public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents,
			TooltipFlag pIsAdvanced) {
		if (getCapabilityLowest(pStack).getRealStack().isEmpty() && pStack != getDefaultInstance())
			pTooltipComponents.add(Component.literal("You should not have this item").withStyle(ChatFormatting.RED));
	}

	@Override
	public Rarity getRarity(ItemStack pStack) {
		return getCapabilityLowest(pStack).getRealStack().getRarity();
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
		var stack = pPlayer.getItemInHand(pUsedHand);
		var capability = getCapabilityLowest(stack);
		if (pPlayer.canEat(capability.canAlwaysEat())) {
			var ret = capability.eat(stack, pPlayer, pLevel);
			return InteractionResultHolder.consume(ret);
		}
		return InteractionResultHolder.pass(stack);
	}

	@Override
	public InteractionResult useOn(UseOnContext pContext) {
		var level = pContext.getLevel();
		var pos = pContext.getClickedPos();
		var blockState = level.getBlockState(pos);
		if (blockState.is(Blocks.COMPOSTER)) {
			if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
				var hand = pContext.getItemInHand();
				var realCopy = getCapabilityLowest(hand).getRealStack().copy();
				ComposterBlock.insertItem(pContext.getPlayer(), blockState, serverLevel, realCopy, pos);
				if (realCopy.isEmpty()) {
					hand.shrink(1);
					level.playSound((Player)null, pos, SoundEvents.COMPOSTER_FILL_SUCCESS, SoundSource.BLOCKS, 0.7f, 0.5f);
				}
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		return super.useOn(pContext);
	}

	@Override
	public ItemStack getCraftingRemainingItem(ItemStack stack) {
		return getCapabilityLowest(stack).getRealStack().getCraftingRemainingItem();
	}

	@Override
	public ParticleOptions getParticle(ItemStack stack) {
		return getCapabilityLowest(stack).getParticle(stack);
	}

	@Override
	public SoundEvent getEatingSound(ItemStack stack) {
		return getCapabilityLowest(stack).getEatingSound(stack);
	}

	@Override
	public CompoundTag getShareTag(ItemStack stack) {
		stack.getOrCreateTag().merge(((MockFoodCapability)getCapabilityLowest(stack)).serializeNBT());
		return stack.serializeNBT();
	}

	@Override
	public void readShareTag(ItemStack stack, CompoundTag nbt) {
		((MockFoodCapability)getCapabilityLowest(stack)).deserializeNBT(nbt);
		stack.deserializeNBT(nbt);
	}

	public static IMockFoodProvider getCapabilityLowest(ItemStack stack) {
		return stack.getCapability(CapabilityEventSubscriber.MOCK_FOOD_CAPABILITY).resolve().get();
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
		return new MockFoodCapability();
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return new MockFoodItemRenderer();
			}
		});
	}
}