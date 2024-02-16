package pedrixzz.barium.mixin.particle;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {

    @Shadow
    private static final int MAX_PARTICLES = 4096;

    @Overwrite
    public void update() {
        // Reduzir o número máximo de partículas
        int maxParticles = MAX_PARTICLES / 4;

        // Iterar sobre as partículas ativas
        for (int i = 0; i < maxParticles; i++) {
            Particle particle = particles[i];

            // Atualizar a partícula
            particle.tick();

            // Se a partícula expirou, removê-la
            if (particle.shouldDie()) {
                particles[i] = particles[maxParticles - 1];
                maxParticles--;
            }
        }
    }

}
