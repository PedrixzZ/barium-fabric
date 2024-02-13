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

    @Inject(method = "render", at = @At("HEAD"))
    private void onRenderStart(BlockRenderView view, BlockState state, BlockPos pos, Matrix4f matrix, BufferBuilder buffer, boolean checkSides, CallbackInfo info) {
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRenderEnd(BlockRenderView view, BlockState state, BlockPos pos, Matrix4f matrix, BufferBuilder buffer, boolean checkSides, CallbackInfo info) {
    }
}
