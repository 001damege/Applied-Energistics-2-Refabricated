package appeng.client.api;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface AEKeyRenderer<T, S> {
    /**
     * Draw the stack, for example the item or the fluid sprite, but not the amount.
     */
    void drawInGui(Minecraft minecraft, GuiGraphicsExtractor guiGraphics, int x, int y, T stack);

    Class<S> stateClass();

    /**
     * @return Create state used for storing this handlers render state.
     */
    S createState();

    /**
     * Draw the representation of a key in-world on the face of a block. Used for displaying it on screens and monitors.
     */
    void extract(S state, T what, @Nullable Level level, int seed);

    /**
     * Draw the representation of a key in-world on the face of a block. Used for displaying it on screens and monitors.
     */
    void submit(PoseStack poseStack,
            S state,
            SubmitNodeCollector nodes,
            int lightCoords);

    /**
     * Return the full tooltip, with the name of the stack and any additional lines.
     */
    List<Component> getTooltip(T stack);
}
