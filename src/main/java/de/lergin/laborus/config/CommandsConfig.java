package de.lergin.laborus.config;

import de.lergin.laborus.Laborus;
import de.lergin.laborus.command.*;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.spec.CommandSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigSerializable
public class CommandsConfig {
    @Setting(value = "ability", comment = "starts the job ability")
    private AbilityStartCommand abilityStartCommand = new AbilityStartCommand();
    @Setting(value = "addXp", comment = "adds job xp")
    private AddXpCommand addXpCommand = new AddXpCommand();
    @Setting(value = "setXp", comment = "sets the job xp")
    private SetXpCommand setXpCommand = new SetXpCommand();
    @Setting(value = "change", comment = "changes the job")
    private ChangeJobCommand changeJobCommand = new ChangeJobCommand();
    @Setting(value = "info", comment = "shows information about jobs")
    private InfoCommand infoCommand = new InfoCommand();
    @Setting(value = "toggle", comment = "command to toggle the active status of the job system (only affects the player itself)")
    private ToggleJobStatusCommand toggleJobStatusCommand = new ToggleJobStatusCommand();
    @Setting(value = "reload", comment = "command to reload the config")
    private ReloadCommand reloadCommand = new ReloadCommand();

    @Setting(value = "mainCommand", comment = "command needed to place before each of the commands")
    public String command = "jobs";

    public void registerCommands(Laborus plugin){
        Map<List<String>, CommandCallable> childCommands = new HashMap<>();

        childCommands.put(abilityStartCommand.getCommandAliases(), abilityStartCommand.getCommandSpec());
        childCommands.put(addXpCommand.getCommandAliases(), addXpCommand.getCommandSpec());
        childCommands.put(setXpCommand.getCommandAliases(), setXpCommand.getCommandSpec());
        childCommands.put(changeJobCommand.getCommandAliases(), changeJobCommand.getCommandSpec());
        childCommands.put(toggleJobStatusCommand.getCommandAliases(), toggleJobStatusCommand.getCommandSpec());
        childCommands.put(infoCommand.getCommandAliases(), infoCommand.getCommandSpec());
        childCommands.put(reloadCommand.getCommandAliases(), reloadCommand.getCommandSpec());

        Sponge.getCommandManager().register(
                plugin,
                CommandSpec.builder().children(childCommands).executor(infoCommand).build(),
                command
        );
    }

    public CommandsConfig(){}
}
