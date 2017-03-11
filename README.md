![Laborus](https://lergin.de/img/plugins/Laborus_Banner.png)

a Minecraft job plugin for [Sponge](https://Spongepowered.org "SpongePowered.org")

1. Features
2. How to use
  1. Commands
3. Configuration
  1. General
  2. Jobs
     1. General settings
     2. JobActions
     3. JobBoni
     4. Ability
  3. Translations

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

Permission for player arg: `laborus.commands.addXp.outher_player`

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

## Configuration

### General

|Name|Description|Default value|
|----|-----------|-------------|
|antiReplaceActive|is the anti replace farming system active|false|
|antiReplaceTime|the time in hours after that a block will again be rewarded if the anti replace farming system is active|48|
|enabledGamemodes|list of gamemodes the job system is activated in|[adventure, survival]|
|maxSelectedJobs|maximum amount of jobs that can be selected at the same time|1|
|xpWithoutJob|the value the ep is multiplied by if the job is not selected|0.5|
|fallbackLanguage|the language to use if the language of the player is not available|"en"|
|level|the default ep borders for the next level|[0,50,100...]|
|jobs|the list of the configuraitons for the jobs|[]|
|jobFiles|a list of files that each have a job configuration|[]|
|commands|the settings for the commands|automatically generated, can be edited|
|translations|the settings for the messages|automatically generated, can be edited, if edited to [] it will stay empty|
|translationFiles|a map of languages and files that each have a translation configuration for one language. The base path is this folder|{}|

### Jobs

##### General settings

|Name|Description|Default value|
|----|-----------|-------------|
|name|the display name of the job|""|
|description|a description of the job|""|
|permission|a permission needed to join the job|""|
|level|the levels of the job if it is empty the default jobs will be used|[]|
|bonus|see JobBoni|{multiDrop=[], ep=[], itemRepair=[], itemDrop=[], economy=[]}|
|ability|the ability of this job|{}|
|actions|see JobActions|{break=[], place=[], kill=[], damage=[], use=[], tame=[]}|

#### JobActions

All JobActions need a list of JobItems with the informations

|Name|Description|Supported JobItem items|
|----|------------|----|
|break|for the destruction of blocks|blockstate
|place|placing of blocks|blockstate
|kill|killing of entities, can't be canceled -> no needLevel|entitytype
|damage|damaging of entities|entitytype
|use|interaction with the item in the hand (right and left click)|itemtype
|tame|when an entity is tamed|entitytype

##### Settings for JobItems

|Name|Description|Default value|
|----|-----------|-------------|
|xp|the amount of xp the player gets if he does the action with this item|0.0|
|needLevel|the level the player needs to have to do this action|0|
|item|the item of the jobaction|""

The items are strings like "minecraft:stone" for stone blocks or "minecraft:wooden_pickaxe" for wooden pickaxes.

#### JobBoni

The available boni types are:

|Name|Works with Jobactions|Description|
|----|------------|---|---|
|multiDrop|destroyBlocks, placeBlocks|drops the item of the action another time
|ep|all|drops some ep (not job xp)
|itemRepair|all, needs an item in the hand slot with durability|repairs the item by a given percentage
|itemDrop|all|drops an extra item
|economy|all|awards economy money

Each of these Boni have a list of Boni Settings under them.

##### Boni Settings

|Name|Description|Default value|
|----|-----------|-------------|
|probability|the probability the boni will be used at an action|0.05|
|sendMessage|should a message be send to the player if the boni is rewarded|false|
|message|the message that will be send if sendMessage is true|""|
|minLevel|the min. level the player need to have to get this boni|0|
|maxLevel|the max. level the player is allowed to have to get this boni|-1|
|onlySelected|only reward the boni if the job is selected|true|

Extra Settings for the different Boni Types:

###### multiDrop

|Name|Description|Default value|
|----|-----------|-------------|
|extraDrops|the amount of extra items|0|

###### ep

|Name|Description|Default value|
|----|-----------|-------------|
|minEp|the minimum amount of Ep|0|
|maxEp|the maximum amount of Ep|0|

###### itemRepair

|Name|Description|Default value|
|----|-----------|-------------|
|minPercent|the minimum percent the item gets repaired|0|
|maxPercent|the maximum percent the item gets repaired|0|

###### itemDrop

|Name|Description|Default value|
|----|-----------|-------------|
|item|the itemstack that will be droped|{type="dirt", amount=1}|

###### economy

|Name|Description|Default value|
|----|-----------|-------------|
|amountMin|the minimum amount of money rewarded|0.0|
|amountMax|the maximum amount of money rewarded|0.0|
|currency|the id of the currency to use, defaults to the default currency| |

#### Ability

|Name|Description|Default value|
|----|-----------|-------------|
|name|the name of the ability|""|
|cooldown|amount of seconds between uses|0|
|potionEffect|adds a potion effect to the player|{amplifier=0, duration=1, potionType="minecraft:speed", particles=true, ambiance=false}

### Translation

A Documentation of the Translation Settings and which variables are available can be found in the [Github Wiki](https://github.com/Lergin/Laborus/wiki/Translations) or on [Sponge Ore](https://ore.spongepowered.org/Lergin/Laborus/pages/Translations)
