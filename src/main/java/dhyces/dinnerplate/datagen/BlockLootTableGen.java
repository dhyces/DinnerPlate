package dhyces.dinnerplate.datagen;

import com.google.common.collect.ImmutableList;
import dhyces.dinnerplate.block.MixingBowlBlock;
import dhyces.dinnerplate.block.PlateBlock;
import dhyces.dinnerplate.registry.BlockRegistry;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class BlockLootTableGen implements LootTableSubProvider {

    @Override
    public void generate(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
//		add(BlockRegistry.MIXING_BOWL_BLOCK.get(), LootTable.lootTable()
//														.withPool(LootPool.lootPool()
//																		.setRolls(ConstantValue.exactly(1.0f))
//																		.));
        addPlateDrop(consumer, BlockRegistry.WHITE_PLATE_BLOCK.get());
        addPlateDrop(consumer, BlockRegistry.ORANGE_PLATE_BLOCK.get());
        addPlateDrop(consumer, BlockRegistry.MAGENTA_PLATE_BLOCK.get());
        addPlateDrop(consumer, BlockRegistry.LIGHT_BLUE_PLATE_BLOCK.get());
        addPlateDrop(consumer, BlockRegistry.YELLOW_PLATE_BLOCK.get());
        addPlateDrop(consumer, BlockRegistry.LIME_PLATE_BLOCK.get());
        addPlateDrop(consumer, BlockRegistry.PINK_PLATE_BLOCK.get());
        addPlateDrop(consumer, BlockRegistry.GRAY_PLATE_BLOCK.get());
        addPlateDrop(consumer, BlockRegistry.LIGHT_GRAY_PLATE_BLOCK.get());
        addPlateDrop(consumer, BlockRegistry.CYAN_PLATE_BLOCK.get());
        addPlateDrop(consumer, BlockRegistry.PURPLE_PLATE_BLOCK.get());
        addPlateDrop(consumer, BlockRegistry.BLUE_PLATE_BLOCK.get());
        addPlateDrop(consumer, BlockRegistry.BROWN_PLATE_BLOCK.get());
        addPlateDrop(consumer, BlockRegistry.GREEN_PLATE_BLOCK.get());
        addPlateDrop(consumer, BlockRegistry.RED_PLATE_BLOCK.get());
        addPlateDrop(consumer, BlockRegistry.BLACK_PLATE_BLOCK.get());

        // TODO
        add(consumer, BlockRegistry.MIXING_BOWL_BLOCK.get(), c -> oopsItsJustFunctions(c, copyItemsFunc(),
                copyFluidsFunc(),
                copyDataFunc("MixState"),
                copyDataFunc("UsedSize"),
                copyStateFunc(c, MixingBowlBlock.MIX_POSITION)));
        add(consumer, BlockRegistry.MEASURING_CUP_BLOCK.get(), c -> oopsItsJustFunctions(c, copyFluidFunc()));
    }

    protected void addPlateDrop(BiConsumer<ResourceLocation, LootTable.Builder> consumer, Block plateBlock) {
        add(consumer, plateBlock, LootTable.lootTable()
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

    protected void add(BiConsumer<ResourceLocation, LootTable.Builder> consumer, Block block, Function<Block, LootTable.Builder> function) {
        add(consumer, block, function.apply(block));
    }

    protected void add(BiConsumer<ResourceLocation, LootTable.Builder> consumer, Block block, LootTable.Builder lootTableBuilder) {
        consumer.accept(block.getLootTable(), lootTableBuilder);
    }

    public static class LootTableGen extends LootTableProvider {

        public LootTableGen(PackOutput pOutput) {
            super(pOutput, Set.of(), ImmutableList.of(new SubProviderEntry(BlockLootTableGen::new, LootContextParamSets.BLOCK)));
        }
    }
}
