package appeng.client.integrations.jei.transfer;

import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.world.item.crafting.Recipe;

public abstract class AbstractTransferHandler {
    protected final boolean fitsIn3x3Grid(Recipe<?> recipe, IRecipeSlotsView display) {
        if (recipe != null) {
            return !recipe.placementInfo().isImpossibleToPlace();
        } else {
            return true;
        }
    }
}
