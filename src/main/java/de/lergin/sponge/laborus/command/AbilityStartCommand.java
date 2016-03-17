package de.lergin.sponge.laborus.command;

import de.lergin.sponge.laborus.JobsMain;
import de.lergin.sponge.laborus.job.Job;
import de.lergin.sponge.laborus.util.ConfigHelper;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbilityStartCommand extends JobCommand {
    private final static ConfigurationNode configNode = ConfigHelper.getNode("commands", "abilityStart");

    public AbilityStartCommand() {
        super();
    }

    /**
     * creates the {@link CommandSpec} for the command
     *
     * @return the {@link CommandSpec}
     */
    @Override
    public CommandSpec getCommandSpec() {
        CommandSpec.Builder builder = CommandSpec.builder();

        builder.description(Text.of(configNode.getNode("description").getString()));

        builder.executor(getExecutor());

        final String permission = configNode.getNode("permission").getString();

        if(!"".equals(permission)){
            builder.permission(permission);
        }

        //only add jobs that have a ability
        Map<String, Job> jobs = new HashMap<>();
        JobsMain.instance().jobs.values().stream()
                .filter(Job::hasJobAbility)
                .forEach(job -> jobs.put(job.getId(), job));

        builder.arguments(GenericArguments.choices(
                Text.of(configNode.getNode("params", "job", "description").getString("job")),
                jobs
        ));

        return builder.build();
    }

    /**
     * creates the list of possible aliases for the command
     *
     * @return a list of aliases
     */
    @Override
    public List<String> getCommandAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add(configNode.getNode("command").getString("ability"));

        return aliases;
    }

    @Override
    protected CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {
        if(!(commandSource instanceof Player))
            return CommandResult.empty();

        final Player player = (Player) commandSource;

        final Job job =
                (Job) args.getOne(configNode.getNode("params", "job", "description").getString("job")).get();

        job.getJobAbility().startAbility(player);

        return CommandResult.success();
    }
}
