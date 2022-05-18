package dhyces.dinnerplate.block.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.ForgeSoundType;

public interface IUniqueSoundType {

    ForgeSoundType getUniqueSoundType(BlockState state, LevelReader world, BlockPos pos, Entity entity);

}
