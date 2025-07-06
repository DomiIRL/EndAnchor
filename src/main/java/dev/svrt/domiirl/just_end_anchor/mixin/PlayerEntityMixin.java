package dev.svrt.domiirl.just_end_anchor.mixin;

import com.mojang.authlib.GameProfile;
import dev.svrt.domiirl.just_end_anchor.common.blocks.EndAnchorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ServerPlayer.class)
public abstract class PlayerEntityMixin extends Player {

	public PlayerEntityMixin(Level level, GameProfile gameProfile) {
		super(level, gameProfile);
	}

	@Inject(method = "findRespawnAndUseSpawnBlock", at = @At(value = "HEAD"), cancellable = true)
	private static void findRespawn(ServerLevel world, ServerPlayer.RespawnConfig respawnConfig, boolean death, CallbackInfoReturnable<Optional<ServerPlayer.RespawnPosAngle>> cir) {
		if (!death) {
			return;
		}

		BlockPos pos = respawnConfig.pos();
		BlockState blockState = world.getBlockState(pos);
		Block block = blockState.getBlock();


		if (block instanceof EndAnchorBlock && blockState.getValue(EndAnchorBlock.CHARGE) > 0 && EndAnchorBlock.isEnd(world)) {
			Optional<Vec3> optional = RespawnAnchorBlock.findStandUpPosition(EntityType.PLAYER, world, pos);
			if (optional.isPresent()) {
				world.setBlock(pos, blockState.setValue(EndAnchorBlock.CHARGE, blockState.getValue(EndAnchorBlock.CHARGE) - 1), Block.UPDATE_ALL);
			}

			cir.setReturnValue(optional.map((vec3) -> ServerPlayer.RespawnPosAngle.of(vec3, pos)));
		}
	}

}
