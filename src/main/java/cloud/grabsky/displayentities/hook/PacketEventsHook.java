package cloud.grabsky.displayentities.hook;

import cloud.grabsky.displayentities.DisplayEntities;
import cloud.grabsky.displayentities.listener.PacketListener;
import cloud.grabsky.displayentities.listener.ResourcePackListener;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public final class PacketEventsHook {

    private final DisplayEntities plugin;

    public PacketEventsHook enable() {
        // Initializing PacketEvents API.
        PacketEvents.getAPI().init();
        // Registering resource-pack listener.
        plugin.getServer().getPluginManager().registerEvents(new ResourcePackListener(plugin), plugin);
        // Returning...
        return this;
    }

    public PacketEventsHook init() {
        // Initializing PacketEvents API.
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(plugin));
        // Loading PacketEvents.
        PacketEvents.getAPI().load();
        // Registering event listeners.
        PacketEvents.getAPI().getEventManager().registerListener(new PacketListener(plugin), PacketListenerPriority.HIGHEST);
        // Returning...
        return this;
    }

}
