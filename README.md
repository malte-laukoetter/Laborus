# Jobs
a Minecraft job plugin for [Sponge](https://Spongepowered.org "SpongePowered.org")

1. Features
2. How to use
3. Configuration
  1. Jobs
     1. Actions
     2. Boni
     3. Ability
  2. Commands
  3. Translation
4. Contribution

## Features

* Jobs in Minecraft
* reward the placing and destroying of Blocks, the using of items and the killing and thaming of mobs
* Job Abilitys that eg. allow you to destroy blocks faster for 3 minutes every two hours
* anti replace farming system that blocks the rewarding of the placing and destroying of blocks on the same spot so the economy isn't as easily exploidable 
* only allow doing the actions (eg. destroying of a block) after a specific level
* Job Boni like special or multiple drops or repairing of items
* completly configurable and translatable

For full lists of all possible actions, boni and abilitys klick here 

## How to use
To use this plugin you need a [Sponge](https://spongepowered.org) Server that is supporting the SpongeApi Version 4.0.
If you have a Server like this you only need to place the pluginfile into the mods folder. 
When you have done this and you have started and stopped the server the default configuration file will automaticly be added, but you still need to add the jobs you want to have to this file due to the missing of jobs in the default configuration. 
Some ready configuration files you can find in the [jobs folder in this repo](jobs). These files you can just copy and paste into the configuration folder of the plugin and link them in the configuration file (de.lergin.sponge.jobs.conf) like this:
```Javascript
jobs: {
  miner: "miner.conf"
  woodcutter: "woodcutter.conf"
}
```

## Configuration

### Jobs
#### Actions
#### Boni
#### Ability

### Commands

### Translations


## Contribution
