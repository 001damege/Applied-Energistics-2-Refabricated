package appeng.client.integrations.jei;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.GenericStack;
import appeng.client.api.integrations.jei.IngredientConverter;
import com.google.common.primitives.Ints;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.neoforge.NeoForgeTypes;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class FluidIngredientConverter implements IngredientConverter<FluidStack> {
    @Override
    public IIngredientType<FluidStack> getIngredientType() {
        return NeoForgeTypes.FLUID_STACK;
    }

    @Nullable
    @Override
    public FluidStack getIngredientFromStack(GenericStack stack) {
        if (stack.what() instanceof AEFluidKey fluidKey) {
            return fluidKey.toStack(Math.max(1, Ints.saturatedCast(stack.amount())));
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public GenericStack getStackFromIngredient(FluidStack ingredient) {
        return GenericStack.fromFluidStack(ingredient);
    }
}
