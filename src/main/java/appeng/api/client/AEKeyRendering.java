package appeng.api.client;

import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEKeyType;
import com.google.common.base.Preconditions;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Environment(EnvType.CLIENT)
public class AEKeyRendering {
    private static volatile Map<AEKeyType, AEKeyRenderHandler<?>> renderers = new IdentityHashMap<>();

    public static synchronized <T extends AEKey> void register(AEKeyType channel, Class<T> keyClass, AEKeyRenderHandler<T> handler) {
        Objects.requireNonNull(channel, "channel");
        Objects.requireNonNull(handler, "handler");
        Objects.requireNonNull(keyClass, "keyClass");
        Preconditions.checkArgument(channel.getKeyClass() == keyClass, "%s != %s", channel.getKeyClass(), keyClass);

        var renderersCopy = new IdentityHashMap<>(renderers);
        if (renderersCopy.put(channel, handler) != null) {
            throw new IllegalArgumentException("Duplicate registration of render handler for channel " + channel);
        }
        renderers = renderersCopy;
    }

    @Nullable
    public static AEKeyRenderHandler<?> get(AEKeyType channel) {
        return renderers.get(channel);
    }

    public static AEKeyRenderHandler<?> getOrThrow(AEKeyType channel) {
        var renderHandler = get(channel);

        if (renderHandler == null) {
            throw new IllegalArgumentException("Missing render handler for channel " + channel);
        }
        return renderHandler;
    }

    @SuppressWarnings("rawtypes")
    private static AEKeyRenderHandler getUnchecked(AEKey stack) {
        return getOrThrow(stack.getType());
    }

    @SuppressWarnings("unchecked")
    public static void drawInGui(Minecraft minecraft, GuiGraphics guiGraphics, int x, int y, AEKey what) {
        getUnchecked(what).drawInGui(minecraft, guiGraphics, x, y, what);
    }

    @SuppressWarnings("unchecked")
    public static void drawOnBlockFace(PoseStack poseStack, MultiBufferSource buffers, AEKey what, float scale, int combinedLightIn, Level level) {
        getUnchecked(what).drawOnBlockFace(poseStack, buffers, what, scale, combinedLightIn, level);
    }

    @SuppressWarnings("unchecked")
    public static Component getDisplayName(AEKey stack) {
        return getUnchecked(stack).getDisplayName(stack);
    }

    @SuppressWarnings("unchecked")
    public static List<Component> getTooltip(AEKey stack) {
        // The array list is used to ensure mutability of the returned tooltip.
        return new ArrayList<Component>(getUnchecked(stack).getTooltip(stack));
    }
}
