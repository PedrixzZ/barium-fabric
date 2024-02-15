package pedrixzz.barium.mixin.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRendererAccessor;
import net.minecraft.client.renderer.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
@Environment(EnvType.CLIENT)
public class MixinWorldRenderer {

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(CallbackInfo info) {
        // Força o uso do OpenGL 2.0
        ((WorldRendererAccessor) this).getRenderGlobal().setShader(
            ((WorldRendererAccessor) this).getRenderGlobal().getShaderResourceManager().getShader("minecraft:legacy")
        );
    }
}
