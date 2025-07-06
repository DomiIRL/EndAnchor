package dev.svrt.domiirl.just_end_anchor.common.registry;

import dev.svrt.domiirl.just_end_anchor.EndAnchorMod;
import dev.svrt.domiirl.just_end_anchor.common.blocks.EndAnchorBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Function;

public class ModBlocks {

	public static Block END_ANCHOR;

	public static void init() {
		END_ANCHOR = register(
			"end_anchor",
      EndAnchorBlock::new,
			BlockBehaviour.Properties.of()
				.mapColor(MapColor.COLOR_BLACK)
				.instrument(NoteBlockInstrument.BASEDRUM)
				.requiresCorrectToolForDrops()
				.strength(50.0f, 1200.0f)
				.lightLevel(blockState -> RespawnAnchorBlock.getScaledChargeLevel(blockState, 15))
		);
	}

	private static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties) {
		// Create a registry key for the block
		ResourceKey<Block> blockKey = keyOfBlock(name);
		// Create the block instance
		Block block = blockFactory.apply(properties.setId(blockKey));

		return Registry.register(BuiltInRegistries.BLOCK, blockKey.location(), block);
	}

	private static ResourceKey<Block> keyOfBlock(String name) {
		return ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(EndAnchorMod.MOD_ID, name));
	}
}
