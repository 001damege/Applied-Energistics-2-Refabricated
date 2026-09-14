package appeng.client.integrations.jei.widgets;

import net.minecraft.network.chat.Component;

import java.util.List;

public abstract class AbstractWidget implements Widget {
    public List<Component> tooltipLines = List.of();

    protected final void setTooltipLines(List<Component> tooltipLines) {
        this.tooltipLines = tooltipLines;
    }

    @Override
    public List<Component> getTooltipLines() {
        return tooltipLines;
    }
}
