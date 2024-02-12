package pedrixzz.barium.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class RenderHandler extends TileEntityRenderer<MyTileEntity> {

    private static final Map<ResourceLocation, Texture> textureCache = Maps.newHashMap();

    @Override
    public void render(MyTileEntity tileEntity, float f, PoseStack matrixStack, int light, int overlay) {
        // Reduz chamadas de API: verifica se a renderização é necessária
        if (!tileEntity.shouldRender()) {
            return;
        }

        // Otimização de malha: usa uma malha simples para o bloco
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();

        // Cache de textura: obtém a textura da memória ou carrega-a
        ResourceLocation textureLocation = tileEntity.getTextureLocation();
        Texture texture = getTexture(textureLocation);

        // Renderização em lote: agrupa blocos com a mesma textura
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormats.POSITION_TEX);

        // Renderiza o bloco
        // ...

        tessellator.end();
    }

    private static Texture getTexture(ResourceLocation textureLocation) {
        Texture texture = textureCache.get(textureLocation);
        if (texture == null) {
            texture = new Texture(textureLocation);
            textureCache.put(textureLocation, texture);
        }
        return texture;
    }
}
