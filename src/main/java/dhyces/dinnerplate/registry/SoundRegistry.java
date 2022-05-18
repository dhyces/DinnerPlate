package dhyces.dinnerplate.registry;

import dhyces.dinnerplate.DinnerPlate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegistry {

    public static final RegistryObject<SoundEvent> PLATE_PLACED;
    public static final RegistryObject<SoundEvent> PLATE_STEP;
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DinnerPlate.MODID);

    static {
        PLATE_PLACED = register("plate_placed");
        PLATE_STEP = register("plate_step");
    }

    public static void register(IEventBus bus) {
        SOUND_EVENTS.register(bus);
    }

    private static RegistryObject<SoundEvent> register(String id) {
        return SOUND_EVENTS.register(id, () -> new SoundEvent(new ResourceLocation(DinnerPlate.MODID, id)));
    }
}