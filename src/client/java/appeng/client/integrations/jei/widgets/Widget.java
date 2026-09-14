package appeng.client.integrations.jei.widgets;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

import java.util.List;

public interface Widget {
    void draw(GuiGraphicsExtractor guiGraphics);

    default boolean hitTest(double x, double y) {
        return false;
    }

    default List<Component> getTooltipLines() {
        return List.of();
    }
}
