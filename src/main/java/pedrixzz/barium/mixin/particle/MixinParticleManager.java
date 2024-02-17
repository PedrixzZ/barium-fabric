package pedrixzz.barium.mixin.particle;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleList;
import net.minecraft.client.particle.Particle;

@Mixin(ParticleManager.class)
public abstract class MixinParticleManager {

    @Inject
    private static final int MAX_PARTICLES = 1000; // Ajuste este valor de acordo com suas necessidades.

    @Redirect(
        method = "addParticle",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/particle/ParticleList;addParticle(Lnet/minecraft/client/particle/Particle;)V"
        )
    )
    private static void onAddParticle(ParticleManager manager, Particle particle) {
        if (manager.getParticleCount() < MAX_PARTICLES) {
            ParticleList.addParticle(particle);
        }
    }
}
