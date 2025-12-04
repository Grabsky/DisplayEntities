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
import org.bukkit.entity.Mannequin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public enum MannequinListener implements Listener {
    INSTANCE;

    // Cancelling mannequins income damage if they are (1) created by this plugin and (2) are marked as invulnerable.
    @EventHandler(ignoreCancelled = true)
    public void onMannequinDamage(final EntityDamageEvent event) {
        if (event.getEntity() instanceof Mannequin mannequin)
            if (mannequin.getPersistentDataContainer().has(DisplayEntities.Keys.NAME) == true && mannequin.isInvulnerable() == true)
                event.setCancelled(true);
    }

    // Cancelling mannequins entering vehicles if they are (1) created by this plugin and (2) are marked as immovable.
    @EventHandler(ignoreCancelled = true)
    public void onVehicleEnter(final VehicleEnterEvent event) {
        if (event.getEntered() instanceof Mannequin mannequin)
            if (mannequin.getPersistentDataContainer().has(DisplayEntities.Keys.NAME) == true && mannequin.isImmovable() == true)
                event.setCancelled(true);
    }

}
