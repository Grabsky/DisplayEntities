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
