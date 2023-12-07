package dhyces.dinnerplate.item;

import dhyces.dinnerplate.client.render.PlateRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import java.util.function.Consumer;

public class PlateItem extends CapabilityNBTBlockItem {

    public PlateItem(Block block, Properties pProperties) {
        super((stack, tag) -> {
            return new ICapabilityProvider() {
                @Override
                public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
                    return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, LazyOptional.of(() -> new ItemStackHandler(1)));
                }
            };
        }, block, pProperties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new PlateRenderer();
            }
        });
    }
}
