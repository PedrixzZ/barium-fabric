package pedrixzz.barium.mixin.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {
    private static final float[] SKY_COLORS = new float[3];
    private static final float[] SUN_COLORS = new float[3];
    private static final float[] FOG_COLORS = new float[3];
    private static boolean skyUpdated = false;
    private static float lastSkyAngle = 0.0F;
    private static long lastSkyColorUpdate = 0L;
    private static long lastSunColorUpdate = 0L;

    @Inject(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V",
            at = @At(value = "HEAD"))
    private static void initSkyColors(CallbackInfo ci) {
        skyUpdated = false;
    }

    @WrapOperation(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getSkyAngle(F)F", ordinal = 0))
    private static float cacheSkyAngle(ClientWorld world, float delta, Operation<Float> original,
                                      @Share("skyAngle") LocalFloatRef skyAngle) {
        float result = original.call(world, delta);
        if (result != lastSkyAngle) {
            skyUpdated = true;
            lastSkyAngle = result;
        }
        skyAngle.set(result);
        return result;
    }

    @Redirect(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getSkyColor(FFF)V"))
    private static void getSkyColor(ClientWorld world, float f, float g, float h) {
        if (skyUpdated || System.currentTimeMillis() - lastSkyColorUpdate > 100L) {
            world.getSkyColor(f, g, h, SKY_COLORS);
            lastSkyColorUpdate = System.currentTimeMillis();
        }
        for (int i = 0; i < 3; i++) {
            FOG_COLORS[i] = FOG_COLORS[i] * 0.95F + SKY_COLORS[i] * 0.05F;
        }
    }

    @Redirect(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getSunColor(FFF)V"))
    private static void getSunColor(ClientWorld world, float f, float g, float h) {
        if (skyUpdated || System.currentTimeMillis() - lastSunColorUpdate > 100L) {
            world.getSunColor(f, g, h, SUN_COLORS);
	    lastSunColorUpdate = System.currentTimeMillis();
        }
        for (int i = 0; i < 3; i++) {
            FOG_COLORS[i] = FOG_COLORS[i] * 0.9F + SUN_COLORS[i] * 0.1F;
        }
    }

    @Redirect(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderFog(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FDDDD[FFFF)V"))
    private static void renderFog(WorldRenderer instance, MatrixStack matrices, Matrix4f matrix, float f, double d, double e, double g, double h, float[] fogColors, float[] blendColors) {
        for (int i = 0; i < 3; i++) {
            fogColors[i] = FOG_COLORS[i];
        }
//        instance.renderFog(matrices, matrix, f, d, e, g, h, fogColors, blendColors);
    }
}
