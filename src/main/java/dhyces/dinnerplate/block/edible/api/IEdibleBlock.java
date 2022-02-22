package dhyces.dinnerplate.block.edible.api;

import dhyces.dinnerplate.bite.IBite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public interface IEdibleBlock {

	public InteractionResult takeBite(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult res);

	// In case the block wants to utilize an array of IBites to control every bite
	public IBite getBite(int biteNum);

}
