![Laborus](https://lergin.de/img/plugins/Laborus_Banner.png)

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
* reward the placing and destroying of Blocks, the using of items and the killing and taming of mobs
* Job Abilities that eg. allow you to destroy blocks faster for 3 minutes every two hours
* anti replace farming system that blocks the rewarding of the placing and destroying of blocks on the same spot so the economy isn't as easily exploitable 
* only allow doing the actions (eg. destroying of a block) after a specific level
* Job Boni like special or multiple drops or repairing of items
* completely configurable and translatable

A full lists of all possible actions, boni and abilities you can find in the Configuration section.

## How to use
To use this plugin you need a [Sponge](https://spongepowered.org) Server that is supporting the SpongeApi Version 4.0.
If you have a Server like this you only need to place the pluginfile into the mods folder. 
When you have done this and you have started and stopped the server the default configuration file will automatically be added, but you still need to add the jobs you want to have to this file due to the missing of jobs in the default configuration. 
Some ready configuration files you can find in the [jobs folder in the Github repository](https://github.com/Lergin/Laborus/tree/master/jobs). These files you can just copy and paste into the configuration folder of the plugin and link them in the configuration file (de.lergin.sponge.jobs.conf) like this:
```Javascript
jobs: {
  miner: "miner.conf"
  woodcutter: "woodcutter.conf"
}
```

### Commands

#### /jobs addXP <job> <xp> [player]

This command adds the given amount of xp to the xp of the job.

Permission: `laborus.commands.addXp`

Permission for player arg: `laborus.commands.addXp.outher_player`

#### /jobs change <join|leave> <job>

Joins or leaves the job. A job that is joined can give more xp for actions and special boni. The amount of jobs that can be joined can be limited.

Permission: none / can be set in the config

#### /jobs toggle

Activates or deactivates the job system for the player that executes this command.

Permission: `laborus.commands.toggle`

#### /jobs ability <job>

Starts the ability of the job.

Permission: `laborus.commands.ability`

## Configuration

### Jobs

##### General settings

|Name|Description|Default value|
|----|-----------|-------------|
|name|the display name of the job|""|
|description|a description of the job|""|
|permission|a permission needed to join the job|""|
|use_default_level|does the job uses the default levels|true|
|level|the levels of the job <br> see Level|[]|
|bonus|a list of jobBoni|[]|
|ability|the ability of this job||
|ACTION_NAME <br>-> needs to be replaced by the name of the action|the settings for the action|{}|

#### Actions

##### Settings for JobItems

|Name|Description|Default value|
|----|-----------|-------------|
|xp|the amount of xp the player gets if he does the action with this item|0.0|
|needLevel|the level the player needs to have to do this action|0|

##### Possible actions:

|Name|Description|info|
|----|------------|----|
|destroyBlocks|for the destruction of blocks
|placeBlocks|placing of blocks
|killEntities|killing of entities|can't be canceled -> no needLevel
|damageEntities|damaging of entities
|useItems|interaction with the item in the hand (right and left click)
|tameEntities|when an entity is tamed

#### Boni


##### Settings

|Name|Description|Default value|
|----|-----------|-------------|
|probability|the probability the boni will be used at an action|0.05|
|sendMessage|should a message be send to the player if the boni is rewarded|false|
|message|the message that will be send if sendMessage is true|""|
|condition.minLevel|the min. level the player need to have to get this boni|Integer.MIN_VALUE|
|condition.maxLevel|the max. level the player is allowed to have to get this boni|Integer.MAX_VALUE|
|condition.onlySelected|only reward the boni if the job is selected|true|
|condition.actions|only reward if the action that starts this boni is in this list. <br> A comma separated list of actionNames within []|[] -> all actions will start the boni|
|condition.items|only reward if the item that starts this boni is in this list. <br> A comma separated list of itemNames within []|[] -> all items will start the boni|

##### Possible actions:

|Name|Works with Jobactions|Description|special config settings
|----|------------|---|---|
|multiDrop|destroyBlocks, placeBlocks|drops the item of the action another time|itemMultiplier -> amount of items that should be droped extra
|ep|all|drops some ep (not job xp)|minEp -> minimum of ep dropped <br> maxEp -> maximum of ep dropped
|itemRepair|all, needs an item in the hand slot with durability|repairs the item by a given percentage|minPercentage -> minimum the item gets repaired <br> maxPercentage -> maximum the item gets repaired
|itemDrop|all|drops an extra item|itemType -> the itemType of the item <br> amount -> the amount of items that should be dropped

#### Ability

##### Settings

|Name|Description|Default value|
|----|-----------|-------------|
|name|the name of the ability|none|
|coolDown|amount of seconds between uses|60|

##### Possible abilities:

|Name|Desctription|special config settings|
|----|------------|---|
|effect|adds a potion effect to the player|amplifier -> effect level<br>duration -> amount of seconds the effect holds on<br>type -> potion type<br>particles -> should the particles not be hidden<br>ambience -> the ambience setting of a effect

### Commands

### Translations


## Contribution
