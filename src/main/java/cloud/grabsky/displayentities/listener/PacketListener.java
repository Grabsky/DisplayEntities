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
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityHeadLook;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityRotation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mannequin;
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

// TO-DO: - Mannequin direction task may need some adjustments to work on Folia.
//        - Perhaps there's a smart way to avoid sending same packets over and over again.
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public final class PacketListener implements com.github.retrooper.packetevents.event.PacketListener {

    // Holds reference to the plugin instance.
    private final @NotNull DisplayEntities plugin;

    // Stores entities that have an active task.
    private final Set<Integer> hasActiveTextRefreshTask = ConcurrentHashMap.newKeySet();

    // Stores entities that have an active task.
    private final Set<Integer> hasActiveDirectionTask = ConcurrentHashMap.newKeySet();

    // Stores players that are currently being tracked by a mannequin entity.
    private final Set<String> isBeingTracked = ConcurrentHashMap.newKeySet();

    // Stores entities not created by DisplayEntities.
    // This should (hopefully) avoid many calls to a rather expensive (?) SpigotConversionUtil#getEntityById method.
    private final Set<Integer> notHandled = ConcurrentHashMap.newKeySet();

    @Override @SuppressWarnings("unchecked")
    public void onPacketSend(final @NotNull PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.SPAWN_ENTITY && event.getPlayer() instanceof Player eventPlayer) {
            // Getting the id of the entity associated with the event.
            final int entityId = new WrapperPlayServerSpawnEntity(event).getEntityId();
            // Skipping further execution if this entity was already marked as not created by the plugin.
            if (notHandled.contains(entityId) == true)
                return;
            // Getting the entity from it's int id. Can be null.
            final @Nullable Entity entity = SpigotConversionUtil.getEntityById(eventPlayer.getWorld(), entityId);
            // Checking if the entity with this id exists on the server and is a text display entity.
            if (entity instanceof TextDisplay && entity.getPersistentDataContainer().has(DisplayEntities.Keys.TEXT_CONTENTS) == true) {
                // Skipping further logic if the task is already running.
                if (hasActiveTextRefreshTask.contains(entityId) == true)
                    return;
                // Getting the text contents stored inside PDC.
                final String text = entity.getPersistentDataContainer().getOrDefault(DisplayEntities.Keys.TEXT_CONTENTS, PersistentDataType.STRING, "");
                // Checking if text contains PlaceholderAPI placeholders and is tracked by any player.
                if (PlaceholderAPI.containsPlaceholders(text) == true) {
                    // Scheduling logic to be executed on Folia's EntityScheduler if needed, or on the current thread, which is NOT the main thread.
                    runWrapped(entity, () -> {
                        // Continuing only if the entity is being tracked by at least one player.
                        if (entity.getTrackedBy().isEmpty() == true)
                            return;
                        hasActiveTextRefreshTask.add(entityId);
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
                                // Removing the task from the list of active tasks.
                                hasActiveTextRefreshTask.remove(entityId);
                                return;
                            }
                            // Scheduling further logic to be executed off the main thread or on Folia's EntityScheduler if needed.
                            runWrappedAsync(entity, () -> {
                                entity.getTrackedBy().forEach(viewer -> {
                                    final var packet = new WrapperPlayServerEntityMetadata(entityId, List.of(
                                            // Setting text to an empty component. This is intercepted in the entity_metadata listener where all placeholders and colors are applied.
                                            new EntityData<>(23, EntityDataTypes.ADV_COMPONENT, Component.empty())
                                    ));
                                    // Sending the packet to the viewer.
                                    PacketEvents.getAPI().getPlayerManager().sendPacket(viewer, packet);
                                    // Logging debug information to the console.
                                    plugin.debug("[E:" + entityId + "] Packet sent to user " + viewer.getName() + "... [P:" + packet.getNativePacketId() + "] [H:" + packet.hashCode() + "]");
                                });
                            });
                        }, () -> {
                            // Logging debug information to the console.
                            plugin.debug("[E:" + entityId + "] Cancelling the placeholders refresh task... [C:RETIRED]");
                            // Removing the task from the list of active tasks.
                            hasActiveTextRefreshTask.remove(entityId);
                        }, 10L, refreshInterval);
                    });
                }
            }
            // Handling mannequin's player tracking task.
            else if (entity instanceof Mannequin && entity.getPersistentDataContainer().has(DisplayEntities.Keys.MANNEQUIN_TRACK_NEAREST_PLAYER) == true) {
                // Skipping further logic if the task is already running.
                if (hasActiveDirectionTask.contains(entityId) == true)
                    return;
                // Scheduling logic to be executed on Folia's EntityScheduler if needed, or on the current thread, which is NOT the main thread.
                runWrapped(entity, () -> {
                    // Continuing only if the entity is being tracked by at least one player.
                    if (entity.getTrackedBy().isEmpty() == true)
                        return;
                    hasActiveDirectionTask.add(entityId);
                    // Calculating squared distance of the configured radius.
                    final float trackNearestPlayerRadius = plugin.configuration().trackNearestPlayerRadius();
                    final float squaredTrackNearestPlayerRadius = trackNearestPlayerRadius * trackNearestPlayerRadius;
                    // Logging debug information to the console.
                    plugin.debug("[E:" + entityId + "] Starting the mannequin radius search task task... [R:" + trackNearestPlayerRadius + "]");
                    // Scheduling a repeating task to refresh the entity's rotation to make it look at the nearest player.
                    entity.getScheduler().runAtFixedRate(plugin, (distanceTask) -> {
                        // Cancelling the task if nobody is tracking the entity.
                        if (entity.getTrackedBy().isEmpty() == true) {
                            // Logging debug information to the console.
                            plugin.debug("[E:" + entityId + "] Cancelling the mannequin radius search task... [C:SELF]");
                            // Cancelling the task.
                            distanceTask.cancel();
                            // Removing a task from the list of active tasks.
                            hasActiveDirectionTask.remove(entityId);
                            return;
                        }
                        // Scheduling further logic to be executed off the main thread or on Folia's EntityScheduler if needed.
                        runWrappedAsync(entity, () -> {
                            // Calculating the center point of the circular radius within which the mannequin entity will look at the nearest player.
                            final Location center = entity.getLocation().add(entity.getLocation().getDirection().normalize().multiply(trackNearestPlayerRadius - 0.35));
                            // Iterating over all viewers of the mannequin entity.
                            for (final Player viewer : entity.getTrackedBy()) {
                                // Skipping further logic if the viewer is not within the configured radius.
                                if (viewer.getLocation().distanceSquared(center) > squaredTrackNearestPlayerRadius)
                                    continue;
                                // Getting viewer's entity id.
                                final int viewerId = viewer.getEntityId();
                                // Generating identifier to store in the set and keep track of current tasks.
                                final String identifier = viewerId + ":" + entityId;
                                // Skipping further logic if the viewer is already being tracked.
                                if (isBeingTracked.contains(identifier) == true)
                                    continue;
                                isBeingTracked.add(identifier);
                                // Scheduling a repeating task...
                                entity.getScheduler().runAtFixedRate(plugin, (refreshTask) -> {
                                    // Cancelling the task if the viewer is no longer connected to the server.
                                    if (viewer.isConnected() == false) {
                                        // Removing viewer from the list.
                                        isBeingTracked.remove(identifier);
                                        // Cancelling the task.
                                        refreshTask.cancel();
                                        return;
                                    }
                                    // Resetting the mannequin position and cancelling the task if the viewer is no longer within the configured radius.
                                    if (viewer.getLocation().distanceSquared(center) > squaredTrackNearestPlayerRadius) {
                                        // Resetting a mannequin's body and head rotations.
                                        PacketEvents.getAPI().getPlayerManager().sendPacket(viewer, new WrapperPlayServerEntityRotation(entityId, entity.getYaw(), entity.getPitch(), false));
                                        PacketEvents.getAPI().getPlayerManager().sendPacket(viewer, new WrapperPlayServerEntityHeadLook(entityId, entity.getYaw()));
                                        // Removing viewer from the list.
                                        isBeingTracked.remove(identifier);
                                        // Cancelling the task.
                                        refreshTask.cancel();
                                        return;
                                    }
                                    // Calculating the direction for the mannequin to look at.
                                    final Location direction = entity.getLocation().setDirection(viewer.getEyeLocation().toVector().subtract(((Mannequin) entity).getEyeLocation().toVector()));
                                    // Updating the mannequin's body and head rotations to face the nearest player.
                                    PacketEvents.getAPI().getPlayerManager().sendPacket(viewer, new WrapperPlayServerEntityRotation(entityId, direction.getYaw(), direction.getPitch(), false));
                                    PacketEvents.getAPI().getPlayerManager().sendPacket(viewer, new WrapperPlayServerEntityHeadLook(entityId, direction.getYaw()));
                                }, () -> {
                                    // Resetting a mannequin's body and head rotations.
                                    PacketEvents.getAPI().getPlayerManager().sendPacket(viewer, new WrapperPlayServerEntityRotation(entityId, entity.getYaw(), entity.getPitch(), false));
                                    PacketEvents.getAPI().getPlayerManager().sendPacket(viewer, new WrapperPlayServerEntityHeadLook(entityId, entity.getYaw()));
                                    // Removing viewer from the list.
                                    isBeingTracked.remove(identifier);
                                }, 1L, 2L);
                            }
                        });
                    }, () -> {
                        // Logging debug information to the console.
                        plugin.debug("[E:" + entityId + "] Cancelling the mannequin radius search task... [C:RETIRED]");
                        // Removing the task from the list of active tasks.
                        hasActiveDirectionTask.remove(entityId);
                    }, 3L, 3L);
                });
            } else if (entity != null) {
                // Adding id of this entity to the list of entities that should not be handled.
                notHandled.add(entityId);
            }
        }
        // Handling text display formatting and placeholder parsing.
        else if (event.getPacketType() == PacketType.Play.Server.ENTITY_METADATA && event.getPlayer() instanceof Player player) {
            final var packet = new WrapperPlayServerEntityMetadata(event);
            // Getting the entity from it's int id. Can be null.
            final @Nullable Entity entity = SpigotConversionUtil.getEntityById(player.getWorld(), packet.getEntityId());
            // Checking if an entity with this id exists on the server and is a text display entity.
            if (entity instanceof TextDisplay) {
                // Getting the text contents stored inside PDC.
                final String text = entity.getPersistentDataContainer().getOrDefault(DisplayEntities.Keys.TEXT_CONTENTS, PersistentDataType.STRING, "");
                // Checking if text contains PlaceholderAPI placeholders.
                if (PlaceholderAPI.containsPlaceholders(text) == true) {
                    // Iterating over list entity metadata in search for text changes.
                    for (final EntityData<?> data : packet.getEntityMetadata()) {
                        if (data.getType() == EntityDataTypes.ADV_COMPONENT)
                            // Overriding with a parsed component.
                            ((EntityData<Component>) data).setValue(MiniMessage.miniMessage().deserialize(PlaceholderAPI.setPlaceholders(player, text)));
                    }
                }
            }
        }
    }

    private void runWrapped(final @NotNull Entity entity, final @NotNull Runnable runnable) {
        // Executing runnable on EntityScheduler when the server is using Folia.
        if (DisplayEntities.isFolia() == true)
            entity.getScheduler().run(plugin, (it) -> runnable.run(), null);
        // Otherwise, runnable is executed on the current thread.
        else runnable.run();
    }

    private void runWrappedAsync(final @NotNull Entity entity, final @NotNull Runnable runnable) {
        // Executing runnable on EntityScheduler when the server is using Folia.
        if (DisplayEntities.isFolia() == true)
            entity.getScheduler().run(plugin, (it) -> runnable.run(), null);
        // Otherwise, runnable is executed on async scheduler.
        else plugin.getServer().getAsyncScheduler().runNow(plugin, (it) -> runnable.run());
    }

}
