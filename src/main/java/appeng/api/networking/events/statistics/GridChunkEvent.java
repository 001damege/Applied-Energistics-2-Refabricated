/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 TeamAppliedEnergistics
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package appeng.api.networking.events.statistics;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;

/**
 * An event send from the statistics grid once something about a chunk changes.
 * 
 * Listeners will not receive updates about pre-existing chunks when joining a network.
 */
public abstract class GridChunkEvent extends GridStatisticsEvent {
    public static final Event<Added> ADDED = EventFactory.createArrayBacked(Added.class, callbacks -> (level, chunkPos) -> {
        for (var callback : callbacks) {
            callback.added(level, chunkPos);
        }
    });

    public static final Event<Removed> REMOVED = EventFactory.createArrayBacked(Removed.class, callbacks -> (level, chunkPos) -> {
        for (var callback : callbacks) {
            callback.removed(level, chunkPos);
        }
    });

    @FunctionalInterface
    public interface Added {
        void added(ServerLevel level, ChunkPos chunkPos);
    }

    public interface Removed {
        void removed(ServerLevel level, ChunkPos chunkPos);
    }
}
