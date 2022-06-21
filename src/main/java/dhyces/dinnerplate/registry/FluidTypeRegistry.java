package dhyces.dinnerplate.registry;

import dhyces.dinnerplate.DinnerPlate;
import dhyces.dinnerplate.fluid.StewFluidType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FluidTypeRegistry {

    private static final DeferredRegister<FluidType> FLUID_TYPE_REGISTER = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, DinnerPlate.MODID);

    public static final RegistryObject<FluidType> STEW_FLUID_TYPE;

    static {
        STEW_FLUID_TYPE = FLUID_TYPE_REGISTER.register("stew_fluid_type", () -> new StewFluidType());
    }

    public static void register(IEventBus bus) {
        FLUID_TYPE_REGISTER.register(bus);
    }
}
