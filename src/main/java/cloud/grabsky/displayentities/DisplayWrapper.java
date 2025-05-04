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

import cloud.grabsky.displayentities.util.LombokExtensions;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.ExtensionMethod;

@Accessors(fluent = true)
@ExtensionMethod(LombokExtensions.class)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public sealed abstract class DisplayWrapper permits DisplayWrapper.Text, DisplayWrapper.Item, DisplayWrapper.Block {

    @Getter(AccessLevel.PUBLIC)
    protected final Class<? extends Display> type;

    @Getter(AccessLevel.PUBLIC)
    protected final @NotNull String name;

    protected final @NotNull Display entity;

    /**
     * Initial configuration of display entity, called the moment after it has been spawned.
     */
    public abstract <T extends DisplayWrapper> @NotNull T initialConfiguration();

    /**
     * Returns entity held by this wrapper.
     */
    public abstract <T extends Display> T entity();

    /**
     * Creates new instance of {@link DisplayWrapper}
     */
    @SuppressWarnings("unchecked")
    public static <T extends Display, W extends DisplayWrapper> @NotNull W create(final @NotNull T entity, final @NotNull String name) {
        // Setting name of the display.
        entity.getPersistentDataContainer().set(DisplayEntities.Keys.NAME, PersistentDataType.STRING, name);
        // Returning new instance of DisplayWrapper containing provided entity.
        if (entity instanceof TextDisplay display)
            return (W) new Text(display, name);
        else if (entity instanceof ItemDisplay display)
            return (W) new Item(display, name);
        else if (entity instanceof BlockDisplay display)
            return (W) new Block(display, name);
        // ...
        throw new IllegalArgumentException("UNSUPPORTED_ENTITY_TYPE");
    }

    /**
     * Creates new instance of {@link DisplayWrapper}
     */
    @SuppressWarnings("unchecked")
    public static <T extends Display, W extends DisplayWrapper> @NotNull W existing(final @NotNull T entity) {
        // Getting name of the display.
        final @NotNull String name = Objects.requireNonNull(entity.getPersistentDataContainer().get(DisplayEntities.Keys.NAME, PersistentDataType.STRING), "NAME_NOT_SET");
        // Returning new instance of DisplayWrapper containing provided entity.
        return switch (entity) {
            case TextDisplay  it -> (W) new Text(it, name);
            case ItemDisplay  it -> (W) new Item(it, name);
            case BlockDisplay it -> (W) new Block(it, name);
            default -> throw new IllegalArgumentException("UNSUPPORTED_ENTITY_TYPE");
        };
    }

    @SuppressWarnings("unchecked")
    public <T extends DisplayWrapper> @NotNull T as(final Class<T> type) throws ClassCastException {
        return (T) this;
    }

    public <T extends DisplayWrapper> boolean is(final Class<T> type) {
        return type.isAssignableFrom(type);
    }


    /**
     * Removes persistent data of the entity.
     */
    public <P, C> @NotNull DisplayWrapper remove(final @NotNull NamespacedKey key) {
        entity.getPersistentDataContainer().remove(key);
        return this;
    }

    /**
     * Sets persistent data of the entity.
     */
    public <P, C> @NotNull DisplayWrapper set(final @NotNull NamespacedKey key, final @NotNull PersistentDataType<P, C> type, @NotNull C value) {
        entity.getPersistentDataContainer().set(key, type, value);
        return this;
    }

    /**
     * Gets persistent data of the entity. Can be null.
     */
    public <P, C> @Nullable C get(final @NotNull NamespacedKey key, final @NotNull PersistentDataType<P, C> type) {
        return entity.getPersistentDataContainer().get(key, type);
    }

    /**
     * Gets persistent data of the entity or specified default. Cannot be null.
     */
    public <P, C> @NotNull C get(final @NotNull NamespacedKey key, final @NotNull PersistentDataType<P, C> type, @NotNull C def) {
        return entity.getPersistentDataContainer().getOrDefault(key, type, def);
    }


    public static final class Text extends DisplayWrapper {

        private Text(final @NotNull TextDisplay entity, final @NotNull String name) {
            super(TextDisplay.class, name, entity);
        }

        @Override @SuppressWarnings("unchecked")
        public @NotNull TextDisplay entity() {
            return (TextDisplay) this.entity;
        }

        @Override @SuppressWarnings("unchecked")
        public @NotNull DisplayWrapper.Text initialConfiguration() {
            this.entity().setBillboard(Display.Billboard.VERTICAL);
            this.entity().setRichText("Use <#65D85F>/display edit <#C7F1C5>" + MiniMessage.miniMessage().stripTags(name) + " <#65D85F>set_line <#C7F1C5>(line) (text) <reset>command to edit.");
            this.entity().getPersistentDataContainer().set(DisplayEntities.Keys.TEXT_CONTENTS, PersistentDataType.STRING, "Use <yellow>/display set_line " + MiniMessage.miniMessage().stripTags(name) + " <reset>command to edit.");
            return this;
        }

    }


    public static final class Item extends DisplayWrapper {

        private static final ItemStack DEFAULT_ITEM_STACK = new ItemStack(Material.DIAMOND);

        private Item(final @NotNull ItemDisplay entity, final @NotNull String name) {
            super(ItemDisplay.class, name, entity);
        }

        @Override @SuppressWarnings("unchecked")
        public @NotNull ItemDisplay entity() {
            return (ItemDisplay) this.entity;
        }

        @Override @SuppressWarnings("unchecked")
        public @NotNull DisplayWrapper.Item initialConfiguration() {
            this.entity().setBillboard(Display.Billboard.FIXED);
            this.entity().setItemStack(DEFAULT_ITEM_STACK);
            return this;
        }

    }


    public static final class Block extends DisplayWrapper {

        private static final BlockData DEFAULT_BLOCK_DATA = Material.GRASS_BLOCK.createBlockData();

        private Block(final @NotNull BlockDisplay entity, final @NotNull String name) {
            super(BlockDisplay.class, name, entity);
        }

        @Override @SuppressWarnings("unchecked")
        public @NotNull BlockDisplay entity() {
            return (BlockDisplay) this.entity;
        }

        @Override @SuppressWarnings("unchecked")
        public @NotNull DisplayWrapper.Block initialConfiguration() {
            this.entity().setBillboard(Display.Billboard.FIXED);
            this.entity().setBlock(DEFAULT_BLOCK_DATA);
            return this;
        }

    }


    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public enum Type {
        TEXT(EntityType.TEXT_DISPLAY),
        ITEM(EntityType.ITEM_DISPLAY),
        BLOCK(EntityType.BLOCK_DISPLAY);

        @Getter(AccessLevel.PUBLIC)
        private final EntityType type;

        public DisplayWrapper create(final @NotNull Location location, final @NotNull String name) {
            // Spawning new entity of this type at specified location.
            final Display entity = (Display) location.getWorld().spawnEntity(location, type, CreatureSpawnEvent.SpawnReason.COMMAND);
            // Wrapping and returning...
            return DisplayWrapper.create(entity, name);
        }

    }

}
