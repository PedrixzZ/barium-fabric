package pedrixzz.barium.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.LightmapTextureManager;
import net.minecraft.client.renderer.Framebuffer;
import net.minecraft.client.renderer.IRenderTypeBuffer.Impl;
import net.minecraft.client.world.World;
import net.minecraft.util.math.MatrixStack;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Shadow
    public abstract void originalRender();

    @Overwrite
    public void render() {
        WorldRenderer.render();

        // Chamar o método original se necessário (opcional)
        // originalRender();
    }

    @Inject(method = "renderWorld", at = @At("HEAD"))
    private void injectRenderWorld(World world, float partialTicks, int pass, MatrixStack matrixStack, IRenderTypeBuffer.Impl buffer, LightmapTextureManager lightmapTextureManager, Framebuffer framebuffer) {
        // Injetar código antes do renderizador do mundo
    }

    @Inject(method = "renderWorld", at = @At("TAIL"))
    private void injectRenderWorldEnd(World world, float partialTicks, int pass, MatrixStack matrixStack, IRenderTypeBuffer.Impl buffer, LightmapTextureManager lightmapTextureManager, Framebuffer framebuffer) {
        // Injetar código após o renderizador do mundo
    }
}
