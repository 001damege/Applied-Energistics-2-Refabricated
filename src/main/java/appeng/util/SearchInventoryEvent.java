package appeng.util;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Event fired when AE2 is looking for ItemStacks in a player inventory. By default, AE2 only looks at the 36 usual
 * slots of the player inventory, use this event to make AE2 consider more stacks. AE2 will check after the event if
 * they contain the item it is searching.
 */
public class SearchInventoryEvent {
    public static final Event<SearchInventory> EVENT = EventFactory.createArrayBacked(SearchInventory.class, callbacks -> (player, stacks) -> {
        for (var callback : callbacks) {
            callback.search(player, stacks);
        }
    });

    public interface SearchInventory {
        void search(Player player, List<ItemStack> stacks);
    }

    public static List<ItemStack> getItems(Player player) {
        List<ItemStack> items = new ArrayList<>();
        EVENT.invoker().search(player, items);
        return items;
    }
}
