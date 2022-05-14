package dhyces.dinnerplate.datagen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import dhyces.dinnerplate.block.PlateBlock;
import dhyces.dinnerplate.registry.BlockRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BlockLootTableGen extends BlockLoot {

	protected void addTables() {
//		add(BlockRegistry.MIXING_BOWL_BLOCK.get(), LootTable.lootTable()
//														.withPool(LootPool.lootPool()
//																		.setRolls(ConstantValue.exactly(1.0f))
//																		.));
		super.addTables();
		addPlateDrop(BlockRegistry.WHITE_PLATE_BLOCK.get());
		addPlateDrop(BlockRegistry.ORANGE_PLATE_BLOCK.get());
		addPlateDrop(BlockRegistry.MAGENTA_PLATE_BLOCK.get());
		addPlateDrop(BlockRegistry.LIGHT_BLUE_PLATE_BLOCK.get());
		addPlateDrop(BlockRegistry.YELLOW_PLATE_BLOCK.get());
		addPlateDrop(BlockRegistry.LIME_PLATE_BLOCK.get());
		addPlateDrop(BlockRegistry.PINK_PLATE_BLOCK.get());
		addPlateDrop(BlockRegistry.GRAY_PLATE_BLOCK.get());
		addPlateDrop(BlockRegistry.LIGHT_GRAY_PLATE_BLOCK.get());
		addPlateDrop(BlockRegistry.CYAN_PLATE_BLOCK.get());
		addPlateDrop(BlockRegistry.PURPLE_PLATE_BLOCK.get());
		addPlateDrop(BlockRegistry.BLUE_PLATE_BLOCK.get());
		addPlateDrop(BlockRegistry.BROWN_PLATE_BLOCK.get());
		addPlateDrop(BlockRegistry.GREEN_PLATE_BLOCK.get());
		addPlateDrop(BlockRegistry.RED_PLATE_BLOCK.get());
		addPlateDrop(BlockRegistry.BLACK_PLATE_BLOCK.get());
		
		// TODO
		add(BlockRegistry.MIXING_BOWL_BLOCK.get(), noDrop());
		add(BlockRegistry.MEASURING_CUP_BLOCK.get(), noDrop());
	}
	
	protected void addPlateDrop(Block block) {
		add(block, plateDrop(block));
	}

	protected LootTable.Builder plateDrop(Block plateBlock) {
		return LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0f))
						.add(LootItem.lootTableItem(plateBlock.asItem()).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("Item", "Item")).apply(CopyBlockState.copyState(plateBlock).copy(PlateBlock.PLATES))));
	}
	
	public static class BlockLootTableProvider extends LootTableProvider {

		public BlockLootTableProvider(DataGenerator pGenerator) {
			super(pGenerator);
		}
		
		@Override
		protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootContextParamSet>> getTables() {
			return ImmutableList.of(Pair.of(BlockLootTableGen::new, LootContextParamSets.BLOCK));
		}
	}
}
