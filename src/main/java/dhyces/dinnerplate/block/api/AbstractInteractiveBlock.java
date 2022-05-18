package dhyces.dinnerplate.block.api;

import net.minecraft.world.level.block.Block;

/**
 * This block holds an immutable map of mouseinteractions and functions, where the functions transform a context into an interactionresult
 */
public abstract class AbstractInteractiveBlock extends Block {


    public AbstractInteractiveBlock(Properties p_49795_) {
        super(p_49795_);
        //initInteractions();
    }

//	private Pair<List<Function<, R>>> initInteractions() {
//
//	}
}
