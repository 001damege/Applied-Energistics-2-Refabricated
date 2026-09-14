/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2021, TeamAppliedEnergistics, All rights reserved.
 *
 * Applied Energistics 2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Applied Energistics 2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Applied Energistics 2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */

package appeng.core;

import appeng.api.parts.CableRenderMode;
import appeng.api.stacks.AEKeyType;
import appeng.api.stacks.AEKeyTypesInternal;
import appeng.core.definitions.AEItems;
import appeng.core.network.ClientboundPacket;
import appeng.hooks.ticking.TickHandler;
import appeng.hotkeys.HotkeyActions;
import appeng.init.InitCauldronInteraction;
import appeng.init.InitDispenserBehavior;
import appeng.init.internal.*;
import appeng.server.AECommand;
import appeng.server.services.ChunkLoadingService;
import appeng.sounds.AppEngSounds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;

/**
 * Mod functionality that is common to both dedicated server and client.
 * <p>
 * Note that a client will still have zero or more embedded servers (although only one at a time).
 */
public abstract class AppEngBase implements ModInitializer, AppEng {

    private static final Logger LOG = LoggerFactory.getLogger(AppEngBase.class);

    /**
     * While we process a player-specific part placement/cable interaction packet, we need to use that player's
     * transparent-facade mode to understand whether the player can see through facades or not.
     * <p>
     * We need to use this method since the collision shape methods do not know about the player that the shape is being
     * requested for, so they will call {@link #getCableRenderMode()} below, which then will use this field to figure
     * out which player it's for.
     */
    private final ThreadLocal<Player> partInteractionPlayer = new ThreadLocal<>();

    static AppEngBase INSTANCE;

    @Override
    public void onInitialize() {
        if (INSTANCE != null) {
            throw new IllegalStateException();
        }
        INSTANCE = this;

        AEConfig.init();

        InitGridLinkables.init();
        InitBlockEntityMoveStrategies.init();

        TickHandler.instance().init();

        HotkeyActions.init();
    }

    /**
     * Runs after all mods have had time to run their registrations into registries.
     */
    public void postRegistrationInitialization() {
        // Now that item instances are available, we can initialize registries that need item instances
        InitGridLinkables.init();
        InitStorageCells.init();

        InitP2PAttunements.init();

        InitCauldronInteraction.init();
        InitDispenserBehavior.init();

        InitUpgrades.init();
    }

    public void registerKeyTypes(Registry<AEKeyType> registry) {
        Registry.register(registry, AEKeyType.items().getId(), AEKeyType.items());
        Registry.register(registry, AEKeyType.fluids().getId(), AEKeyType.fluids());
    }

    public void registerCommands() {
        new AECommand().register(null);
    }

    public void registerSounds(Registry<SoundEvent> registry) {
        AppEngSounds.register(registry);
    }

    public void registerRegistries() {
        var registry = FabricRegistryBuilder.createSimple(AEKeyType.REGISTRY_KEY)
                .attribute(RegistryAttribute.SYNCED)
                .buildAndRegister();
        AEKeyTypesInternal.setRegistry(registry);
    }

    private void onServerAboutToStart() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> ChunkLoadingService.getInstance().onServerAboutToStart());
    }

    private void serverStopping() {
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> ChunkLoadingService.getInstance().onServerStopping());
    }

    private void serverStopped() {
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> TickHandler.instance().shutdown());
    }

    public void registerCreativeTabs(Registry<CreativeModeTab> registry) {
        MainCreativeTab.init(registry);
        FacadeCreativeTab.init(registry);
    }

    @Override
    public Collection<ServerPlayer> getPlayers() {
        var server = getCurrentServer();
        return server != null ? server.getPlayerList().getPlayers() : Collections.emptyList();
    }

    @Override
    public void sendToAllNearExcept(Player p, double x, double y, double z, double dist, Level level, ClientboundPacket packet) {
    }

    @Override
    public void setPartInteractionPlayer(Player player) {
        this.partInteractionPlayer.set(player);
    }

    @Override
    public CableRenderMode getCableRenderMode() {
        return this.getCableRenderModeForPlayer(partInteractionPlayer.get());
    }

    @Override
    public @Nullable MinecraftServer getCurrentServer() {
        return null;
    }

    @Override
    public void sendSystemMessage(Player player, Component text) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.sendSystemMessage(text);
        }
    }

    protected final CableRenderMode getCableRenderModeForPlayer(@Nullable Player player) {
        return player != null && (AEItems.NETWORK_TOOL.is(player.getItemInHand(InteractionHand.MAIN_HAND)) || AEItems.NETWORK_TOOL.is(player.getItemInHand(InteractionHand.OFF_HAND))) ? CableRenderMode.CABLE_VIEW : CableRenderMode.STANDARD;
    }

    private void registerTests() {
    }
}
