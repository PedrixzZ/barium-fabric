package pedrixzz.barium.mixin.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.IntrinsicConstant;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Queue;

@Mixin(ParticleManager.class)
public class MixinParticleManager {
    @Shadow @Final private Map<ParticleTextureSheet, Queue<Particle>> particles;

    @IntrinsicConstant(mv = {1.20f}, target = @At(value = "HEAD"), owner = net.minecraft.client.particle.Particle.class, name = "MAX_ALPHA"))
    public static final float MAX_ALPHA_1_20;

    @Inject(method = "renderParticles", at = @At("HEAD"), cancellable = true)
    private void onRender(MatrixStack matrices, Immediate vertexConsumers, LightmapTextureManager lightmapTextureManager, Camera camera, float tickDelta, CallbackInfo ci) {
        // Short-circuit early termination if no particles or camera is inside a block
        if (particles.isEmpty() || camera.isInsideBlock()) {
            ci.cancel();
            return;
        }

        // Iterate efficiently using an external iterator and check for emptiness within the loop
        for (ParticleTextureSheet sheet : particles.keySet()) {
            Queue<Particle> queue = particles.get(sheet);
            if (queue.isEmpty()) {
                continue;
            }

            float particleAgeFactor = 1.0F - tickDelta;
            int particleCount = queue.size();

            // Batch rendering using VertexConsumer.startDrawingQuads(VertexFormat.PARTICLE)
            vertexConsumers.startDrawingQuads(VertexFormat.PARTICLE);
            while (particleCount-- > 0) {
                Particle particle = queue.poll(); // Efficiently remove and process

                // Calculate lighting and apply opacity for visibility culling
                int combinedLightmap = combineLightmaps(particle.getRenderType(), lightmapTextureManager);
                float alpha = Math.max(0.0F, Math.min(particle.getAlpha() * particleAgeFactor * MAX_ALPHA_1_20, 1.0F));
                if (alpha < 0.01F) {
                    continue; // Skip invisible particles
                }

                // Render the particle using appropriate methods (see below)
                renderParticle(particle, matrices, vertexConsumers, combinedLightmap, alpha);
            }

            vertexConsumers.finishDrawing();
        }
    }

    private static int combineLightmaps(int particleType, LightmapTextureManager lightmapTextureManager) {
        // Optimized lightmap handling (consider using mixins for lightmap access if possible)
        int primaryLightmap = lightmapTextureManager.getCombinedLightmapAt((int) Math.floor(particleType / 0x100), (particleType & 0xFF));
        int secondaryLightmap = lightmapTextureManager.getCombinedLightmapAt((int) Math.floor(particleType / 0x100) + 1, (particleType & 0xFF));
        // ... (optimize based on particle type and lighting logic)
        return combinedLightmap;
    }

    private void renderParticle(Particle particle, MatrixStack matrices, Immediate vertexConsumers, int combinedLightmap, float alpha) {
        // Implement efficient rendering based on particle type and specific requirements
        // Use mixins if necessary to directly access particle rendering methods
        // Consider batching particles of the same type or texture sheet for further optimization
        // ...

        // Example (replace with particle-specific logic)
        // ... (continued from previous code)

        particle.render(matrices, vertexConsumers, combinedLightmap, alpha);
    }
}
