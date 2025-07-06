package dev.svrt.domiirl.just_end_anchor;

import dev.svrt.domiirl.just_end_anchor.common.registry.ModBlockEntities;
import dev.svrt.domiirl.just_end_anchor.common.registry.ModBlocks;
import dev.svrt.domiirl.just_end_anchor.common.registry.ModItems;
import net.fabricmc.api.ModInitializer;

public class EndAnchorMod implements ModInitializer {

	public static final String MOD_ID = "just_end_anchor";

	@Override
	public void onInitialize() {
		ModBlocks.init();
		ModItems.init();
		ModBlockEntities.init();
	}

}
