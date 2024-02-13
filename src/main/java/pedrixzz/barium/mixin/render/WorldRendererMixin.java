package pedrixzz.barium.mixin.render;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.joml.Matrix4f
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Shadow private int chunkIndexY;
    @Shadow private int chunkIndexZ;
    @Shadow private int chunkIndexX;

    @Inject(method = "render", at = @At("HEAD"))
    private void onRenderStart(RenderLayer layer, Frustum frustum, double cameraX, double cameraY, double cameraZ, MatrixStack projectionMatrix, MatrixStack viewMatrix, Entity viewEntity, boolean isWorldRenderer, int chunkIndexX, int chunkIndexY, int chunkIndexZ, CallbackInfo info) {
        // Desativar a renderização de blocos transparentes se não forem visíveis
        if (layer == RenderLayer.TRANSLUCENT) {
            if (!frustum.isBoundingBoxVisible(AxisAlignedBB.of(chunkIndexX * 16, chunkIndexY * 16, chunkIndexZ * 16, (chunkIndexX + 1) * 16, (chunkIndexY + 1) * 16, (chunkIndexZ + 1) * 16))) {
                info.cancel();
                return;
            }
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRenderEnd(RenderLayer layer, Frustum frustum, double cameraX, double cameraY, double cameraZ, MatrixStack projectionMatrix, MatrixStack viewMatrix, Entity viewEntity, boolean isWorldRenderer, int chunkIndexX, int chunkIndexY, int chunkIndexZ, CallbackInfo info) {
        // Limpar a lista de vértices após a renderização
        if (layer != RenderLayer.TRANSLUCENT) {
            WorldRendererAccessor accessor = (WorldRendererAccessor) this;
            accessor.getVertexList().clear();
        }
    }
}
