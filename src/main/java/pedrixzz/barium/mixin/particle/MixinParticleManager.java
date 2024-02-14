package pedrixzz.barium.mixin.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mixin(ParticleManager.class)
@Environment(EnvType.CLIENT)
public class MixinParticleManager {
    @Shadow @Final private Map<ParticleTextureSheet, Queue<Particle>> particles;

    private boolean hasParticles = false;

    @Inject(method = "renderParticles", at = @At("HEAD"), cancellable = true)
    private void onRender(MatrixStack matrices, Immediate vertexConsumers, LightmapTextureManager lightmapTextureManager, Camera camera, float tickDelta, CallbackInfo ci) {
        if (!hasParticles) {
            ci.cancel();
            return;
        }

        for (Map.Entry<ParticleTextureSheet, Queue<Particle>> entry : particles.entrySet()) {
            ParticleTextureSheet textureSheet = entry.getKey();
            Queue<Particle> particleQueue = entry.getValue();

            // Utilize ConcurrentLinkedQueue for thread-safe iteration and modification.
            while (!particleQueue.isEmpty()) {
                Particle particle = particleQueue.poll();

                if (particle.isAlive()) {
                 //   particle.render(matrices, vertexConsumers, lightmapTextureManager, camera, tickDelta);
                }
            }
        }
    }

    @Inject(method = "addParticle", at = @At("HEAD"))
    private void onAddParticle(Particle particle, CallbackInfo ci) {
        hasParticles = true;
    }

    @Inject(method = "clearParticles", at = @At("HEAD"))
    private void onClearParticles(CallbackInfo ci) {
        hasParticles = false;
    }
}
