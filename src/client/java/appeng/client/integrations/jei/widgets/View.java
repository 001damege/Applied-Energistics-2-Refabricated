package appeng.client.integrations.jei.widgets;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

import java.util.List;

public interface View {
    default void createWidgets(WidgetFactory factory, List<Widget> widgets) {
    }

    default void buildSlots(IRecipeLayoutBuilder builder) {
    }

    default void draw(GuiGraphicsExtractor guiGraphics, IRecipeSlotsView slots, double mouseX, double mouseY) {
    }

    default List<Component> getTooltipStrings(double mouseX, double mouseY) {
        return List.of();
    }
}
