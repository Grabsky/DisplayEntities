/*
 * DisplayEntities (https://github.com/Grabsky/DisplayEntities)
 *
 * MIT License
 *
 * Copyright (c) 2025 Grabsky (michal.czopek.foss@proton.me)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package cloud.grabsky.displayentities.listener;

import cloud.grabsky.displayentities.DisplayEntities;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public final class PacketListener implements com.github.retrooper.packetevents.event.PacketListener {

    // Holds reference  to the plugin instance.
    private final @NotNull DisplayEntities plugin;

    // Stores entities that have an active task.
    private final Set<Integer> hasActiveTask = ConcurrentHashMap.newKeySet();

    // Stores information of whether the server is Folia or not.
    private static boolean IS_FOLIA;

    static {
        try {
            Class.forName("io.papermc.paper.threadedregions.ThreadedRegionizer");
            IS_FOLIA = true;
        } catch (final ClassNotFoundException e) {
            IS_FOLIA = false;
        }
    }

    @Override @SuppressWarnings("unchecked")
    public void onPacketSend(final @NotNull PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.SPAWN_ENTITY) {
            // Getting the the entity from it's int id. Can be null.
            final @Nullable Entity entity = SpigotConversionUtil.getEntityById(((Player)event.getPlayer()).getWorld(), new WrapperPlayServerSpawnEntity(event).getEntityId());
            // Checking if entity with this id exists on the server and is a text display entity.
            if (entity instanceof TextDisplay && entity.getPersistentDataContainer().has(DisplayEntities.Keys.NAME) == true) {
                final int entityId = entity.getEntityId();
                // Skipping further logic if the task is already running.
                if (hasActiveTask.contains(entityId) == true)
                    return;
                // Getting the text contents stored inside PDC.
                final String text = entity.getPersistentDataContainer().getOrDefault(DisplayEntities.Keys.TEXT_CONTENTS, PersistentDataType.STRING, "");
                // Checking if text contains PlaceholderAPI placeholders and is tracked by any player.
                if (PlaceholderAPI.containsPlaceholders(text) == true) {
                    // Scheduling logic to be executed on Folia's EntityScheduler if needed, or on the current thread, which is NOT the main thread.
                    runWrapped(entity, () -> {
                        // Continuing only if the entity is being tracked by at least one player.
                        if (entity.getTrackedBy().isEmpty() == false) {
                            hasActiveTask.add(entityId);
                            // Getting refresh interval from the entity. Defaults to the configured value.
                            final int refreshInterval = entity.getPersistentDataContainer().getOrDefault(DisplayEntities.Keys.REFRESH_INTERVAL, PersistentDataType.INTEGER, plugin.configuration().refreshInterval());
                            // Logging debug information to the console.
                            plugin.debug("[E:" + entityId + "] Starting the placeholders refresh task... [RI:" + refreshInterval + "]");
                            // Scheduling a repeating task to refresh placeholders for all viewers, every N ticks.
                            entity.getScheduler().runAtFixedRate(plugin, (task) -> {
                                // Cancelling the task if nobody is tracking the entity.
                                if (entity.getTrackedBy().isEmpty() == true) {
                                    // Logging debug information to the console.
                                    plugin.debug("[E:" + entityId + "] Cancelling the placeholders refresh task... [C:SELF]");
                                    // Cancelling the task.
                                    task.cancel();
                                    // Removing task from the list of active tasks.
                                    hasActiveTask.remove(entityId);
                                    return;
                                }
                                // Scheduling further logic to be executed off the main thread, or on Folia's EntityScheduler if needed.
                                runWrappedAsync(entity, () -> {
                                    entity.getTrackedBy().forEach(viewer -> {
                                        final var packet = new WrapperPlayServerEntityMetadata(entityId, List.of(
                                                // Setting text to an empty component. This is intercepted in the entity_metadata listener where all placeholders and colors are applied.
                                                new EntityData<>(23, EntityDataTypes.ADV_COMPONENT, Component.empty())
                                        ));
                                        // Sending packet to the viewer.
                                        PacketEvents.getAPI().getPlayerManager().sendPacket(viewer, packet);
                                        // Logging debug information to the console.
                                        plugin.debug("[E:" + entityId + "] Packet sent to user " + viewer.getName() + "... [P:" + packet.getNativePacketId() + "] [H:" + packet.hashCode() + "]");
                                    });
                                });
                            }, () -> {
                                // Logging debug information to the console.
                                plugin.debug("[E:" + entityId + "] Cancelling the placeholders refresh task... [C:RETIRED]");
                                // Removing task from the list of active tasks.
                                hasActiveTask.remove(entityId);
                            }, 10, refreshInterval);
                        }
                    });
                }
            }
        }
        else if (event.getPacketType() == PacketType.Play.Server.ENTITY_METADATA) {
            final var packet = new WrapperPlayServerEntityMetadata(event);
            // Getting the the entity from it's int id. Can be null.
            final @Nullable Entity entity = SpigotConversionUtil.getEntityById(((Player) event.getPlayer()).getWorld(), packet.getEntityId());
            // Checking if entity with this id exists on the server and is a text display entity.
            if (entity instanceof TextDisplay) {
                // Getting the text contents stored inside PDC.
                final String text = entity.getPersistentDataContainer().getOrDefault(DisplayEntities.Keys.TEXT_CONTENTS, PersistentDataType.STRING, "");
                // Checking if text contains PlaceholderAPI placeholders.
                if (PlaceholderAPI.containsPlaceholders(text) == true) {
                    // Iterating over list entity metadata in search for text changes.
                    for (final EntityData<?> data : packet.getEntityMetadata()) {
                        if (data.getType() == EntityDataTypes.ADV_COMPONENT)
                            // Overriding with parsed component.
                            ((EntityData<Component>) data).setValue(MiniMessage.miniMessage().deserialize(PlaceholderAPI.setPlaceholders(event.getPlayer(), text)));
                    }
                }
            }
        }
    }

    private void runWrapped(final @NotNull Entity entity, final @NotNull Runnable runnable) {
        // Executing runnable on EntityScheduler when server is using Folia.
        if (IS_FOLIA == true)
            entity.getScheduler().run(plugin, (it) -> runnable.run(), null);
        // Otherwise, runnable is executed on the current thread.
        else runnable.run();
    }

    private void runWrappedAsync(final @NotNull Entity entity, final @NotNull Runnable runnable) {
        // Executing runnable on EntityScheduler when server is using Folia.
        if (IS_FOLIA == true)
            entity.getScheduler().run(plugin, (it) -> runnable.run(), null);
        // Otherwise, runnable is executed on async scheduler.
        else plugin.getServer().getAsyncScheduler().runNow(plugin, (it) -> runnable.run());
    }

}
