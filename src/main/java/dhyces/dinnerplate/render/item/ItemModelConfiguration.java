package dhyces.dinnerplate.render.item;

import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraftforge.client.model.IModelConfiguration;

public class ItemModelConfiguration implements IModelConfiguration {

	private boolean isSideLit;
	public static final ItemModelConfiguration INSTANCE = new ItemModelConfiguration(false);
	
	public ItemModelConfiguration(boolean isSideLit) {
		this.isSideLit = isSideLit;
	}
	
	@Override
	public UnbakedModel getOwnerModel() {
		return null;
	}

	@Override
	public String getModelName() {
		return null;
	}

	@Override
	public boolean isTexturePresent(String name) {
		return false;
	}

	@Override
	public Material resolveTexture(String name) {
		return null;
	}

	@Override
	public boolean isShadedInGui() {
		return false;
	}

	@Override
	public boolean isSideLit() {
		return this.isSideLit;
	}

	@Override
	public boolean useSmoothLighting() {
		return true;
	}

	@Override
	public ItemTransforms getCameraTransforms() {
		return null;
	}

	@Override
	public ModelState getCombinedTransform() {
		return null;
	}
}
