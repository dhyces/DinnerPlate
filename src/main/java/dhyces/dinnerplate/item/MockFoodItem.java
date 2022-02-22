package dhyces.dinnerplate.item;

import java.util.List;
import java.util.function.Consumer;

import dhyces.dinnerplate.bite.IBitable;
import dhyces.dinnerplate.bite.IBite;
import dhyces.dinnerplate.capability.CapabilityEventSubscriber;
import dhyces.dinnerplate.capability.IMockFoodProvider;
import dhyces.dinnerplate.capability.MockFoodCapability;
import dhyces.dinnerplate.registry.ItemRegistry;
import dhyces.dinnerplate.render.item.MockFoodItemRenderer;
import dhyces.dinnerplate.util.FoodHelper;
import dhyces.dinnerplate.util.ItemHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class MockFoodItem extends Item implements IBitable {

	private final MockFoodItemRenderer renderer = new MockFoodItemRenderer();

	public MockFoodItem() {
		super(new Item.Properties().food(new FoodProperties.Builder().build()).stacksTo(1));
	}

	public static ItemStack mockFoodStack(ItemStack mockStack, int chewCount) {
		if (mockStack.getItem() instanceof MockFoodItem || mockStack.isEmpty() || !mockStack.isEdible())
			return mockStack;
		var stack = ItemRegistry.MOCK_FOOD_ITEM.get().getDefaultInstance().copy();
		var optional = stack.getCapability(CapabilityEventSubscriber.MOCK_FOOD_CAPABILITY);
		optional.ifPresent(c -> c.initialize(mockStack, chewCount));
		return stack;
	}

	@Override
	public int getBiteCount(ItemStack stack) {
		return getCapabilityLowest(stack).getBiteCount();
	}

	@Override
	public int getMaxBiteCount(ItemStack stack) {
		return getCapabilityLowest(stack).getMaxBiteCount();
	}

	@Override
	public boolean incrementBiteCount(ItemStack stack) {
		return getCapabilityLowest(stack).incrementBiteCount();
	}

	@Override
	public void setBiteCount(ItemStack stack, int count) {
		getCapabilityLowest(stack).setBiteCount(count);
	}

	@Override
	public IBite getBite(ItemStack stack, int chew) {
		return getCapabilityLowest(stack).getBite(chew);
	}

	@Override
	public boolean canAlwaysEat(ItemStack stack) {
		return getCapabilityLowest(stack).canAlwaysEat();
	}

	@Override
	public boolean isFast(ItemStack stack) {
		return getCapabilityLowest(stack).isFast();
	}

	@Override
	public boolean isMeat(ItemStack stack) {
		return getCapabilityLowest(stack).isMeat();
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemStack) {
		return getCapabilityLowest(itemStack).getRealStack();
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
			pTooltipComponents.add(new TextComponent("You should not have this item").withStyle(ChatFormatting.RED));
	}

	@Override
	public Rarity getRarity(ItemStack pStack) {
		var hiddenStack = getCapabilityLowest(pStack).getRealStack();
		return hiddenStack.getRarity();
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
		var stack = pPlayer.getItemInHand(pUsedHand);
		var capability = getCapabilityLowest(stack);
		if (capability.getRealStack().isEdible()) {
			if (pPlayer.canEat(capability.canAlwaysEat())) {
				var isEaten = capability.incrementBiteCount();
				var bite = capability.getBite(capability.getBiteCount());
				var underlyingStack = capability.getRealStack();
				if (!pLevel.isClientSide)
					FoodHelper.playerStaticEatBite(pLevel, pPlayer, underlyingStack, bite);
				if (isEaten) {
					capability.setBiteCount(0);
					if (!pPlayer.getAbilities().instabuild)
						stack.shrink(1);
					if (!pLevel.isClientSide)
						FoodHelper.playerStaticEat(pLevel, pPlayer, underlyingStack);
					return InteractionResultHolder.consume(ItemHelper.returnedItem(underlyingStack));
				}
				return InteractionResultHolder.consume(stack);
			}
		}
		return InteractionResultHolder.pass(stack);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
		var real = getCapabilityLowest(pStack).getRealStack();
		var ret = real.finishUsingItem(pLevel, pLivingEntity);
		real.setCount(1);
		return ret;
	}

	public static IMockFoodProvider getCapabilityLowest(ItemStack stack) {
		return stack.getCapability(CapabilityEventSubscriber.MOCK_FOOD_CAPABILITY).resolve().get();
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
		return new MockFoodCapability();
	}

	@Override
	public void initializeClient(Consumer<IItemRenderProperties> consumer) {
		consumer.accept(new IItemRenderProperties() {
			@Override
			public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
				return renderer;
			}
		});
	}
}
