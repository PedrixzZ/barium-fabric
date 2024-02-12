package pedrixzz.barium.mixin.render;

import pedrixzz.barium.client.render.MinecraftRenderer;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftRenderer.class)
public abstract class MinecraftRendererMixin {
    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(CallbackInfo ci) {
        // Chamar o método "renderWorld" do MinecraftRenderer
        MinecraftRenderer renderer = (MinecraftRenderer) (Object) this;
        renderer.renderWorld();
    }
}
