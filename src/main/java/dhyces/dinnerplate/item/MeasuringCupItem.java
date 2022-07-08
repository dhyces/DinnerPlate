package dhyces.dinnerplate.item;

import dhyces.dinnerplate.capability.fluid.MeasuredFluidCapability;
import dhyces.dinnerplate.client.render.MeasuringCupRenderer;
import dhyces.dinnerplate.util.FluidHelper;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class MeasuringCupItem extends CapabilityNBTBlockItem {

    public MeasuringCupItem(Block block, Properties pProperties) {
        super((stack, tag) -> new MeasuredFluidCapability(stack, 1000, FluidHelper.PILE), block, pProperties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new MeasuringCupRenderer();
            }
        });
    }
}