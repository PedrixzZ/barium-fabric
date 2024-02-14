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

import java.util.Map;
import java.util.Queue;

@Mixin(ParticleManager.class)
public class MixinParticleManager {
	@Shadow @Final private Map<ParticleTextureSheet, Queue<Particle>> particles;

	private static final int VISIBILITY_CACHE_SIZE = 1024;
	private final boolean[] visibilityCache = new boolean[VISIBILITY_CACHE_SIZE];

	@Inject(method = "renderParticles", at = @At("HEAD"), cancellable = true)
	private void onRender(MatrixStack matrices, Immediate vertexConsumers, LightmapTextureManager lightmapTextureManager, Camera camera, float tickDelta, CallbackInfo ci) {
		if (particles.isEmpty()) {
			ci.cancel();
			return;
		}

		int visibleParticles = 0;
		for (Map.Entry<ParticleTextureSheet, Queue<Particle>> entry : particles.entrySet()) {
			ParticleTextureSheet textureSheet = entry.getKey();
			Queue<Particle> particleQueue = entry.getValue();

			int renderedParticles = 0;
			while (!particleQueue.isEmpty()) {
				Particle particle = particleQueue.poll();
				if (!particle.shouldRender(camera)) {
					continue;
				}

				int cacheIndex = particle.getRenderIndex() % VISIBILITY_CACHE_SIZE;
				if (!visibilityCache[cacheIndex]) {
					visibilityCache[cacheIndex] = particle.isVisible(camera);
				}

				if (visibilityCache[cacheIndex]) {
					particle.render(matrices, vertexConsumers, lightmapTextureManager, camera, tickDelta);
					renderedParticles++;
				}
			}

			if (renderedParticles == 0) {
				particles.remove(textureSheet);
			} else {
				visibleParticles += renderedParticles;
			}
		}

		if (visibleParticles == 0) {
			ci.cancel();
		}
	}
}
