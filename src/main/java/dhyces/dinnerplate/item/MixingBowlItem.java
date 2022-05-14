package dhyces.dinnerplate.item;

import dhyces.dinnerplate.capability.mixed.MixedCapability;
import dhyces.dinnerplate.client.render.item.MixingBowlItemRenderer;
import dhyces.dinnerplate.inventory.MixedInventory;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;

public class MixingBowlItem extends CapabilityNBTBlockItem {

    public MixingBowlItem(Block pBlock, Properties pProperties) {
        super((stack, tag) -> new MixedCapability.Item(stack, new MixedInventory(9)), pBlock, pProperties);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return new MixingBowlItemRenderer();
            }
        });
    }
}
