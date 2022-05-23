package dhyces.dinnerplate.registry;

import com.mojang.datafixers.util.Pair;
import dhyces.dinnerplate.DinnerPlate;
import dhyces.dinnerplate.blockentity.MeasuringCupBlockEntity;
import dhyces.dinnerplate.blockentity.MixingBowlBlockEntity;
import dhyces.dinnerplate.blockentity.MortarBlockEntity;
import dhyces.dinnerplate.blockentity.PlateBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
@EventBusSubscriber(modid = DinnerPlate.MODID, bus = Bus.MOD)
public class BEntityRegistry {

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, DinnerPlate.MODID);
    public static final RegistryObject<BlockEntityType<PlateBlockEntity>> PLATE_ENTITY;
    public static final RegistryObject<BlockEntityType<MixingBowlBlockEntity>> MIXING_BOWL_ENTITY;
    public static final RegistryObject<BlockEntityType<MeasuringCupBlockEntity>> MEASURING_CUP_ENTITY;
    public static final RegistryObject<BlockEntityType<MortarBlockEntity>> MORTAR_ENTITY;

    private static final List<Pair<Supplier<BlockEntityType<BlockEntity>>, BlockEntityRendererProvider<BlockEntity>>> renderers = new LinkedList<>();

    static {
        PLATE_ENTITY = register("plate", () -> commonType(PlateBlockEntity::new,
                BlockRegistry.WHITE_PLATE_BLOCK,
                BlockRegistry.ORANGE_PLATE_BLOCK,
                BlockRegistry.MAGENTA_PLATE_BLOCK,
                BlockRegistry.LIGHT_BLUE_PLATE_BLOCK,
                BlockRegistry.YELLOW_PLATE_BLOCK,
                BlockRegistry.LIME_PLATE_BLOCK,
                BlockRegistry.PINK_PLATE_BLOCK,
                BlockRegistry.GRAY_PLATE_BLOCK,
                BlockRegistry.LIGHT_GRAY_PLATE_BLOCK,
                BlockRegistry.CYAN_PLATE_BLOCK,
                BlockRegistry.PURPLE_PLATE_BLOCK,
                BlockRegistry.BLUE_PLATE_BLOCK,
                BlockRegistry.BROWN_PLATE_BLOCK,
                BlockRegistry.GREEN_PLATE_BLOCK,
                BlockRegistry.RED_PLATE_BLOCK,
                BlockRegistry.BLACK_PLATE_BLOCK));
        MIXING_BOWL_ENTITY = register("mixing_bowl", () -> BlockEntityType.Builder.of(MixingBowlBlockEntity::new, BlockRegistry.MIXING_BOWL_BLOCK.get()).build(null));
        MEASURING_CUP_ENTITY = register("measuring_cup", () -> BlockEntityType.Builder.of(MeasuringCupBlockEntity::new, BlockRegistry.MEASURING_CUP_BLOCK.get()).build(null));
        MORTAR_ENTITY = register("mortar_pestle", () -> BlockEntityType.Builder.of(MortarBlockEntity::new, BlockRegistry.MORTAR_BLOCK.get()).build(null));
    }

    public static void register(IEventBus bus) {
        BLOCK_ENTITY_REGISTER.register(bus);
    }

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String id, Supplier<BlockEntityType<T>> supplier) {
        return BLOCK_ENTITY_REGISTER.register(id, supplier);
    }

    private static <T extends BlockEntity> BlockEntityType<T> commonType(BlockEntitySupplier<T> supplier, Supplier<Block>... blocks) {
        return BlockEntityType.Builder.of(supplier, Arrays.stream(blocks).map(c -> c.get()).toArray(Block[]::new)).build(null);
    }

    private static RegistryObject<BlockEntityType<BlockEntity>> customRendered(String id, Supplier<BlockEntityType<BlockEntity>> supplier, BlockEntityRendererProvider<BlockEntity> provider) {
        var registered = register(id, supplier);
        renderers.add(Pair.of(registered, provider));
        return registered;
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        renderers.forEach(pair -> event.registerBlockEntityRenderer(pair.getFirst().get(), pair.getSecond()));
    }
}
