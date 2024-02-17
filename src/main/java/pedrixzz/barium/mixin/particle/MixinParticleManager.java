package pedrixzz.barium.mixin.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.List;

@Mixin(ParticleManager.class)
public abstract class MixinParticleManager {

    @Inject
    private static final MixinLogger logger = MixinLogger.getMixinLogger(MixinParticleManager.class);

    @Overwrite
    public void tick() {
        // Crie uma lista para armazenar as partículas que serão removidas
        List<Particle> particlesToRemove = new ArrayList<>();

        // Itere por todas as partículas ativas
        for (Particle particle : this.particles) {
            // Verifique se a partícula ainda está viva
            if (particle.isAlive()) {
                // Atualize a partícula
                particle.tick();
            } else {
                // Adicione a partícula à lista de remoção
                particlesToRemove.add(particle);
            }
        }

        // Remova as partículas da lista de remoção
        for (Particle particle : particlesToRemove) {
            this.removeParticle(particle);
        }

        // Otimização: Limpe a lista de remoção
        particlesToRemove.clear();

        // Reduza a frequência de atualização de partículas com base na RAM
        if (Runtime.getRuntime().totalMemory() <= 2_097_152_000L) {
            // Atualize as partículas a cada 2 ticks
            if (this.world.getGameTime() % 2 == 0) {
                super.tick();
            }
        } else {
            // Atualize as partículas normalmente
            super.tick();
        }
    }
}
