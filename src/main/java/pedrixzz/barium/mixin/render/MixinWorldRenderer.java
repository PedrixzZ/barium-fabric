package pedrixzz.barium.mixin.render;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class PerformanceMixin {

    @Inject
    @At(value = "HEAD", target = "renderWorld(Lnet/minecraft/client/render/Camera; FLnet/minecraft/world/World; Lnet/minecraft/util/math/BlockPos; Lnet/minecraft/client/util/math/MatrixStack; F)V")
    private void onRenderWorldStart(Camera camera, float partialTicks, World world, BlockPos pos, MatrixStack matrices, float alpha, Callback callback) {
        // Insira seu código de otimização aqui
    }

    @Inject
    @At(value = "TAIL", target = "renderWorld(Lnet/minecraft/client/render/Camera; FLnet/minecraft/world/World; Lnet/minecraft/util/math/BlockPos; Lnet/minecraft/client/util/math/MatrixStack; F)V")
    private void onRenderWorldEnd(Camera camera, float partialTicks, World world, BlockPos pos, MatrixStack matrices, float alpha, Callback callback) {
        // Insira seu código de limpeza aqui
    }
}
