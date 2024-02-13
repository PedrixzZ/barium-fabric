package pedrixzz.barium.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.world.World;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Shadow
    public abstract void originalRender();

    public void render() {
        WorldRenderer.render(MatrixStack, float, long, boolean, Camera,GameRenderer, LightmapTextureManager, Matrix4f);
        

        // Chamar o método original se necessário (opcional)
        // originalRender();
    }

    @Inject(method = "renderWorld", at = @At("HEAD"))
    private void injectRenderWorld(World world, float partialTicks, int pass, MatrixStack matrixStack, LightmapTextureManager lightmapTextureManager, Framebuffer framebuffer) {
        // Injetar código antes do renderizador do mundo
    }

    @Inject(method = "renderWorld", at = @At("TAIL"))
    private void injectRenderWorldEnd(World world, float partialTicks, int pass, MatrixStack matrixStack, LightmapTextureManager lightmapTextureManager, Framebuffer framebuffer) {
        // Injetar código após o renderizador do mundo
    }
}
