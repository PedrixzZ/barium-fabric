package pedrixzz.barium.mixin.render;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    @Inject
    private static final Field<Boolean> field_239224_a = 
        ReflectionHelper.findField(GameRenderer.class, "field_239224_a");

    @Inject
    private static final Method method_239232_a = 
        ReflectionHelper.findMethod(GameRenderer.class, "method_239232_a", 
            MatrixStack.class, float.class, long.class, boolean.class);

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;setupCamera(Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/entity/Entity;F)V"))
    private void redirectSetupCamera(GameRenderer instance, Camera camera, ClientWorld world, Entity entity, float tickDelta) {
        // Desabilita a frustum culling para aumentar o desempenho
        field_239224_a.set(instance, false);

        // Chama o método original
        try {
            method_239232_a.invoke(instance, camera, tickDelta, world.getGameTime(), true);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }

        // Reativa a frustum culling
        field_239224_a.set(instance, true);
    }

    // ... outras otimizações aqui ...
}
