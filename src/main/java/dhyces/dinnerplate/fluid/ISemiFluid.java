package dhyces.dinnerplate.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;

public interface ISemiFluid extends BucketPickup {

    ItemStack getPile(BlockState state, BlockPos pos, Level level, Player player);

}
