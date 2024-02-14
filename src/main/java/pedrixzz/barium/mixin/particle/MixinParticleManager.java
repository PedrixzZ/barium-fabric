package pedrixzz.barium.mixin.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
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
    private final List<Particle> renderList = new ArrayList<>();

    @Inject(method = "renderParticles", at = @At("HEAD"), cancellable = true)
    private void onRender(MatrixStack matrices, Immediate vertexConsumers, LightmapTextureManager lightmapTextureManager, Camera camera, float tickDelta, CallbackInfo ci) {
        if (particles.isEmpty()) {
            ci.cancel();
            return;
        }

        // Filtra partículas visíveis e as coloca na lista de renderização.
        for (Queue<Particle> queue : particles.values()) {
            for (Particle particle : queue) {
                if (particle.shouldRender(camera, tickDelta)) {
                    renderList.add(particle);
                }
            }
        }

        // Ordena as partículas por textura para otimizar a troca de texturas.
        renderList.sort((p1, p2) -> p1.getSprite().getAtlasIndex() - p2.getSprite().getAtlasIndex());

        // Renderiza as partículas em batches por textura.
        int currentAtlasIndex = -1;
        for (Particle particle : renderList) {
            int atlasIndex = particle.getSprite().getAtlasIndex();
            if (atlasIndex != currentAtlasIndex) {
                if (currentAtlasIndex != -1) {
                    vertexConsumers.end();
                }
                vertexConsumers.begin(particle.getRenderType(tickDelta), particle.getSprite().getAtlas());
                currentAtlasIndex = atlasIndex;
            }
            particle.render(matrices, vertexConsumers, lightmapTextureManager, camera, tickDelta);
        }
        vertexConsumers.end();
        renderList.clear();
    }
}
