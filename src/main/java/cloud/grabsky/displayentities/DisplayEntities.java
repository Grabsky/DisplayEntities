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
package cloud.grabsky.displayentities;

import cloud.grabsky.displayentities.command.CommandDisplayAlignment;
import cloud.grabsky.displayentities.command.CommandDisplayBackground;
import cloud.grabsky.displayentities.command.CommandDisplayBillboard;
import cloud.grabsky.displayentities.command.CommandDisplayBlock;
import cloud.grabsky.displayentities.command.CommandDisplayBrightness;
import cloud.grabsky.displayentities.command.CommandDisplayCreate;
import cloud.grabsky.displayentities.command.CommandDisplayDelete;
import cloud.grabsky.displayentities.command.CommandDisplayEdit;
import cloud.grabsky.displayentities.command.CommandDisplayItem;
import cloud.grabsky.displayentities.command.CommandDisplayLineWidth;
import cloud.grabsky.displayentities.command.CommandDisplayMoveTo;
import cloud.grabsky.displayentities.command.CommandDisplayRefreshInterval;
import cloud.grabsky.displayentities.command.CommandDisplayReload;
import cloud.grabsky.displayentities.command.CommandDisplayRespawn;
import cloud.grabsky.displayentities.command.CommandDisplayScale;
import cloud.grabsky.displayentities.command.CommandDisplaySeeThrough;
import cloud.grabsky.displayentities.command.CommandDisplayTextManipulation;
import cloud.grabsky.displayentities.command.CommandDisplayTextOpacity;
import cloud.grabsky.displayentities.command.CommandDisplayTextShadow;
import cloud.grabsky.displayentities.command.CommandDisplayViewRange;
import cloud.grabsky.displayentities.command.visitor.BuilderVisitor;
import cloud.grabsky.displayentities.configuration.PluginConfiguration;
import cloud.grabsky.displayentities.listener.PacketListener;
import cloud.grabsky.displayentities.util.LombokExtensions;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.google.gson.Gson;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import revxrsal.commands.Lamp;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.bukkit.BukkitLampConfig;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.spec.ArrayCommentStyle;
import revxrsal.spec.CommentedConfiguration;
import revxrsal.spec.Specs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.ExtensionMethod;

@Accessors(fluent = true)
@ExtensionMethod(LombokExtensions.class)
public final class DisplayEntities extends JavaPlugin {

    @Getter(AccessLevel.PUBLIC)
    private static DisplayEntities instance;

    @Getter(AccessLevel.PUBLIC)
    private File configurationFile;

    @Getter(AccessLevel.PUBLIC)
    private PluginConfiguration configuration;

    @Getter(AccessLevel.PUBLIC)
    private Lamp<BukkitCommandActor> lamp;

    @Getter(AccessLevel.PUBLIC)
    private MiniMessage miniMessage = MiniMessage.miniMessage();

    @Getter(AccessLevel.PUBLIC)
    @Setter(value = AccessLevel.PUBLIC, onMethod_ = @Internal)
    private boolean isDebugEnabled = false;

    private static final ThreadLocal<Yaml> YAML = ThreadLocal.withInitial(() -> {
        final DumperOptions options = new DumperOptions();
        options.setSplitLines(false);
        options.setProcessComments(false);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        return new Yaml(options);
    });

    @Override
    public void onEnable() {
        instance = this;
        // Initializing instance File representing config.yml file of this plugin.
        this.configurationFile = new File(this.getDataFolder(), "config.yml");
        // Loading configuration file.
        this.configuration = Specs.fromConfig(
                PluginConfiguration.class,
                new CommentedConfiguration(this.configurationFile.toPath(), CommentedConfiguration.GSON, ArrayCommentStyle.COMMENT_FIRST_ELEMENT, YAML.get())
        );
        // Saving default contents to the configuration file.
        this.configuration.save();
        // Reloading and mapping configuration file contents to the PluginConfiguration instance.
        this.configuration.reload();
        // Printing warning to the console if PacketEvents is not installed.
        if (this.getServer().getPluginManager().getPlugin("packetevents") == null)
            if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null)
                this.getLogger().warning("PacketEvents is not installed. Placeholders in text displays will not be refreshed automatically.");
        // Otherwise, initializing it's API.
        else PacketEvents.getAPI().init();
        // Customizing BukkitLamp instance.
        final BukkitLampConfig<BukkitCommandActor> config = BukkitLampConfig.builder(this)
                // Brigadier must be disabled for completions filtering to work properly.
                .disableBrigadier(true)
                // Async completion must be disabled because Player#getNearbyEntities can only be called on the main thread.
                .disableAsyncCompletion(true)
                .build();
        // Initializing BukkitLamp instance.
        this.lamp = BukkitLamp.builder(config)
                // Registering @Dependency dependencies.
                .dependency(DisplayEntities.class, this)
                .dependency(PluginConfiguration.class, this.configuration)
                // Accepting the BuilderVisitor, which applies further customizations to the builder.
                .accept(new BuilderVisitor(this))
                // Building the instance.
                .build();
        // Registering plugin commands.
        this.registerCommands(this.lamp);
    }

    @Override
    public void onLoad() {
        if (this.getServer().getPluginManager().getPlugin("packetevents") != null || this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            // Initializing PacketEvents API.
            PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
            // Loading PacketEvents.
            PacketEvents.getAPI().load();
            // Registering event listeners.
            PacketEvents.getAPI().getEventManager().registerListener(new PacketListener(this), PacketListenerPriority.HIGHEST);
        }
    }

    private void registerCommands(final @NotNull Lamp<BukkitCommandActor> lamp) {
        // Management
        lamp.register(CommandDisplayCreate.INSTANCE);
        lamp.register(CommandDisplayDelete.INSTANCE);
        lamp.register(CommandDisplayReload.INSTANCE);
        lamp.register(CommandDisplayRespawn.INSTANCE);
        // Editing (Common)
        lamp.register(CommandDisplayEdit.INSTANCE);
        lamp.register(CommandDisplayScale.INSTANCE);
        lamp.register(CommandDisplayMoveTo.INSTANCE);
        lamp.register(CommandDisplayBillboard.INSTANCE);
        lamp.register(CommandDisplayViewRange.INSTANCE);
        lamp.register(CommandDisplayBrightness.INSTANCE);
        // Editing (Block)
        lamp.register(CommandDisplayBlock.INSTANCE);
        // Editing (Item)
        lamp.register(CommandDisplayItem.INSTANCE);
        // Editing (Text)
        lamp.register(CommandDisplayAlignment.INSTANCE);
        lamp.register(CommandDisplayLineWidth.INSTANCE);
        lamp.register(CommandDisplayBackground.INSTANCE);
        lamp.register(CommandDisplaySeeThrough.INSTANCE);
        lamp.register(CommandDisplayTextShadow.INSTANCE);
        lamp.register(CommandDisplayTextOpacity.INSTANCE);
        lamp.register(CommandDisplayRefreshInterval.INSTANCE);
        lamp.register(CommandDisplayTextManipulation.INSTANCE);
    }

    public void debug(final String message) {
        if (this.isDebugEnabled == true)
            this.getLogger().warning("[DEBUG] " + message);
    }

    @SuppressWarnings("PatternValidation")
    public void rebuildMiniMessage() {
        this.miniMessage = MiniMessage.builder()
                .tags(TagResolver.standard())
                .editTags(it -> {
                    this.configuration.predefinedColors().forEach((key, value) -> {
                        final @Nullable TextColor color = TextColor.fromHexString(value);
                        // Logging invalid invalid color definitions.
                        if (color == null) {
                            this.getLogger().warning("Color '" + key + "' with value '" + value + "' is not a valid hex color.");
                            return;
                        }
                        // Adding new tag.
                        it.tag(key, Tag.styling(color));
                    });
                })
                .build();
    }

    /* PERSISTENT DATA CONTAINER KEYS; FOR INTERNAL USE ONLY */

    public static final class Keys {

        /** Used to store and retrieve the name of a persistent display entity. */
        public static final NamespacedKey NAME = new NamespacedKey("display_entities", "name");

        /** Used to store and retrieve the string content associated with a persistent text display entity. */
        public static final NamespacedKey TEXT_CONTENTS = new NamespacedKey("display_entities", "text_contents");

        /** Used to store override for refresh interval time. */
        public static final NamespacedKey REFRESH_INTERVAL = new NamespacedKey("display_entities", "refresh_interval");

    }

    /* PLUGIN LOADER; FOR USE WITH PLUGIN-YML FOR GRADLE */

    @SuppressWarnings("UnstableApiUsage")
    public static final class PluginLoader implements io.papermc.paper.plugin.loader.PluginLoader {

        @Override
        public void classloader(final @NotNull PluginClasspathBuilder classpathBuilder) throws IllegalStateException {
            final MavenLibraryResolver resolver = new MavenLibraryResolver();
            // Parsing the file.
            try (final InputStream in = getClass().getResourceAsStream("/paper-libraries.json")) {
                final PluginLibraries libraries = new Gson().fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), PluginLibraries.class);
                // Adding repositories to the maven library resolver.
                libraries.asRepositories().forEach(resolver::addRepository);
                // Adding dependencies to the maven library resolver.
                libraries.asDependencies().forEach(resolver::addDependency);
                // Adding library resolver to the classpath builder.
                classpathBuilder.addLibrary(resolver);
            } catch (final IOException e) {
                throw new IllegalStateException(e);
            }
        }

        @RequiredArgsConstructor(access = AccessLevel.PUBLIC)
        private static final class PluginLibraries {

            private final Map<String, String> repositories;
            private final List<String> dependencies;

            public Stream<RemoteRepository> asRepositories() {
                return repositories.entrySet().stream().map(entry -> new RemoteRepository.Builder(entry.getKey(), "default", entry.getValue()).build());
            }

            public Stream<Dependency> asDependencies() {
                return dependencies.stream().map(value -> new Dependency(new DefaultArtifact(value), null));
            }
        }
    }

}
