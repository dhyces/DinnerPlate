package dhyces.dinnerplate.datagen;

import dhyces.dinnerplate.registry.BlockRegistry;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class BlockLootTableGen extends BlockLoot {

	@Override
	protected void addTables() {
//		add(BlockRegistry.MIXING_BOWL_BLOCK.get(), LootTable.lootTable()
//														.withPool(LootPool.lootPool()
//																		.setRolls(ConstantValue.exactly(1.0f))
//																		.));
	}
	
}
