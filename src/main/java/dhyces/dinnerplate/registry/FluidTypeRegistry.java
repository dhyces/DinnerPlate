package dhyces.dinnerplate.registry;

import dhyces.dinnerplate.DinnerPlate;
import dhyces.dinnerplate.client.FluidClientInit;
import dhyces.dinnerplate.fluid.StewFluidType;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class FluidTypeRegistry {

    private static final DeferredRegister<FluidType> FLUID_TYPE_REGISTER = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, DinnerPlate.MODID);

    public static final RegistryObject<FluidType> MUSHROOM_STEW_FLUID_TYPE;
    public static final RegistryObject<FluidType> BEETROOT_SOUP_FLUID_TYPE;
    public static final RegistryObject<FluidType> RABBIT_STEW_FLUID_TYPE;

    static {
        MUSHROOM_STEW_FLUID_TYPE = FLUID_TYPE_REGISTER.register("mushroom_stew_fluid_type", () -> new StewFluidType() {
            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(FluidClientInit.MUSHROOM_STEW_RENDERER);
            }
        });
        BEETROOT_SOUP_FLUID_TYPE = FLUID_TYPE_REGISTER.register("beetroot_soup_fluid_type", () -> new StewFluidType() {
            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(FluidClientInit.BEETROOT_SOUP_RENDERER);
            }
        });
        RABBIT_STEW_FLUID_TYPE = FLUID_TYPE_REGISTER.register("rabbit_stew_fluid_type", () -> new StewFluidType() {
            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(FluidClientInit.RABBIT_STEW_RENDERER);
            }
        });
    }

    public static void register(IEventBus bus) {
        FLUID_TYPE_REGISTER.register(bus);
    }
}
