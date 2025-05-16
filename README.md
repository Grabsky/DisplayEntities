<div align="center">
  
  ![](https://i.imgur.com/aziwQIL.png)

  [![GitHub Release](https://img.shields.io/github/v/release/Grabsky/DisplayEntities?logo=github&labelColor=%2324292F&color=%23454F5A)](https://github.com/Grabsky/DisplayEntities/releases/latest)
  [![Modrinth Downloads](https://img.shields.io/modrinth/dt/display-entities?logo=modrinth&logoColor=white&label=downloads&labelColor=%23139549&color=%2318c25f)](https://modrinth.com/plugin/display-entities)
  [![Discord](https://img.shields.io/discord/1366851451208601783?cacheSeconds=3600&logo=discord&logoColor=white&label=%20&labelColor=%235865F2&color=%23707BF4)](https://discord.com/invite/PuzqF2Yd5q)
  [![CodeFactor Grade](https://img.shields.io/codefactor/grade/github/Grabsky/DisplayEntities?logo=codefactor&logoColor=white&label=%20)](https://www.codefactor.io/repository/github/grabsky/displayentities/issues/main)

</div>

To put it simple, **DisplayEntities** is an utility plugin that adds commands for manipulating **server-side** display entities. Display entities are **lightweight** and **powerful** alternative to armor stands. Reasonable amount of these entities should **never** be a subject to server lag.

Plugin does not store anything on it's own except configuration file and necessary data attached to the PDC (Persistent Data Container) of each entity it interacts with. It means that each entity created by the plugin will be kept in the world until manually removed. This can be done either by using `/display delete (name)` command provided by the plugin, or vanilla `/kill` command.

To make the plugin a bit more useful, I added **MiniMessage** parsing and packet-level **PlaceholderAPI** support. That should possibly make it as useful as any other holograms plugin you're already familiar with.

Use with [**Axiom**](https://modrinth.com/mod/axiom) and [**AxiomPaperPlugin**](https://modrinth.com/plugin/axiom-paper-plugin) to make the overall experience **even better**!

<br/>

<details>
  <summary><strong>(Click here to expand list of available commands)</strong></summary>

```yml
# () - Required Argument, [] - Optional Argument

=== Management ===

# Shows list of available commands.
Command: /display help [page]
Permission: displayentities.command.display.help

# Clones specified display.
Command: /display clone (display) (name)
Permission: displayentities.command.display.clone

# Creates a new display of specified type.
Command: /display create (type) (name)
Permission: displayentities.command.display.create

# Permanently deletes specified display.
Command: /display delete (type) (name)
Permission: displayentities.command.display.delete

# Respawns specified display for all viewers.
# Use it after adding placeholders or modifying placeholder refresh interval.
Command: /display respawn (type) (name)
Permission: displayentities.command.display.respawn

=== Editing (Common) ===

# Modifies scale of specified display.
Command: /display edit (display) scale (x) (y) (z)
Permission: displayentities.command.display.edit.scale

# Modifies view range of specified display.
# Range must be between 0.0 and 1.0 as this is how it is scaled internally.
Command: /display edit (display) view_range (range)
Permission: displayentities.command.display.edit.view_range

# Moves entity to specified coordinates.
# Character ~ can be used and is replaced with sender's current coordinate.
Command: /display edit (display) move_to (x) (y) (Z)
Permission: displayentities.command.display.edit.move_to

# Modifies billboard of specified display.
# Billboard can be fixed, center, horizontal or vertical.
Command: /display edit (display) billboard (billboard)
Permission: displayentities.command.display.edit.billboard

# Modifies brightness of specified display.
# Brightness is an integer between 0 and 15.
Command: /display edit (display) brightness (block | sky) (brightness)
Permission: displayentities.command.display.edit.brightness

=== Editing (Text) ===

# Adds new line to text contents of specified text display.
# Supports MiniMessage formatting and PlaceholderAPI.
Command: /display edit (display) add_line (text)
Permission: displayentities.command.display.edit.add_line

# Removes specified line, from specified text display.
Command: /display edit (display) remove_line (line)
Permission: displayentities.command.display.edit.remove_line

# Sets contents of specified line, to specified text.
# Supports MiniMessage formatting and PlaceholderAPI.
Command: /display edit (display) set_line (line) (text)
Permission: displayentities.command.display.edit.set_line

# Inserts new line before the specified line number.
# Supports MiniMessage formatting and PlaceholderAPI.
Command: /display edit (display) insert_line (line) (text)
Permission: displayentities.command.display.edit.insert_line

# Modifies ticks interval at which this text display will be refreshed to all viewers.
# Interval can be number of ticks, where 20 ticks is equal to 1 second.
# Interval can also be set to 'default' which makes it use a value specified in configuration file.
Command: /display edit (display) refresh_interval (ticks) (text)
Permission: displayentities.command.display.edit.refresh_interval

# Modifies text alignment of specified text display.
# Alignment can be left, center or right.
Command: /display edit (display) alignment (alignment)
Permission: displayentities.command.display.edit.alignment

# Modifies background color of specified text display.
# Color can be either named (like cyan) or any hex value (like #00FF00).
# Opacity is specified in percentage, where 0% is fully transparent, and 100% is fully opaque.
# Opacity, if unspecified, defaults to fully opaque.
Command: /display edit (display) background (color) [opacity]
Permission: displayentities.command.display.edit.background

# Modifies line width of specified text display.
Command: /display edit (display) line_width (width)
Permission: displayentities.command.display.edit.line_width

# Modifies see through state of specified text display.
Command: /display edit (display) see_through (true / false)
Permission: displayentities.command.display.edit.see_through

# Modifies text shadow state of specified text display.
Command: /display edit (display) text_shadow (true / false)
Permission: displayentities.command.display.edit.text_shadow

# Modifies text opacity of specified text display.
# Opacity is specified in percentage, where 0% is fully transparent, and 100% is fully opaque.
Command: /display edit (display) text_opacity (opacity)
Permission: displayentities.command.display.edit.text_opacity

=== Editing (Block) ===

# Modifies the block that is represented by specified block display.
# Block can be a predefined block type, or currently held block. Latter can be set with @main_hand or @off_hand selector.
Command: /display edit (display) block (@main_hand | @off_hand | type)
Permission: displayentities.command.display.edit.block

=== Editing (Item) ===

# Modifies the item that is represented by specified item display.
# Item can be a predefined item type, or currently held item. Latter can be set with @main_hand or @off_hand selector.
Command: /display edit (display) item (@main_hand | @off_hand | type)
Permission: displayentities.command.display.edit.item
```

</details>


<br />

## Features

- **Pure Simplicity**  
  Based on existing Bukkit and Paper APIs with no NMS access involved.

- **Minimal Footprint**  
  Basically a user-friendly wrapper around display entities. There's not much logic attached to it.

- **Compatibility**  
  Entities created by the plugin are real entities that can be modified with commands and other plugins.

- [**MiniMessage Support**](https://docs.advntr.dev/minimessage/format.html)  
  Text display entities are capable of parsing MiniMessage formatting.

- [**Folia Support**](https://github.com/PaperMC/Folia)  
  Designed to run on Paper and Folia servers. Folia Scheduler APIs are used when needed.

- [**PlaceholderAPI Support**](https://github.com/PlaceholderAPI/PlaceholderAPI)  
  Placeholders inside text displays are parsed and displayed per-player.  
  <sup>(Requires **[PlaceholderAPI](https://github.com/PlaceholderAPI/PlaceholderAPI)** and **[PacketEvents](https://github.com/retrooper/packetevents)**)</sup>

<br/>

## Requirements
Plugin runs only on **Paper** (or **Folia**) **1.21.4** and above, powered by **Java 21** or higher.

<br/>

## Compiling
```python
# Cloning the repository.
$ git clone https://github.com/Grabsky/DisplayEntities.git
# Entering the cloned repository.
$ cd ./DisplayEntities
# Compiling and building artifacts.
$ ./gradlew clean build
```

<br/>

## Contributing
This project is open for contributions. Help in regards of improving performance, adding new features or fixing bugs is greatly appreciated.
