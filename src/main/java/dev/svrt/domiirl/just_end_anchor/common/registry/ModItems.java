package dev.svrt.domiirl.just_end_anchor.common.registry;

import dev.svrt.domiirl.just_end_anchor.EndAnchorMod;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;

public class ModItems {

	public static Item END_ANCHOR;

	public static void init() {
		END_ANCHOR = register(ModBlocks.END_ANCHOR);

		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> {
			entries.accept(END_ANCHOR);
		});
	}

	private static Item register(Block block) {
		return register(BuiltInRegistries.BLOCK.getKey(block).getPath(), properties -> new BlockItem(block, properties), new Item.Properties());
	}

	public static Item register(String name, Function<Item.Properties, Item> itemFactory, Item.Properties properties) {
		ResourceLocation itemId = ResourceLocation.fromNamespaceAndPath(EndAnchorMod.MOD_ID, name);
		Item item = itemFactory.apply(properties.setId(keyOfItem(name)));
		Registry.register(BuiltInRegistries.ITEM, itemId, item);
		return item;
	}

	private static ResourceKey<Item> keyOfItem(String name) {
		return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(EndAnchorMod.MOD_ID, name));
	}
}
