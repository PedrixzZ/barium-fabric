package pedrixzz.barium.mixin.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public abstract class MixinParticleManager {

    @Inject
    @At(value = "HEAD", target = "Lnet/minecraft/client/particle/ParticleManager;tick()V")
    private void tick(ClientWorld world, Camera camera, float tickDelta) {
        // Desative a renderização de partículas para entidades que não estejam na tela
        for (Particle particle : world.getEntitiesByClass(Particle.class, null)) {
            Box particleBoundingBox = particle.getBoundingBox();
            Vec3d particlePos = particle.getPos();
            if (!camera.isBoundingBoxVisible(particleBoundingBox) && !camera.isBoundingBoxVisible(particlePos)) {
                particle.setExpired(true);
            }
        }
    }

    @Inject
    @At(value = "HEAD", target = "Lnet/minecraft/client/particle/ParticleManager;addParticle(Lnet/minecraft/particle/Particle;)V")
    private void addParticle(Particle particle, CallbackInfo ci) {
        // Cancele a adição de partículas se o limite for excedido
        if (world.getParticles().size() >= 10000) {
            ci.cancel();
            return;
        }
        
        // Otimize a lógica de renderização de acordo com o tipo de partícula
        if (particle instanceof FlameParticle) {
            ((FlameParticle) particle).setAlpha(0.5f);
        } else if (particle instanceof SmokeParticle) {
            ((SmokeParticle) particle).setGravity(0.05f);
        }
    }
}
