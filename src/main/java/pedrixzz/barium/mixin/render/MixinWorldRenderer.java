package pedrixzz.barium.mixin.render;

import net.minecraft.block.BlockState;
import net.minecraft.block.render.BlockRenderView;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Inject(method = "renderWorld", at = @At("HEAD"))
    private void onRenderWorldStart(
        @Nullable IBlockState state,
        BlockRenderView entityView,
        BlockStateView blockStateView,
        WorldRenderer worldRenderer,
        LightmapTextureManager lightmapTextureManager,
        Matrix4f matrix4f,
        int i,
        int j,
        int k,
        Camera camera) {
        // Insira seu código de otimização aqui
        // Por exemplo, você pode desabilitar a renderização de entidades
        // ou reduzir a qualidade da luz.
    }

    @Inject(method = "renderWorld", at = @At("TAIL"))
    private void onRenderWorldEnd(
        @Nullable IBlockState state,
        BlockRenderView entityView,
        BlockStateView blockStateView,
        WorldRenderer worldRenderer,
        LightmapTextureManager lightmapTextureManager,
        Matrix4f matrix4f,
        int i,
        int j,
        int k,
        Camera camera) {
        // Insira seu código de limpeza aqui
    }
}
