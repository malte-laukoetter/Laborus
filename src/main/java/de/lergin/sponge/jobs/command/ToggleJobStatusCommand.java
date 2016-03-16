package de.lergin.sponge.jobs.command;

import com.google.common.collect.ImmutableMap;
import de.lergin.sponge.jobs.data.JobKeys;
import de.lergin.sponge.jobs.util.ConfigHelper;
import de.lergin.sponge.jobs.util.TranslationHelper;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;

import static org.spongepowered.api.text.TextTemplate.arg;

// TODO: add a player argument 

/**
 * command for enabling and disabling the job system for specific players
 */
public class ToggleJobStatusCommand extends JobCommand {
    private final static ConfigurationNode configNode = ConfigHelper.getNode("commands", "toggleJobStatus");

    public ToggleJobStatusCommand() {
        super();
    }

    @Override
    public CommandSpec getCommandSpec() {
        CommandSpec.Builder builder = CommandSpec.builder();

        builder.description(Text.of(configNode.getNode("description").getString()));

        builder.executor(getExecutor());

        final String permission = configNode.getNode("permission").getString();

        if(!"".equals(permission)){
            builder.permission(permission);
        }

        return builder.build();
    }

    @Override
    public List<String> getCommandAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add(configNode.getNode("command").getString("toggleJobStatus"));

        return aliases;
    }

    @Override
    protected CommandResult execute(CommandSource commandSource, CommandContext commandContext) throws CommandException {
        if(!(commandSource instanceof Player))
            return CommandResult.empty();

        Player player = (Player) commandSource;

        Boolean jobsEnabled = player.get(JobKeys.JOB_ENABLED).orElse(true);

        player.offer(JobKeys.JOB_ENABLED, !jobsEnabled);

        player.sendMessage(
                TranslationHelper.template(
                        TextTemplate.of(
                                TextColors.AQUA,
                                "Toggled enabled status of jobSystem to: ",
                                arg("status").color(TextColors.GREEN).build()
                        ),
                        "job_toggle"
                ),
                ImmutableMap.of(
                        "status", Text.of(!jobsEnabled)
                )
        );

        return CommandResult.success();
    }

}
