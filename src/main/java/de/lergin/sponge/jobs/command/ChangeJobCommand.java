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

import java.util.*;

public class ChangeJobCommand extends JobCommand{
    private final static ConfigurationNode configNode = ConfigHelper.getNode("commands", "changeJob");

    public ChangeJobCommand() {
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

        builder.description(Text.of(configNode.getNode("description").getString("MISSING DESCRIPTION")));

        builder.executor(getExecutor());

        final String permission = configNode.getNode("permission").getString("");

        if(!"".equals(permission)){
            builder.permission(permission);
        }

        CommandElement[] commandElements = new CommandElement[2];

        Map<String, Boolean> actions = new HashMap<>();
        actions.put(configNode.getNode("params", "join", "description").getString("join"), true);
        actions.put(configNode.getNode("params", "leave", "description").getString("leave"), false);

        commandElements[0] =
                GenericArguments.choices(
                        Text.of(configNode.getNode("params", "action", "description").getString("action")),
                        actions
                );

        commandElements[1] =
                GenericArguments.choices(
                        Text.of(configNode.getNode("params", "job", "description").getString("job")),
                        JobsMain.instance().jobs
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
        aliases.add(configNode.getNode("command").getString("changeJob"));

        return aliases;
    }

    /**
     * @param commandSource
     * @param args
     * @see CommandExecutor#execute(CommandSource, CommandContext)
     */
    @Override
    protected CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {
        if(!(commandSource instanceof Player))
            return CommandResult.empty();

        Player player = (Player) args.getOne(
                configNode.getNode("params", "player", "description").getString("player")
        ).orElse(commandSource);

        boolean join = (Boolean)
                args.getOne(configNode.getNode("params", "action", "description").getString("action")).orElse(true);

        Set<String> selectedJobs = player.get(JobKeys.JOB_SELECTED).orElse(new HashSet<>());


        if(join){
            if(selectedJobs.size() > ConfigHelper.getNode("setting", "max_selected_jobs").getInt(1)){
                player.sendMessage(TranslationHelper.p(player, "player.warn.job.too_many_selected_jobs"));

                return CommandResult.empty();
            }

            String jobId =
                    ((Job) args.getOne(configNode.getNode("params", "job", "description").getString("job")).get()).getId();

            if(selectedJobs.add(jobId)){
                player.sendMessage(TranslationHelper.p(player, "player.info.job.select_job", jobId));
                return CommandResult.success();
            }else {
                player.sendMessage(TranslationHelper.p(player, "player.warning.job.cant_select_job.already_selected", jobId));
                return CommandResult.empty();
            }
        }else{
            String jobId =
                    ((Job) args.getOne(configNode.getNode("params", "job", "description").getString("job")).get()).getId();

            if(selectedJobs.remove(jobId)){
                player.sendMessage(TranslationHelper.p(player, "player.info.job.deselect_job", jobId));
            }else{
                player.sendMessage(TranslationHelper.p(player, "player.warning.job.cant_deselect_job.not_selected", jobId));
            }

            return CommandResult.success();
        }

    }
}
