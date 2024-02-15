package pedrixzz.barium.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.client.render.WorldRenderer;

import static org.lwjgl.opengl.GL11.*;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

    @Shadow private static int CHUNK_SIZE;

    @Inject
    public MixinWorldRenderer(WorldRenderer worldRenderer) {
    }

    @ModifyArg(method = "render", at = 0)
    private int modifyRender(int pass) {
        if (pass == 0) {
            // Ativar OpenGL 2.0
            GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
            GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
            GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        }
        return pass;
    }

    @ModifyArg(method = "renderBlock", at = 1)
    private int modifyRenderBlock(int vertexCount) {
        // Otimizar renderização de blocos
        if (vertexCount > CHUNK_SIZE * CHUNK_SIZE * 6) {
            return CHUNK_SIZE * CHUNK_SIZE * 6;
        }
        return vertexCount;
    }

}
