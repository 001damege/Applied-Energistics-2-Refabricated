package appeng.decorative.solid;

import appeng.block.misc.MysteriousCubeBlock;
import appeng.core.localization.GuiText;
import appeng.core.localization.Tooltips;
import appeng.decorative.AEDecorativeBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.function.Consumer;

public class NotSoMysteriousCubeBlock extends AEDecorativeBlock {
    public NotSoMysteriousCubeBlock(Properties p) {
        super(MysteriousCubeBlock.properties(p));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, Consumer<Component> tooltip,
            TooltipFlag flag) {
        tooltip.accept(Tooltips.of(GuiText.NotSoMysteriousQuote, Tooltips.QUOTE_TEXT));
    }
}
