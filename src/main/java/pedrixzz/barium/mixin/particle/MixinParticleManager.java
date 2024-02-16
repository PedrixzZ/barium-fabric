package pedrixzz.barium.mixin.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public abstract class MixinParticleManager {

    @Inject
    private static final int MAX_PARTICLES = 1000; // Número máximo de partículas

    @Inject
    private static final int MAX_PARTICLES_PER_TICK = 100; // Número máximo de partículas por tick

    @Overwrite
    public void addParticle(Particle particle) {
        if (particle == null) {
            return;
        }

        // Limitar o número de partículas
        if (this.particleCount >= MAX_PARTICLES) {
            return;
        }

        // Adicionar a partícula à lista
        this.particles.add(particle);
        this.particleCount++;

        // Agendar a atualização da partícula
        this.scheduler.schedule(particle::onUpdate, 1);
    }

    @Overwrite
    public void tick() {
        // Limitar o número de partículas por tick
        int particlesProcessed = 0;
        for (int i = 0; i < this.particles.size(); i++) {
            Particle particle = this.particles.get(i);
            if (particle.isAlive()) {
                particle.onUpdate();
                particlesProcessed++;
            }

            if (particlesProcessed >= MAX_PARTICLES_PER_TICK) {
                break;
            }
        }

        // Remover as partículas mortas
        for (int i = this.particles.size() - 1; i >= 0; i--) {
            if (!this.particles.get(i).isAlive()) {
                this.particles.remove(i);
                this.particleCount--;
            }
        }
    }
}
