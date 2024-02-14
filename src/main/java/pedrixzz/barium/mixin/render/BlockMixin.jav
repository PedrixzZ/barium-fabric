package pedrixzz.barium.mixin.render;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.client.render.RenderLayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {

    @Inject
    @At(value = "HEAD", target = "Lnet/minecraft/block/Block;updateTick(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V")
    private void onUpdateTick(World world, BlockPos pos, BlockState state) {
        // Se o bloco for otimizado, cancele a atualização
        if (MixinBlock.isOptimized(state.getBlock())) {
            return;
        }
    }

    @Inject
    @At(value = "HEAD", target = "Lnet/minecraft/block/Block;onBlockAdded(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;Z)V")
    private void onBlockAdded(World world, BlockPos pos, BlockState state, BlockState oldState, boolean isMoving) {
        // Se o bloco for otimizado, cancele a atualização de luz
        if (MixinBlock.isOptimized(state.getBlock())) {
            world.getLightUpdater().updateLight(LightType.BLOCK, pos);
        }
    }

    @Inject
    @At(value = "HEAD", target = "Lnet/minecraft/block/Block;onReplaced(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;Z)V")
    private void onReplaced(World world, BlockPos pos, BlockState state, BlockState newState, boolean isMoving) {
        // Se o bloco for otimizado, cancele a atualização de luz
        if (MixinBlock.isOptimized(state.getBlock())) {
            world.getLightUpdater().updateLight(LightType.BLOCK, pos);
        }
    }

    @Inject
    @At(value = "HEAD", target = "Lnet/minecraft/block/Block;onEntityCollision(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/Entity;)V")
    private void onEntityCollision(World world, BlockPos pos, BlockState state, Entity entity) {
        // Se o bloco for otimizado, cancele a colisão
        if (MixinBlock.isOptimized(state.getBlock())) {
            return;
        }
    }

    @Inject
    @At(value = "HEAD", target = "Lnet/minecraft/block/Block;onProjectileCollision(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/projectile/ProjectileEntity;)V")
    private void onProjectileCollision(World world, BlockPos pos, BlockState state, ProjectileEntity projectile) {
        // Se o bloco for otimizado, cancele a colisão
        if (MixinBlock.isOptimized(state.getBlock())) {
            return;
        }
    }

    @Inject
    @At(value = "HEAD", target = "Lnet/minecraft/block/Block;getAmbientOcclusionLightLevel(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)F")
    private float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        // Se o bloco for otimizado, use um valor de oclusão de luz constante
        if (MixinBlock.isOptimized(state.getBlock())) {
            return 1.0f;
        }
        return state.getAmbientOcclusionLightLevel(world, pos);
    }

    @Inject
    @At(value = "HEAD", target = "Lnet/minecraft/block/Block;getRenderLayer(Lnet/minecraft/block/BlockState;)Lnet/minecraft/client/render/RenderLayer;")
    private RenderLayer getRenderLayer(BlockState state) {
        // Se o bloco for otimizado, use uma camada de renderização mais eficiente
        if (MixinBlock.isOptimized(state.getBlock())) {
            return RenderLayer.SOLID;
        }
        return state.getRenderLayer();
    }

    private static boolean isOptimized(Block block) {
        // Verifique se o bloco está na lista de blocos otimizados
        return Arrays.asList(OPTIMIZED_BLOCKS).contains(block);
    }
}
