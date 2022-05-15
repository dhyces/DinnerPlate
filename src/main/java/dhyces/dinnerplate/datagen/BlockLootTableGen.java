package dhyces.dinnerplate.datagen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import dhyces.dinnerplate.block.MixingBowlBlock;
import dhyces.dinnerplate.block.PlateBlock;
import dhyces.dinnerplate.registry.BlockRegistry;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockLootTableGen implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {

	Map<ResourceLocation, LootTable.Builder> map = new HashMap<>();

	protected void addTables() {
//		add(BlockRegistry.MIXING_BOWL_BLOCK.get(), LootTable.lootTable()
//														.withPool(LootPool.lootPool()
//																		.setRolls(ConstantValue.exactly(1.0f))
//																		.));
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
		add(BlockRegistry.MIXING_BOWL_BLOCK.get(), c -> oopsItsJustFunctions(c, copyItemsFunc(),
																				copyFluidsFunc(),
																				copyDataFunc("MixState"),
																				copyDataFunc("UsedSize"),
																				copyStateFunc(c, MixingBowlBlock.MIX_POSITION)));
		add(BlockRegistry.MEASURING_CUP_BLOCK.get(), c -> oopsItsJustFunctions(c, copyFluidFunc()));
	}
	
	protected void addPlateDrop(Block plateBlock) {
		add(plateBlock, LootTable.lootTable()
				.withPool(singleRollPool()
						.add(AlternativesEntry.alternatives(
								LootItem.lootTableItem(plateBlock.asItem())
										.apply(copyItemFunc())
										.when(stateCondition(plateBlock, PlateBlock.PLATES, 1)),
								LootItem.lootTableItem(plateBlock.asItem())
										.apply(copyStateFunc(plateBlock, PlateBlock.PLATES))))));
	}

	protected LootTable.Builder oopsItsJustFunctions(Block block, LootItemFunction.Builder... functions) {
		var lootItem = LootItem.lootTableItem(block);
		Arrays.stream(functions).forEach(lootItem::apply);
		return LootTable.lootTable()
				.withPool(singleRollPool()
						.add(lootItem));
	}

	protected LootItemFunction.Builder copyFluidsFunc() {
		return copyDataFunc("Fluids");
	}

	protected LootItemFunction.Builder copyFluidFunc() {
		return copyDataFunc("Fluid");
	}

	protected LootItemFunction.Builder copyItemsFunc() {
		return copyDataFunc("Items");
	}

	protected LootItemFunction.Builder copyItemFunc() {
		return copyDataFunc("Item");
	}

	protected LootPool.Builder singleRollPool() {
		return LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f));
	}

	protected LootItemCondition.Builder stateCondition(Block block, Property<Integer> property, int value) {
		return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
				.setProperties(StatePropertiesPredicate.Builder.properties()
						.hasProperty(property, value));
	}

	protected LootItemFunction.Builder copyStateFunc(Block block, Property<?> property) {
		return CopyBlockState.copyState(block).copy(property);
	}

	protected LootItemFunction.Builder copyDataFunc(String data) {
		return copyDataFunc(data, data);
	}

	protected LootItemFunction.Builder copyDataFunc(String sourceData, String targetData) {
		return CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy(sourceData, targetData);
	}

	protected LootTable.Builder noDrop() {
		return LootTable.lootTable();
	}

	protected void add(Block block, Function<Block, LootTable.Builder> function) {
		add(block, function.apply(block));
	}

	protected void add(Block block, LootTable.Builder lootTableBuilder) {
		map.put(block.getLootTable(), lootTableBuilder);
	}

	public void accept(BiConsumer<ResourceLocation, LootTable.Builder> p_124179_) {
		this.addTables();
		Set<ResourceLocation> set = Sets.newHashSet();

		for(Block block : BlockRegistry.BLOCK_REGISTER.getEntries().stream().map(c -> c.get()).toList()) {
			ResourceLocation resourcelocation = block.getLootTable();
			if (resourcelocation != BuiltInLootTables.EMPTY && set.add(resourcelocation)) {
				LootTable.Builder loottable$builder = map.remove(block.getLootTable());
				if (loottable$builder == null) {
					throw new IllegalStateException(String.format("Missing loottable '%s' for '%s'", resourcelocation, Registry.BLOCK.getKey(block)));
				}

				p_124179_.accept(resourcelocation, loottable$builder);
			}
		}

		if (!this.map.isEmpty()) {
			throw new IllegalStateException("Created block loot tables for non-blocks: " + this.map.keySet());
		}
	}

	public static class BlockLootTableProvider extends LootTableProvider {

		public BlockLootTableProvider(DataGenerator pGenerator) {
			super(pGenerator);
		}
		
		@Override
		protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
			return ImmutableList.of(Pair.of(BlockLootTableGen::new, LootContextParamSets.BLOCK));
		}

		@Override
		protected void validate(Map<ResourceLocation, LootTable> map, @NotNull ValidationContext validationTracker) {
			// NO_OP
		}
	}
}
