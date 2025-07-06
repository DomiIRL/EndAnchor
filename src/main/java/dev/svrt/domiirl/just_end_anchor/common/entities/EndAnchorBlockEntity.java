package dev.svrt.domiirl.just_end_anchor.common.entities;

import dev.svrt.domiirl.just_end_anchor.common.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class EndAnchorBlockEntity extends TheEndPortalBlockEntity {

	protected EndAnchorBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}

	public EndAnchorBlockEntity(BlockPos blockPos, BlockState blockState) {
		this(ModBlockEntities.END_ANCHOR, blockPos, blockState);
	}
}
