package appeng.items.tools.powered;

import appeng.api.stacks.AEItemKey;
import appeng.me.cells.BasicCellHandler;
import appeng.util.BootstrapMinecraft;
import net.minecraft.world.item.Items;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@BootstrapMinecraft
class ColorApplicatorItemTest {
    @Test
    void testCreateFullColorApplicator() {
        var applicator = ColorApplicatorItem.createFullColorApplicator();
        var item = (ColorApplicatorItem) applicator.getItem();

        assertNotEquals(0, item.getAEMaxPower(applicator));
        assertEquals(item.getAEMaxPower(applicator), item.getAECurrentPower(applicator));

        // Get new storage and list content
        var dyeStorage = BasicCellHandler.INSTANCE.getCellInventory(applicator, null);
        var availableStacks = dyeStorage.getAvailableStacks();
        assertEquals(128, availableStacks.get(AEItemKey.of(Items.SNOWBALL)));
    }
}
