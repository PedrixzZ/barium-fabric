package pedrixzz.barium.mixin.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

@Mixin(ParticleManager.class)
public class MixinParticleManager {
    @Shadow @Final private Map<ParticleTextureSheet, Queue<Particle>> particles;

    private final List<Particle> particlesToRemove = new ArrayList<>();

    @Inject(method = "renderParticles", at = @At("HEAD"))
    private void onRenderStart(MatrixStack matrices, Immediate vertexConsumers, LightmapTextureManager lightmapTextureManager, Camera camera, float tickDelta, CallbackInfo ci) {
        for (Map.Entry<ParticleTextureSheet, Queue<Particle>> entry : particles.entrySet()) {
            ParticleTextureSheet textureSheet = entry.getKey();
            Queue<Particle> particleQueue = entry.getValue();

            while (!particleQueue.isEmpty()) {
                Particle particle = particleQueue.poll();
           //     if (particle.shouldRender()) {
           //         particle.render(matrices, vertexConsumers, lightmapTextureManager, camera, tickDelta);
                } else {
                    particlesToRemove.add(particle);
                }
            }
        }

        for (Particle particle : particlesToRemove) {
            particle.markDead();
        }

        particlesToRemove.clear();
    }
}
