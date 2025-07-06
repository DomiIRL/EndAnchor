package dev.svrt.domiirl.just_end_anchor.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.svrt.domiirl.just_end_anchor.common.blocks.EndAnchorBlock;
import dev.svrt.domiirl.just_end_anchor.common.entities.EndAnchorBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class EndAnchorBlockEntityRenderer<T extends EndAnchorBlockEntity> extends TheEndPortalRenderer<T> {

	public EndAnchorBlockEntityRenderer(Context ctx) {
		super(ctx);
	}

	@Override
	public void render(T endPortalBlockEntity, float f, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, int j, Vec3 vec3) {
		if (endPortalBlockEntity.getBlockState().getValue(EndAnchorBlock.CHARGE) > 0) {
			Matrix4f matrix4f = matrixStack.last().pose();
			this.renderSide(endPortalBlockEntity, matrix4f, vertexConsumerProvider.getBuffer(this.renderType()), 0.1238F, 0.8762F, 1, 1, 0.8762F, 0.8762F, 0.1238F, 0.1238F, Direction.UP);
		}
	}

	private void renderSide(T entity, Matrix4f matrix4f, VertexConsumer vertexConsumer, float f, float g, float h, float i, float j, float k, float l, float m, Direction direction) {
		if (entity.shouldRenderFace(direction)) {
			vertexConsumer.addVertex(matrix4f, f, h, j);
			vertexConsumer.addVertex(matrix4f, g, h, k);
			vertexConsumer.addVertex(matrix4f, g, i, l);
			vertexConsumer.addVertex(matrix4f, f, i, m);
		}

	}
}
