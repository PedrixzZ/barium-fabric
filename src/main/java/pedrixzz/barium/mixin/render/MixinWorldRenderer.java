package pedrixzz.barium.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

    @Shadow
    private int vertexCount;

    @Shadow
    private boolean isDrawing;

    @Inject
    private void start(int mode) {
        if (isDrawing) {
            throw new IllegalStateException("Renderer already drawing!");
        }
        isDrawing = true;
        vertexCount = 0;
        GL11.glBegin(mode);
    }

    @Inject
    private void draw() {
        if (!isDrawing) {
            throw new IllegalStateException("Renderer not drawing!");
        }
        tessellator.getBuffer().rewind();
        while (tessellator.getBuffer().hasRemaining()) {
            VertexFormat format = tessellator.getVertexFormat();
            int position = 0;
            for (int element : format.getElements()) {
                switch (element) {
                    case VertexFormatElement.POSITION:
                        GL11.glVertex3f(
                            tessellator.getBuffer().getShort(position),
                            tessellator.getBuffer().getShort(position + 2),
                            tessellator.getBuffer().getShort(position + 4)
                        );
                        position += 6;
                        break;
                    case VertexFormatElement.COLOR:
                        GL11.glColor4ub(
                            tessellator.getBuffer().getByte(position),
                            tessellator.getBuffer().getByte(position + 1),
                            tessellator.getBuffer().getByte(position + 2),
                            tessellator.getBuffer().getByte(position + 3)
                        );
                        position += 4;
                        break;
                    case VertexFormatElement.UV:
                        GL11.glTexCoord2f(
                            tessellator.getBuffer().getShort(position) / 16.0f,
                            tessellator.getBuffer().getShort(position + 2) / 16.0f
                        );
                        position += 4;
                        break;
                    case VertexFormatElement.NORMAL:
                        GL11.glNormal3f(
                            tessellator.getBuffer().getByte(position),
                            tessellator.getBuffer().getByte(position + 1),
                            tessellator.getBuffer().getByte(position + 2)
                        );
                        position += 3;
                        break;
                }
            }
            vertexCount++;
        }
    }

    @Inject
    private void finish() {
        if (!isDrawing) {
            throw new IllegalStateException("Renderer not drawing!");
        }
        isDrawing = false;
        GL11.glEnd();
    }
}
