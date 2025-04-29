<div align="center">
  
  ![](https://i.imgur.com/aziwQIL.png)

  [![GitHub Release](https://img.shields.io/github/v/release/Grabsky/DisplayEntities?logo=github&labelColor=%2324292F&color=%23454F5A)](https://github.com/Grabsky/DisplayEntities/releases/latest)
  [![Modrinth Downloads](https://img.shields.io/modrinth/dt/display-entities?logo=modrinth&logoColor=white&label=downloads&labelColor=%23139549&color=%2318c25f)](https://modrinth.com/plugin/display-entities)
  [![Discord](https://img.shields.io/discord/1366851451208601783?cacheSeconds=3600&logo=discord&logoColor=white&label=%20&labelColor=%235865F2&color=%23707BF4)](https://discord.com/invite/PuzqF2Yd5q)
  [![CodeFactor Grade](https://img.shields.io/codefactor/grade/github/Grabsky/DisplayEntities?logo=codefactor&logoColor=white&label=%20)](https://www.codefactor.io/repository/github/grabsky/displayentities/issues/main)

</div>

Small plugin that simplifies display entity management. You can use it for map-making, holograms or anything you'd use display entities for.

<br />

## Features

- **Pure Simplicity**  
  Based on existing Bukkit and Paper APIs with no NMS access involved.

- **Minimal Footprint**  
  Basically a user-friendly wrapper around display entities. There's not much logic attached to it.

- **Compatibility**  
  Entities created by the plugin are real entities that can be modified with commands or other plugins.

- **Folia Support**  
  Designed to run on Paper and Folia servers. Folia Scheduler APIs are used when available.

- **PlaceholderAPI Support** (Optional)  
  Placeholders inside text displays are parsed and displayed per-player. Requires **[PlaceholderAPI](https://github.com/PlaceholderAPI/PlaceholderAPI)** and **[PacketEvents](https://github.com/retrooper/packetevents)**.

<br/>

## Requirements
Plugin runs only on **Paper 1.21.4** and above, powered by **Java 21** or higher.

<br/>

## Details
Plugin is based on display entities that were added by Mojang back in 1.19.4 as a light and powerful alternative to armor stands. Reasonable amounts of them should never be a subject of lag. Plugin does not store anything on it's own except for necessary data attached to the PDC (Persistent Data Container) of each entity it interacts with. It means that each "hologram" created by the plugin will be kept in the world until you manually remove it, either by using the `/display delete (name)` command provided by the plugin, or vanilla `/kill` command. If that's not what you're looking for, consider using a fully packet-based plugin such as **[FancyHolograms](https://modrinth.com/plugin/fancyholograms)**.

<ins>**Still not sure what is all of that about?**</ins>  
Join our Discord server and ask your question. (Not set up yet)

<br/>

## Compiling
```bash
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
