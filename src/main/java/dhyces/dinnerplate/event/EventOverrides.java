package dhyces.dinnerplate.event;

import dhyces.dinnerplate.DinnerPlate;
import dhyces.dinnerplate.block.api.IForkedInteract;
import dhyces.dinnerplate.blockentity.api.AbstractDinnerBlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = DinnerPlate.MODID)
public class EventOverrides {

    @SubscribeEvent
    public static void shiftLeftClick(final PlayerInteractEvent.LeftClickBlock e) {
        var blockState = e.getWorld().getBlockState(e.getPos());
        if (e.getPlayer().isShiftKeyDown() && blockState.getBlock() instanceof IForkedInteract base) {
            if (e.getWorld().getBlockEntity(e.getPos()) instanceof AbstractDinnerBlockEntity bEntity) {
                var result = base.shiftLeftClick(blockState, bEntity, e.getWorld(), e.getPos(), e.getPlayer(), e.getSide().isClient());
                if (result == InteractionResult.FAIL || result == InteractionResult.PASS) {
                    return;
                }
            }
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void shiftRightClick(final PlayerInteractEvent.RightClickBlock e) {
        var blockState = e.getWorld().getBlockState(e.getPos());
        if (e.getPlayer().isShiftKeyDown() && blockState.getBlock() instanceof IForkedInteract) {
            e.setUseBlock(Result.ALLOW);
        }
    }
}
