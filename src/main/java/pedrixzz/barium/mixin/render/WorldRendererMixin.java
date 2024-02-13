package pedrixzz.barium.mixin.render;

import net.minecraft.world.BlockRenderView;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.WorldRenderer;
import org.joml.Matrix4f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void onRenderStart(BlockRenderView view, BlockState state, BlockPos pos, Matrix4f matrix, BufferBuilder buffer, boolean checkSides, CallbackInfo info) {
        if (state.isSolid()) {
            buffer.begin(VertexFormat.DEFAULT, Tessellator.State.POSITION_COLOR);
            buffer.vertex(pos.getX(), pos.getY(), pos.getZ(), Color.WHITE.getPacked());
            buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ(), Color.WHITE.getPacked());
            buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ(), Color.WHITE.getPacked());
            buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ(), Color.WHITE.getPacked());
            buffer.end();
            info.cancel();
            return;
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRenderEnd(BlockRenderView view, BlockState state, BlockPos pos, Matrix4f matrix, BufferBuilder buffer, boolean checkSides, CallbackInfo info) {
    }
}
