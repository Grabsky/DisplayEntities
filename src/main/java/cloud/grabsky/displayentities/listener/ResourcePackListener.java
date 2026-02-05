/*
 * DisplayEntities (https://github.com/Grabsky/DisplayEntities)
 *
 * MIT License
 *
 * Copyright (c) 2026 Grabsky (michal.czopek.foss@proton.me)
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
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public final class ResourcePackListener implements Listener {

    @Getter(AccessLevel.PUBLIC)
    private final @NotNull DisplayEntities plugin;

    // Responsible for storing packs that are being currently loaded.
    private final Multimap<UUID, UUID> loadingResourcePacks = ArrayListMultimap.create();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onResourcePackStatus(final @NotNull PlayerResourcePackStatusEvent event) {
        // Skipping event calls before player has fully loaded to the server. (Configuration Stage)
        if (event.getPlayer().isOnline() == false)
            return;
        // Getting the UUID of the player.
        final UUID uniqueId = event.getPlayer().getUniqueId();
        // Adding accepted resource-pack to the list of currently loading resource-packs for that player.
        if (event.getStatus() == PlayerResourcePackStatusEvent.Status.ACCEPTED)
            loadingResourcePacks.put(uniqueId, event.getID());
        // Once successfully loaded (or failed to download), removing resource-pack from the map.
        else if (event.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED || event.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
            loadingResourcePacks.remove(uniqueId, event.getID());
            // Refreshing display entities once all resource-packs are loaded.
            if (loadingResourcePacks.get(uniqueId).isEmpty() == true) {
                plugin.debug("[P:" + event.getPlayer().getName() + "] All resource-packs has been loaded... Refreshing display entities...");
                // Removing all packs associated with the player.
                loadingResourcePacks.removeAll(uniqueId);
                // Refreshing all display entities around the player.
                event.getPlayer().getNearbyEntities(event.getPlayer().getWorld().getSimulationDistance() * 16, (double) Math.abs(event.getPlayer().getWorld().getMinHeight() - event.getPlayer().getWorld().getMaxHeight()) / 2, event.getPlayer().getWorld().getSimulationDistance() * 16).forEach(it -> {
                    // Scheduling further logic to be executed off the main thread, or on Folia's EntityScheduler if needed.
                    runWrappedAsync(event.getPlayer(), () -> {
                        // Checking entity is a TextDisplay and was created by the plugin. Otherwise, skipping.
                        if (it instanceof TextDisplay entity && entity.getPersistentDataContainer().has(DisplayEntities.Keys.NAME) == true) {
                            // Getting the text contents stored inside PDC.
                            final String text = entity.getPersistentDataContainer().getOrDefault(DisplayEntities.Keys.TEXT_CONTENTS, PersistentDataType.STRING, "");
                            // Creating and sending entity metadata packet to the player. This should refresh the client-side text display and re-align all custom characters.
                            PacketEvents.getAPI().getPlayerManager().sendPacket(
                                    event.getPlayer(),
                                    new WrapperPlayServerEntityMetadata(entity.getEntityId(), List.of(
                                        new EntityData<>(23, EntityDataTypes.ADV_COMPONENT, MiniMessage.miniMessage().deserialize(PlaceholderAPI.setPlaceholders(event.getPlayer(), text)))
                                    ))
                            );
                            // Logging debug info to the console.
                            plugin.debug("[P:" + event.getPlayer().getName() + "] [E:" + entity.getEntityId() + "] Refreshing packet after loading all resource-packs.");
                        }
                    });
                });
            }
        }
    }

    private void runWrappedAsync(final @NotNull Entity entity, final @NotNull Runnable runnable) {
        // Executing runnable on EntityScheduler when server is using Folia.
        if (DisplayEntities.isFolia() == true)
            entity.getScheduler().run(plugin, (it) -> runnable.run(), null);
        // Otherwise, runnable is executed on async scheduler.
        else plugin.getServer().getAsyncScheduler().runNow(plugin, (it) -> runnable.run());
    }

}
