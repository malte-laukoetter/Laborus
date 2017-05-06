package de.lergin.laborus.implementation.boni;

import de.lergin.laborus.api.JobBonus;
import de.lergin.laborus.api.JobItem;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

@ConfigSerializable
public class CommandExecutionJobBonus extends JobBonus {
    @Setting(value = "command", comment = "the command to execute {player} will be replaced by the playername")
    private String command;

    @Override
    public void applyBonus(JobItem jobitem, Player player, Object item) {
        Sponge.getCommandManager().process(
                Sponge.getServer().getConsole(),
                command.replace("{player}", player.getName())
        );
    }

    public CommandExecutionJobBonus(){}
}
