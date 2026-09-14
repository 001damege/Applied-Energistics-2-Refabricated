package appeng.api.client;

import appeng.api.stacks.AEKey;
import appeng.util.Platform;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Client-side rendering of AE stacks. Must be registered in {@link AEKeyRendering} for each storage channel!
 */
@Environment(EnvType.CLIENT)
public interface AEKeyRenderHandler<T extends AEKey> {
    /**
     * Draw the stack, for example the item or the fluid sprite, but not the amount.
     */
    void drawInGui(Minecraft minecraft, GuiGraphics guiGraphics, int x, int y, T stack);

    /**
     * Draw the representation of a key in-world on the face of a block. Used for displaying it on screens and monitors.
     */
    void drawOnBlockFace(PoseStack poseStack, MultiBufferSource buffers, T what, float scale, int combinedLight, Level level);

    /**
     * Name of the stack, ignoring the amount.
     */
    Component getDisplayName(T stack);

    /**
     * Return the full tooltip, with the name of the stack and any additional lines.
     */
    default List<Component> getTooltip(T stack) {
        // Append the name of the mod by default as mods such as REI would also add that
        return List.of(getDisplayName(stack), Component.literal(Platform.formatModName(stack.getModId())));
    }
}
