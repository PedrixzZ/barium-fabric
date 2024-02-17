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

import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

@Mixin(ParticleManager.class)
public class MixinParticleManager {
    @Shadow @Final private Map<ParticleTextureSheet, Queue<Particle>> particles;

    @Inject(method = "renderParticles", at = @At("HEAD"), cancellable = true)
    private void onRender(MatrixStack matrices, Immediate vertexConsumers, LightmapTextureManager lightmapTextureManager, Camera camera, float tickDelta, CallbackInfo ci) {
        // Filtra as texturas com partículas vazias
        Iterator<Map.Entry<ParticleTextureSheet, Queue<Particle>>> textureIterator = particles.entrySet().iterator();
        while (textureIterator.hasNext()) {
            Map.Entry<ParticleTextureSheet, Queue<Particle>> entry = textureIterator.next();
            if (entry.getValue().isEmpty()) {
                textureIterator.remove();
            }
        }

        // Se nenhuma textura tiver partículas, cancela a renderização
        if (particles.isEmpty()) {
            ci.cancel();
            return;
        }

        // Renderiza as partículas das texturas restantes
        for (Map.Entry<ParticleTextureSheet, Queue<Particle>> entry : particles.entrySet()) {
            ParticleTextureSheet textureSheet = entry.getKey();
            Queue<Particle> particleQueue = entry.getValue();

            textureSheet.bind();
            renderParticleQueue(matrices, vertexConsumers, lightmapTextureManager, camera, tickDelta, particleQueue);
        }
    }

    private void renderParticleQueue(MatrixStack matrices, Immediate vertexConsumers, LightmapTextureManager lightmapTextureManager, Camera camera, float tickDelta, Queue<Particle> particleQueue) {
        // Otimiza iteração com pool de objetos
        Iterator<Particle> particleIterator = particleQueue.iterator();
        while (particleIterator.hasNext()) {
            Particle particle = particleIterator.next();
            if (!particle.shouldRender(camera, tickDelta)) {
                particleIterator.remove();
                continue;
            }

            particle.render(matrices, vertexConsumers, lightmapTextureManager, camera, tickDelta);
        }
    }
}
