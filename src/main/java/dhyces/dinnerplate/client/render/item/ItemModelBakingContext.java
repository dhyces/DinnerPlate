package dhyces.dinnerplate.client.render.item;

import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import org.jetbrains.annotations.Nullable;

public class ItemModelBakingContext implements IGeometryBakingContext {

    public static final ItemModelBakingContext INSTANCE = new ItemModelBakingContext(false);
    private final boolean isSideLit;

    public ItemModelBakingContext(boolean isSideLit) {
        this.isSideLit = isSideLit;
    }

    @Override
    public String getModelName() {
        return null;
    }

    @Override
    public boolean hasMaterial(String name) {
        return false;
    }

    @Override
    public Material getMaterial(String name) {
        return null;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean useBlockLight() {
        return this.isSideLit;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public ItemTransforms getTransforms() {
        return null;
    }

    @Override
    public Transformation getRootTransform() {
        return null;
    }

    @Override
    public @Nullable ResourceLocation getRenderTypeHint() {
        return null;
    }

    @Override
    public boolean isComponentVisible(String component, boolean fallback) {
        return false;
    }
}
