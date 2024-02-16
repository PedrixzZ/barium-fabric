package pedrixzz.barium.mixin.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightmapTextureManager;

import org.lwjgl.opengl.GL11;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

    @Inject(method = "render", at = @At("HEAD"))
    private void onRenderStart(
        BlockRenderLayer layer,
        Matrix4f matrix,
        double cameraX,
        double cameraY,
        double cameraZ,
        LightmapTextureManager lightmapTextureManager,
        int light,
        WorldRenderer.FinishCallback callback
    ) {
        // Insira seu código de otimização OpenGL 2.0 aqui.

        // Exemplo: desabilitar o antialiasing
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRenderEnd(
        BlockRenderLayer layer,
        Matrix4f matrix,
        double cameraX,
        double cameraY,
        double cameraZ,
        LightmapTextureManager lightmapTextureManager,
        int light,
        WorldRenderer.FinishCallback callback
    ) {
        // Reative o antialiasing se você o desabilitou no início.

        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
    }
}
