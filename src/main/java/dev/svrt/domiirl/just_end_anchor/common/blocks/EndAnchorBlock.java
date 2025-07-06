package dev.svrt.domiirl.just_end_anchor.common.blocks;

import dev.svrt.domiirl.just_end_anchor.common.entities.EndAnchorBlockEntity;
import dev.svrt.domiirl.just_end_anchor.common.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EndAnchorBlock extends RespawnAnchorBlock implements EntityBlock {

	public EndAnchorBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected @NotNull InteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		if (isRespawnFuel(itemStack) && canBeCharged(blockState)) {
			RespawnAnchorBlock.charge(player, level, blockPos, blockState);
			itemStack.consume(1, player);
			return InteractionResult.SUCCESS;
		}
		if (interactionHand == InteractionHand.MAIN_HAND && isRespawnFuel(player.getItemInHand(InteractionHand.OFF_HAND)) && canBeCharged(blockState)) {
			return InteractionResult.PASS;
		}
		return InteractionResult.TRY_WITH_EMPTY_HAND;
	}

	@Override
	protected @NotNull InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
		if (blockState.getValue(CHARGE) == 0) {
			return InteractionResult.PASS;
		}
		if (isEnd(level)) {
			if (player instanceof ServerPlayer) {
				ServerPlayer serverPlayer = (ServerPlayer)player;
				ServerPlayer.RespawnConfig respawnConfig = serverPlayer.getRespawnConfig();
				ServerPlayer.RespawnConfig respawnConfig2 = new ServerPlayer.RespawnConfig(level.dimension(), blockPos, 0.0f, false);
				if (respawnConfig == null || !respawnConfig.isSamePosition(respawnConfig2)) {
					serverPlayer.setRespawnPosition(respawnConfig2, true);
					level.playSound(null, (double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5, SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundSource.BLOCKS, 1.0f, 1.0f);
					return InteractionResult.SUCCESS_SERVER;
				}
			}
			return InteractionResult.CONSUME;
		}
		if (!level.isClientSide) {
			this.explode(blockState, level, blockPos);
		}
		return InteractionResult.SUCCESS;
	}

	private void explode(BlockState blockState, Level level, final BlockPos explodedPos) {
		level.removeBlock(explodedPos, false);
		boolean bl = Direction.Plane.HORIZONTAL.stream().map(explodedPos::relative).anyMatch(blockPos -> RespawnAnchorBlock.isWaterThatWouldFlow(blockPos, level));
		final boolean bl2 = bl || level.getFluidState(explodedPos.above()).is(FluidTags.WATER);
		ExplosionDamageCalculator explosionDamageCalculator = new ExplosionDamageCalculator() {
			@Override
			public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
				if (blockPos.equals(explodedPos) && bl2) {
					return Optional.of(Blocks.WATER.getExplosionResistance());
				}
				return super.getBlockExplosionResistance(explosion, blockGetter, blockPos, blockState, fluidState);
			}
		};
		Vec3 vec3 = explodedPos.getCenter();
		level.explode(null, level.damageSources().badRespawnPointExplosion(vec3), explosionDamageCalculator, vec3, 5.0f, true, Level.ExplosionInteraction.BLOCK);

		if (level instanceof ServerLevel serverLevel && serverLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {

			for (int i = 0; i < level.random.nextInt(12 - 6) + 6; i++) {
				Endermite endermiteEntity = new Endermite(EntityType.ENDERMITE, level);
				endermiteEntity.setPosRaw(explodedPos.getX() + 0.5, explodedPos.getY(), explodedPos.getZ() + 0.5);
				endermiteEntity.setYRot(0);
				endermiteEntity.setXRot(0);
				endermiteEntity.setDeltaMovement(new Vec3(level.random.nextDouble() * (level.random.nextBoolean() ? -1 : 1), level.random.nextDouble(), level.random.nextDouble() * (level.random.nextBoolean() ? -1 : 1)));
				level.addFreshEntity(endermiteEntity);
			}
		}
	}

	public static boolean isEnd(Level world) {
		return world.dimension().equals(Level.END);
	}

	private boolean isRespawnFuel(ItemStack itemStack) {
		return itemStack.is(Items.ENDER_PEARL);
	}

	private boolean canBeCharged(BlockState blockState) {
		return blockState.getValue(CHARGE) < 4;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new EndAnchorBlockEntity(pos, state);
	}

	static {
		DispenserBlock.registerBehavior(Items.ENDER_PEARL, new OptionalDispenseItemBehavior() {

			@Override
			public ItemStack execute(net.minecraft.core.dispenser.BlockSource blockSource, ItemStack itemStack) {
				Direction direction = blockSource.state().getValue(DispenserBlock.FACING);
				BlockPos blockPos = blockSource.pos().relative(direction);
				ServerLevel level = blockSource.level();
				BlockState blockState = level.getBlockState(blockPos);
				this.setSuccess(true);
				if (blockState.is(ModBlocks.END_ANCHOR)) {
					if (blockState.getValue(CHARGE) != 4) {
						charge(null, level, blockPos, blockState);
						itemStack.shrink(1);
					} else {
						this.setSuccess(false);
					}
					return itemStack;
				}
				return super.execute(blockSource, itemStack);
			}
		});
	}

}
