package pedrixzz.barium.mixin.render;

import pedrixzz.barium.client.render.MinecraftRenderer;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftRenderer.class)
public abstract class MinecraftRendererMixin {

    @Inject
    private void inject(RendererAccessor renderer) {
    }

    public void render() {

    }

    @Redirect(method = "updateVertexBuffer", at = @At(value = "INVOKE", target = "java/util/List.addAll(Ljava/util/Collection;)Z"))
    private boolean onAddAllVertices(List<Vertex> vertices, Collection<Vertex> newVertices) {

        return true;
    }
}
