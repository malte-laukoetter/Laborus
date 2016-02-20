package de.lergin.sponge.jobs.command;

import de.lergin.sponge.jobs.JobsMain;
import de.lergin.sponge.jobs.data.JobKeys;
import de.lergin.sponge.jobs.job.Job;
import de.lergin.sponge.jobs.util.ConfigHelper;
import de.lergin.sponge.jobs.util.TranslationHelper;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddXpCommand extends JobCommand {
    private final static ConfigurationNode configNode = ConfigHelper.getNode("commands", "addXp");

    public AddXpCommand() {
        super();
    }

    /**
     * creates the {@link CommandSpec} for the command
     *
     * @return the {@link CommandSpec}
     */
    @Override
    protected CommandSpec getCommandSpec() {
        CommandSpec.Builder builder = CommandSpec.builder();

        builder.description(Text.of(configNode.getNode("description").getString()));

        builder.executor(getExecutor());

        final String permission = configNode.getNode("permission").getString();

        if(!"".equals(permission)){
            builder.permission(permission);
        }

        CommandElement[] commandElements = new CommandElement[3];

        commandElements[0] =
                GenericArguments.choices(
                        Text.of(configNode.getNode("params", "job", "description").getString("job")),
                        JobsMain.instance().jobs
                );

        commandElements[1] =
                GenericArguments.doubleNum(
                        Text.of(configNode.getNode("params", "xp", "description").getString("xp"))
                );

        commandElements[2] = GenericArguments.optional(
                getCommandElementWithPermission(
                        GenericArguments.player(Text.of(
                                configNode.getNode("params", "player", "description").getString("player")
                        )),
                        configNode.getNode("params", "player", "permission").getString("")
                )
        );

        builder.arguments(commandElements);

        return builder.build();
    }

    /**
     * creates the list of possible aliases for the command
     *
     * @return a list of aliases
     */
    @Override
    protected List<String> getCommandAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add(configNode.getNode("command").getString("addJobXp"));

        return aliases;
    }

    /**
     * @param commandSource
     * @param args
     * @see CommandExecutor#execute(CommandSource, CommandContext)
     */
    @Override
    protected CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {
        if(!(commandSource instanceof Player ||
                args.hasAny(configNode.getNode("params", "player", "description").getString("player"))))
            return CommandResult.empty();

        Player player = (Player) args.getOne(
                configNode.getNode("params", "player", "description").getString("player")
        ).orElse(commandSource);

        Map<String, Double> jobData = player.get(JobKeys.JOB_DATA).orElse(new HashMap<>());

        String jobId =
                ((Job) args.getOne(configNode.getNode("params", "job", "description").getString("job")).get()).getId();
        double addXp = (double) args.getOne(configNode.getNode("params", "xp", "description").getString("xp")).get();
        double newXp = jobData.getOrDefault(jobId, 0.0) + addXp;


        jobData.put(jobId, newXp);

        player.offer(JobKeys.JOB_DATA, jobData);

        player.sendMessage(TranslationHelper.p(player, "player.info.job.jobs.add_xp", addXp, jobId, newXp));

        return CommandResult.success();
    }
}
