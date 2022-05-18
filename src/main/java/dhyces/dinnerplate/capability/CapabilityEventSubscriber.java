package dhyces.dinnerplate.capability;

import dhyces.dinnerplate.DinnerPlate;
import dhyces.dinnerplate.capability.bitten.IMockFoodProvider;
import dhyces.dinnerplate.registry.FluidRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class CapabilityEventSubscriber {

    public static final Capability<IMockFoodProvider> MOCK_FOOD_CAPABILITY = CapabilityManager.get(new CapabilityToken<IMockFoodProvider>() {
    });

    @EventBusSubscriber(modid = DinnerPlate.MODID, bus = Bus.MOD)
    public class ModSided {

        @SubscribeEvent
        public static void registerCapability(final RegisterCapabilitiesEvent event) {
            event.register(IMockFoodProvider.class);
        }
    }

    @EventBusSubscriber(modid = DinnerPlate.MODID, bus = Bus.FORGE)
    public class ForgeSided {

        public static final ResourceLocation SOUP_CAP_ID = new ResourceLocation(DinnerPlate.MODID, "soup");

        @SubscribeEvent
        public static void attachCapability(final AttachCapabilitiesEvent<ItemStack> event) {
            var stack = event.getObject();
            if (stack.getItem().equals(Items.MUSHROOM_STEW)) {
                event.addCapability(SOUP_CAP_ID, bowlOf(stack, FluidRegistry.MUSHROOM_STEW_FLUID.get()));
            }
            if (stack.getItem().equals(Items.BEETROOT_SOUP)) {
                event.addCapability(SOUP_CAP_ID, bowlOf(stack, FluidRegistry.BEETROOT_SOUP_FLUID.get()));
            }
            if (stack.getItem().equals(Items.RABBIT_STEW)) {
                event.addCapability(SOUP_CAP_ID, bowlOf(stack, FluidRegistry.RABBIT_STEW_FLUID.get()));
            }
        }

        private static ICapabilityProvider bowlOf(ItemStack stack, Fluid fluid) {
            var handler = new FluidHandlerItemStackSimple.SwapEmpty(stack, new ItemStack(Items.BOWL), 100);
            handler.fill(new FluidStack(fluid, 100), FluidAction.EXECUTE);
            return handler;
        }
    }
}
