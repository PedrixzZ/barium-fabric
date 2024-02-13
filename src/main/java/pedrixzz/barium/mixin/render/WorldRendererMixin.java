package pedrixzz.barium.mixin.render;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Overwrite;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Shadow
    private BufferBuilder bufferBuilder;

    @Inject
    public WorldRendererMixin(WorldRenderer worldRenderer) {
        // ...
    }

    @Overwrite
    public void render(World world, Entity viewEntity, IBlockState state, BlockPos pos, Matrix4f matrix4f, float f, float f1, float f2, float f3) {
        // Otimização de vértices
        int vertexCount = bufferBuilder.getVertexCount();
        if (vertexCount > 0) {
            VertexFormat vertexFormat = bufferBuilder.getVertexFormat();
            int stride = vertexFormat.getVertexSize();

            // Combine vértices adjacentes com a mesma cor e textura
            for (int i = 0; i < vertexCount - 1; i++) {
                int offsetA = i * stride;
                int offsetB = (i + 1) * stride;

                // Verifique se os vértices têm a mesma cor e textura
                if (bufferBuilder.getColor(offsetA).equals(bufferBuilder.getColor(offsetB)) &&
                        bufferBuilder.getTexCoord(offsetA).equals(bufferBuilder.getTexCoord(offsetB))) {
                    // Combine os vértices
                    bufferBuilder.setVertex(offsetA, bufferBuilder.getPosition(offsetA), bufferBuilder.getNormal(offsetA),
                            bufferBuilder.getColor(offsetA), bufferBuilder.getTexCoord(offsetA), bufferBuilder.getLightmap(offsetA),
                            bufferBuilder.getOverlay(offsetA));

                    // Remova o vértice duplicado
                    bufferBuilder.removeVertex(i + 1);
                    vertexCount--;
                    i--;
                }
            }
        }

        // Otimização de shaders
        // ...

        // Multithreading
        // ...

        bufferBuilder.endVertex();
    }

    // Otimização de alocações
    // ...

    // ...
}

