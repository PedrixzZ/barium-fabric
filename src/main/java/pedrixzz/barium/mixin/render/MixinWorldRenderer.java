package pedrixzz.barium.mixin.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.mixin.Mixin;
import net.fabricmc.api.mixin.injection.At;
import net.fabricmc.api.mixin.injection.Inject;
import net.fabricmc.api.mixin.injection.Redirect;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.chunk.Chunk;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockLightSubtractions;
import net.minecraft.world.LightmapTextureManager;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import org.lwjgl.opengl.GL11;

@Mixin(Chunk.class)
public abstract class ChunkMixin {

    @Environment(EnvType.CLIENT)
    @Inject(method = "render", at = @At("HEAD"))
    private void onRenderStart(Frustum frustum, double camX, double camY, double camZ, Matrix4f projectionMatrix, Matrix4f viewMatrix, int renderPass, CallbackInfo info) {
        // Otimização: Desativar a renderização de faces ocultas
        GlStateManager.disableCull();

        // Otimização: Desativar o teste de profundidade
        GlStateManager.disableDepthTest();

        // Otimização: Usar alpha blending para translucidez
        GlStateManager.enableAlphaTest();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "getLightmap", at = @At("RETURN"))
    private void onGetLightmap(BlockState state, int x, int y, int z, BlockLightSubtractions subtractions, @Nullable LightmapTextureManager lightmap, CallbackInfoReturnable<Int2> info) {
        // Otimização: Usar um valor de luzmap constante para blocos opacos
        if (state.isOpaque()) {
            info.setReturnValue(new Int2(15, 15));
        }
    }

    @Environment(EnvType.CLIENT)
    @Redirect(method = "isOpaque", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOpaque()Z"))
    private boolean onIsOpaque(BlockState state) {
        // Otimização: Retornar sempre true para blocos que não precisam de translucidez
        return true;
    }

}
