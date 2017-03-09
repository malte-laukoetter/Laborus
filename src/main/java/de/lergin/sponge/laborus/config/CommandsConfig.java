package de.lergin.sponge.laborus.config;

import de.lergin.sponge.laborus.Laborus;
import de.lergin.sponge.laborus.command.*;
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
    @Setting(value = "ability")
    private AbilityStartCommand abilityStartCommand = new AbilityStartCommand();
    @Setting(value = "addXp")
    private AddXpCommand addXpCommand = new AddXpCommand();
    @Setting(value = "change")
    private ChangeJobCommand changeJobCommand = new ChangeJobCommand();
    @Setting(value = "info")
    private InfoCommand infoCommand = new InfoCommand();
    @Setting(value = "toggle")
    private ToggleJobStatusCommand toggleJobStatusCommand = new ToggleJobStatusCommand();

    @Setting(value = "mainCommand")
    public String command = "jobs";

    public void registerCommands(Laborus plugin){
        Map<List<String>, CommandCallable> childCommands = new HashMap<>();

        childCommands.put(abilityStartCommand.getCommandAliases(), abilityStartCommand.getCommandSpec());
        childCommands.put(addXpCommand.getCommandAliases(), addXpCommand.getCommandSpec());
        childCommands.put(changeJobCommand.getCommandAliases(), changeJobCommand.getCommandSpec());
        childCommands.put(toggleJobStatusCommand.getCommandAliases(), toggleJobStatusCommand.getCommandSpec());
        childCommands.put(infoCommand.getCommandAliases(), infoCommand.getCommandSpec());

        Sponge.getCommandManager().register(
                plugin,
                CommandSpec.builder().children(childCommands).executor(infoCommand).build(),
                command
        );
    }

    public CommandsConfig(){}
}
