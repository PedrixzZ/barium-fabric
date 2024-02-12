package pedrixzz.barium.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import pedrixzz.barium.client.render.MinecraftRenderer;

@Mixin({MinecraftRenderer.class})
public abstract class MinecraftRendererMixin {
   public void render() {
   }
}
