package dhyces.dinnerplate.blockentity.api;

import dhyces.dinnerplate.dinnerunit.FlutemStack;

public interface IWorkstation {

	// I want craft to always take in an inventory, which should also implement a container so that it can use clearContents,
	// and I want it to match recipes, then if one matches, clear the crafted contents and return the recipe item
	public void craft();
	
	public boolean hasRecipe();
	
}
