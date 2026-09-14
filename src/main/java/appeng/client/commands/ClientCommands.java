package appeng.client.commands;

import appeng.core.AEConfig;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.List;

public final class ClientCommands {

    public static final List<CommandBuilder> DEBUG_COMMANDS = List.of(
            ClientCommands::highlightGuiAreas);

    private ClientCommands() {
    }

    @FunctionalInterface
    public interface CommandBuilder {
        void build(LiteralArgumentBuilder<CommandSourceStack> builder);
    }

    private static void highlightGuiAreas(LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder.then(Commands.literal("highlight_gui_areas").executes(context -> {
            var src = context.getSource();
            var toggle = !AEConfig.instance().isShowDebugGuiOverlays();
            AEConfig.instance().setShowDebugGuiOverlays(toggle);
            AEConfig.instance().save();
            src.sendSystemMessage(Component.literal("GUI Overlays: " + toggle));
            return 0;
        }));
    }
}
