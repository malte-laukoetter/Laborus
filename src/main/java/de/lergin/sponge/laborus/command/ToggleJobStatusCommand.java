package de.lergin.sponge.laborus.command;

import de.lergin.sponge.laborus.Laborus;
import de.lergin.sponge.laborus.config.TranslationKeys;
import de.lergin.sponge.laborus.data.JobKeys;
import de.lergin.sponge.laborus.data.jobs.JobDataManipulatorBuilder;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * command for enabling and disabling the job system for specific players
 */
@ConfigSerializable
public class ToggleJobStatusCommand extends JobCommand {
    @Setting(value = "command", comment = "The command")
    private String COMMAND = "toggle";

    @Setting(value = "description", comment = "The description of the command")
    private Text DESCRIPTION = Text.of("Toggles if the jobsystem is activated for the player");

    @Setting(value = "permission", comment = "The permission needed to use the command")
    private String PERMISSION = "laborus.commands.toggle";

    public ToggleJobStatusCommand() {
        super();
    }

    @Override
    public CommandSpec getCommandSpec() {
        CommandSpec.Builder builder = CommandSpec.builder();

        builder.description(this.DESCRIPTION);

        builder.executor(this);

        if (!"".equals(this.PERMISSION)) {
            builder.permission(this.PERMISSION);
        }

        return builder.build();
    }

    @Override
    public List<String> getCommandAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add(this.COMMAND);

        return aliases;
    }

    @Override
    public CommandResult execute(CommandSource commandSource, CommandContext commandContext) throws CommandException {
        if (!(commandSource instanceof Player))
            throw new CommandException(Text.of("Only Players can use this command", false));

        Player player = (Player) commandSource;

        Boolean jobsEnabled = player.get(JobKeys.JOB_ENABLED).orElse(true);

        if (!player.offer(JobKeys.JOB_ENABLED, !jobsEnabled).isSuccessful()) {
            player.offer(new JobDataManipulatorBuilder().jobsEnabled(!jobsEnabled).create());
        }

        if(jobsEnabled){
            player.sendMessage(
                    Laborus.instance().translationHelper.get(
                            TranslationKeys.COMMAND_TOGGLE_ACTIVATED,
                            player
                    )
            );
        }else{
            player.sendMessage(
                    Laborus.instance().translationHelper.get(
                            TranslationKeys.COMMAND_TOGGLE_DEACTIVATED,
                            player
                    )
            );
        }

        return CommandResult.success();
    }

}
