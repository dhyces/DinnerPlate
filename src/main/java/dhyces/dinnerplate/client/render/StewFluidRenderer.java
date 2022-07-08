package dhyces.dinnerplate.client.render;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Vector3f;
import dhyces.dinnerplate.DinnerPlate;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.NotNull;

public class StewFluidRenderer implements IClientFluidTypeExtensions {

    final int color;
    private static final ResourceLocation STILL_TEXTURE = DinnerPlate.modLoc("block/soup_still");
    private static final ResourceLocation FLOWING_TEXTURE = DinnerPlate.modLoc("block/soup_flow");

    public StewFluidRenderer(int color) {
        this.color = color;
    }

    @Override
    public int getTintColor() {
        return color;
    }

    @Override
    public ResourceLocation getStillTexture() {
        return STILL_TEXTURE;
    }

    @Override
    public ResourceLocation getFlowingTexture() {
        return FLOWING_TEXTURE;
    }

    @Override
    public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
        float r = (color >> 16) & 255; r = Mth.clamp(r - 50, 0, 255);
        float g = (color >> 8) & 255; g = Mth.clamp(g - 50, 0, 255);
        float b = color & 255; b = Mth.clamp(b - 50, 0, 255);
        fluidFogColor.set(r / 255F, g / 255F, b / 255F);
        return fluidFogColor;
    }

    @Override
    public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
        RenderSystem.setShaderFogStart(0.1F);
        RenderSystem.setShaderFogEnd(2.0F);
        RenderSystem.setShaderFogShape(shape);
    }
}
