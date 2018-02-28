![Laborus](https://github.com/Lergin/Laborus/raw/api_7.0/images/Laborus_Banner.png)


a Minecraft job plugin for [Sponge](https://Spongepowered.org "SpongePowered.org")

1. Features
2. How to use
  1. Commands
3. Configuration

## Features

* Jobs in Minecraft
* reward the placing and destroying of Blocks, the using of items and the killing and taming of mobs
* Job Abilities that eg. allow you to destroy blocks faster for 3 minutes every two hours
* anti replace farming system that blocks the rewarding of the placing and destroying of blocks on the same spot so the economy isn't as easily exploitable
* only allow doing the actions (eg. destroying of a block) after a specific level
* Job Boni like special or multiple drops or repairing of items
* completely configurable and translatable

A full lists of all possible actions, boni and abilities can be found in the Configuration section.

## How to use
To use this plugin you need a [Sponge](https://spongepowered.org) Server that is supporting the SpongeApi Version 5.2.
If you have a Server like this you only need to place the pluginfile into the mods folder.
When you have done this and you have started the server the default configuration file will automatically be added, but you still need to add the jobs you want to have to this file due to the missing of jobs in the default configuration.
Some ready configuration files with some jobs can be found in the [Configuration Cookbook in the Github repository](https://github.com/Lergin/Laborus/tree/master/ConfigurationCookbook). These files can just be used to replace the automatically created config and will get all missing configuration keys after the next stop of the server.

### Commands

#### /jobs addXP [job] [xp] [player]

This command adds the given amount of xp to the xp of the job.

Permission: `laborus.commands.addXp`

Permission for player arg: `laborus.commands.addXp.other_player`

#### /jobs change [join|leave] [job]

Joins or leaves the job. A job that is joined can give more xp for actions and special boni. The amount of jobs that can be joined can be limited.

Permission: none / can be set in the config

#### /jobs toggle

Activates or deactivates the job system for the player that executes this command.

Permission: `laborus.commands.toggle`

#### /jobs ability [job]

Starts the ability of the job.

Permission: `laborus.commands.ability`

#### /jobs info [job]

Shows information about the job if one is send with the command, otherwise a list of all jobs is shown.


#### /jobs setXP [job] [xp] [player]

This command sets the xp of the job.

Permission: `laborus.commands.setXp`

Permission for player arg: `laborus.commands.setXp.other_player`


## Configuration

A Documentation of the Configuration Settings can be found in the [Github Wiki](https://github.com/Lergin/Laborus/wiki/Configuration) or on [Sponge Ore](https://ore.spongepowered.org/Lergin/Laborus/pages/Configuration)

## JavaDocs / Api

The Plugin has an Api to add new Boni, Actions and Abilities. You can find more about this in the [JavaDocs](https://lergin.github.io/Laborus/de/lergin/laborus/api/package-summary.html).
