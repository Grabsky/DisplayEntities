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
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public enum ClickCommandListener implements Listener {
    INSTANCE;

    private final Map<UUID, Long> lastClicked = new HashMap<>();

    @EventHandler(ignoreCancelled = false) // Should still run even if the event was cancelled. Because of WorldGuard etc.
    public void onDisplayInteract(final PlayerInteractEntityEvent event) {
        // Returning if clicked entity does not have any command associated with it.
        if (event.getRightClicked().getPersistentDataContainer().has(DisplayEntities.Keys.CLICK_COMMAND) == false)
            return;
        // Returning if player is on cooldown.
        if (System.currentTimeMillis() - this.lastClicked.getOrDefault(event.getPlayer().getUniqueId(), 0L) < 500L)
            return;
        // Setting the last interaction time to now, putting player on a short cooldown.
        this.lastClicked.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
        // Getting the stored command.
        final String[] commands = PlaceholderAPI.setPlaceholders(event.getPlayer(), event.getRightClicked().getPersistentDataContainer().getOrDefault(DisplayEntities.Keys.CLICK_COMMAND, PersistentDataType.STRING, "")).split("\\$AND");
        // Iterating over all specified commands.
        for (final String command : commands) {
            // Stripping leading slash if needed and trimming leading and trailing whitespaces.
            final String finalCommand = command.trim().startsWith("/") == true ? command.substring(1).trim() : command.trim();
            // Invoking command associated with the clicked entity, or doing nothing if it's blank.
            if (command.isBlank() == false) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand);
            }
        }
    }

}
