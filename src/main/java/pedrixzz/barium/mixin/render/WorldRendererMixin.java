package pedrixzz.barium.mixin.render;

import net.minecraft.world.BlockRenderView;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.Colors;
import org.joml.Matrix4f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    private static final boolean RENDER_DEBUG_OUTLINE = false;

    @Inject(method = "render", at = @At("HEAD"))
    private void onRenderStart(BlockRenderView view, BlockState state, BlockPos pos, Matrix4f matrix, BufferBuilder buffer, boolean checkSides, CallbackInfo info) {
        if (RENDER_DEBUG_OUTLINE) {
            renderDebugOutline(view, state, pos, matrix, buffer);
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRenderEnd(BlockRenderView view, BlockState state, BlockPos pos, Matrix4f matrix, BufferBuilder buffer, boolean checkSides, CallbackInfo info) {
        if (RENDER_DEBUG_OUTLINE) {
            renderDebugOutline(view, state, pos, matrix, buffer);
        }
    }

    private void renderDebugOutline(BlockRenderView view, BlockState state, BlockPos pos, Matrix4f matrix, BufferBuilder buffer) {
        @Nullable
        public @Nullable World getWorld()
        BlockEntity blockEntity = world.getBlockEntity(pos);
    }

    private void renderOutline(BufferBuilder buffer, BlockPos pos, int color) {
        float xMin = pos.getX() - 0.005f;
        float yMin = pos.getY() - 0.005f;
        float zMin = pos.getZ() - 0.005f;
        float xMax = pos.getX() + 1.005f;
        float yMax = pos.getY() + 1.005f;
        float zMax = pos.getZ() + 1.005f;

        buffer.begin(VertexFormat.DrawMode.LINES);
        buffer.vertex(xMin, yMin, zMin).color(color).next();
        buffer.vertex(xMax, yMin, zMin).color(color).next();
        buffer.vertex(xMax, yMin, zMax).color(color).next();
        buffer.vertex(xMin, yMin, zMax).color(color).next();
        buffer.vertex(xMin, yMin, zMin).color(color).next();
        buffer.vertex(xMin, yMax, zMin).color(color).next();
        buffer.vertex(xMax, yMax, zMin).color(color).next();
        buffer.vertex(xMax, yMin, zMin).color(color).next();
        buffer.vertex(xMax, yMax, zMax).color(color).next();
        buffer.vertex(xMax, yMin, zMax).color(color).next();
        buffer.vertex(xMin, yMax, zMax).color(color).next();
        buffer.vertex(xMin, yMin, zMax).color(color).next();
        buffer.vertex(xMin, yMax, zMin).color(color).next();
        buffer.vertex(xMin, yMax, zMax).color(color).next();
        buffer.vertex(xMax, yMax, zMax).color(color).next();
        buffer.end();
    }
}
