package dev.svrt.domiirl.just_end_anchor.client;

import dev.svrt.domiirl.just_end_anchor.client.renderer.EndAnchorBlockEntityRenderer;
import dev.svrt.domiirl.just_end_anchor.common.registry.ModBlockEntities;
import dev.svrt.domiirl.just_end_anchor.common.registry.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;


public class EndAnchorModClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.putBlocks(ChunkSectionLayer.CUTOUT, ModBlocks.END_ANCHOR);
		BlockEntityRenderers.register(ModBlockEntities.END_ANCHOR, EndAnchorBlockEntityRenderer::new);
	}

}
