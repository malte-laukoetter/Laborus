package de.lergin.laborus.command;

import de.lergin.laborus.Laborus;
import de.lergin.laborus.data.jobs.JobDataManipulatorBuilder;
import de.lergin.laborus.job.Job;
import de.lergin.laborus.config.TranslationKeys;
import de.lergin.laborus.data.JobKeys;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextElement;

import java.util.*;

/**
 * changes the selected {@link Job}s
 */
@ConfigSerializable
public class ChangeJobCommand extends JobCommand {
    @Setting(value = "command", comment = "command")
    private String COMMAND = "change";

    @Setting(value = "description", comment = "description of the command")
    private Text DESCRIPTION = Text.of("Changes the Job");

    @Setting(value = "permission", comment = "permission needed to use the command")
    private String PERMISSION = "laborus.commands.change";

    @Setting(value = "paramJobDescription", comment = "description of the job parameter")
    private String PARAM_JOB_DESCRIPTION = "Job";

    @Setting(value = "paramJoinDescription", comment = "description of the join parameter")
    private String PARAM_JOIN_DESCRIPTION = "Join";

    @Setting(value = "paramLeaveDescription", comment = "description of the leave parameter")
    private String PARAM_LEAVE_DESCRIPTION = "Leave";

    @Setting(value = "paramActionDescription", comment = "description of the action parameter")
    private String PARAM_ACTION_DESCRIPTION = "Action";

    public ChangeJobCommand() {
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

        builder.description(this.DESCRIPTION);

        builder.executor(this);

        if (!"".equals(this.PERMISSION)) {
            builder.permission(this.PERMISSION);
        }

        Map<String, Boolean> actions = new HashMap<>();
        actions.put(this.PARAM_JOIN_DESCRIPTION, true);
        actions.put(this.PARAM_LEAVE_DESCRIPTION, false);

        builder.arguments(
                GenericArguments.choices(
                        Text.of(this.PARAM_ACTION_DESCRIPTION),
                        actions
                ),
                GenericArguments.choices(
                        Text.of(this.PARAM_JOB_DESCRIPTION),
                        Laborus.instance().getJobs()
                )
        );

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
        aliases.add(this.COMMAND);

        return aliases;
    }

    /**
     * @param commandSource
     * @param args
     * @see CommandExecutor#execute(CommandSource, CommandContext)
     */
    @Override
    public CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {
        if (!(commandSource instanceof Player))
            throw new CommandException(Text.of("Only Players can use this command", false));

        Player player = (Player) commandSource;

        boolean join = (Boolean) args.getOne(this.PARAM_ACTION_DESCRIPTION).orElse(true);

        Set<String> selectedJobs = player.get(JobKeys.JOB_SELECTED).orElseGet(HashSet::new);

        Job job = ((Job) args.getOne(this.PARAM_JOB_DESCRIPTION).get());


        if (join) {
            if (!selectedJobs.contains(job.getId())) {
                final int maxJobs = Laborus.instance().config.base.maxSelectedJobs;

                if (selectedJobs.size() > maxJobs) {
                    Map<String, TextElement> textArgs = job.textArgs(player);

                    textArgs.put("maxjobs", Text.of(maxJobs));

                    player.sendMessage(
                            Laborus.instance().translationHelper.get(
                                    TranslationKeys.COMMAND_CHANGE_TOO_MANY_SELECTED,
                                    player
                            ),
                            textArgs
                    );

                    return CommandResult.empty();
                }else if(!job.hasPermission(player)){
                    player.sendMessage(
                            Laborus.instance().translationHelper.get(
                                    TranslationKeys.COMMAND_CHANGE_MISSING_JOB_PERMISSION,
                                    player
                            ),
                            job.textArgs(player)
                    );

                    return CommandResult.empty();
                }

                selectedJobs.add(job.getId());

                if (!player.supports(JobKeys.JOB_SELECTED)) {
                    player.offer(new JobDataManipulatorBuilder().selectedJobs(selectedJobs).create());
                }

                player.sendMessage(
                        Laborus.instance().translationHelper.get(
                                TranslationKeys.COMMAND_CHANGE_JOINED,
                                player
                        ),
                        job.textArgs(player)
                );

                return CommandResult.success();
            } else {
                player.sendMessage(
                        Laborus.instance().translationHelper.get(
                                TranslationKeys.COMMAND_CHANGE_ALREADY_JOINED,
                                player
                        ),
                        job.textArgs(player)
                );

                return CommandResult.empty();
            }
        } else {
            if (selectedJobs.remove(job.getId())) {
                player.sendMessage(
                        Laborus.instance().translationHelper.get(
                                TranslationKeys.COMMAND_CHANGE_LEAVED,
                                player
                        ),
                        job.textArgs(player)
                );
            } else {
                player.sendMessage(
                        Laborus.instance().translationHelper.get(
                                TranslationKeys.COMMAND_CHANGE_NOT_SELECTED,
                                player
                        ),
                        job.textArgs(player)
                );
            }

            return CommandResult.success();
        }

    }
}
