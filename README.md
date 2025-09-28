<div align="center">

![](https://i.imgur.com/aziwQIL.png)

[![GitHub Release](https://img.shields.io/github/v/release/Grabsky/DisplayEntities?logo=github&labelColor=%2324292F&color=%23454F5A)](https://github.com/Grabsky/DisplayEntities/releases/latest)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/display-entities?logo=modrinth&logoColor=white&label=downloads&labelColor=%23139549&color=%2318c25f)](https://modrinth.com/plugin/display-entities)
[![Discord](https://img.shields.io/discord/1366851451208601783?cacheSeconds=3600&logo=discord&logoColor=white&label=%20&labelColor=%235865F2&color=%23707BF4)](https://discord.com/invite/PuzqF2Yd5q)
[![CodeFactor Grade](https://img.shields.io/codefactor/grade/github/Grabsky/DisplayEntities?logo=codefactor&logoColor=white&label=%20)](https://www.codefactor.io/repository/github/grabsky/displayentities/issues/main)

**DisplayEntities** is an utility plugin adding commands for manipulation of server-side Display Entities, Interaction Entities and (soon) Mannequins. Comes with [**PlaceholderAPI**](https://github.com/PlaceholderAPI/PlaceholderAPI) and [**MiniMessage**](https://docs.advntr.dev/minimessage/format.html)   support.

</div>

<br/>

### But what *are* Display Entities?
Display entities are lightweight hologram-like entities added in 1.19.4. They are capable of displaying text, items and blocks, and serve as an alternative to armor stands. You can read more [**here**](https://minecraft.wiki/w/Display).

### How does the plugin work?
Each entity created by the plugin will be *physically* kept in the world until manually removed. This can be done either by plugin-provided command or built-in, vanilla commands.

Since all entities are server-side, you can move / scale them freely with external tools such us [**Axiom**](https://modrinth.com/mod/axiom).

### How is that an *utility* plugin?
Plugin does not store anything on it's own except configuration file and necessary data attached to the PDC (Persistent Data Container) of each entity it interacts with. It leaves minimal footprint on the server.

To make the plugin a bit more useful, it comes with **MiniMessage** support and packet-level **PlaceholderAPI** hook. That should possibly make it as useful as any other holograms plugin you're already familiar with.

<br/>

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

## Getting Started
Small guide how to get started until I can finally get a documentation page up and running.

### Commands
Type **/display help** to see list of available commands. Make sure you're a server operator or have correct permissions applied to your account.

### Permissions

<details>
  <summary>Click here to expand list of available permissions.</summary>

  ```yml
  # Management Commands
  displayentities.command.display.help
  displayentities.command.display.clone
  displayentities.command.display.create
  displayentities.command.display.delete
  displayentities.command.display.reload
  displayentities.command.display.respawn
  displayentities.command.display.teleport

  # Editing Commands (Text, Block, Item, Interaction)
  displayentities.command.display.edit.move_to

  # Editing Commands (Text, Block, Item)
  displayentities.command.display.edit.scale
  displayentities.command.display.edit.view_range
  displayentities.command.display.edit.billboard
  displayentities.command.display.edit.brightness
  displayentities.command.display.edit.rotate_x
  displayentities.command.display.edit.rotate_y

  # Editing Commands (Text)
  displayentities.command.display.edit.add_line
  displayentities.command.display.edit.remove_line
  displayentities.command.display.edit.set_line
  displayentities.command.display.edit.insert_line
  displayentities.command.display.edit.refresh_interval
  displayentities.command.display.edit.alignment
  displayentities.command.display.edit.background
  displayentities.command.display.edit.line_width
  displayentities.command.display.edit.see_through
  displayentities.command.display.edit.text_Shadow
  displayentities.command.display.edit.text_opacity

  # Editing Commands (Block)
  displayentities.command.display.edit.block

  # Editing Commands (Item)
  displayentities.command.display.edit.item

  # Editing Commands (Interaction)
  displayentities.command.display.edit.width
  displayentities.command.display.edit.height
  displayentities.command.display.edit.response

  # Editing Commands (Block, Item, Interaction)
  displayentities.command.display.edit.glow
  ```

</details>

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
