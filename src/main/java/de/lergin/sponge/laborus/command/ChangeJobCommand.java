package de.lergin.sponge.laborus.command;

import com.google.common.collect.ImmutableMap;
import de.lergin.sponge.laborus.JobsMain;
import de.lergin.sponge.laborus.data.JobKeys;
import de.lergin.sponge.laborus.data.jobs.JobDataManipulatorBuilder;
import de.lergin.sponge.laborus.job.Job;
import de.lergin.sponge.laborus.util.ConfigHelper;
import de.lergin.sponge.laborus.util.TranslationHelper;
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
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.*;

import static org.spongepowered.api.text.TextTemplate.arg;

/**
 * changes the selected {@link Job}s
 */
public class ChangeJobCommand extends JobCommand {
    private final static ConfigurationNode configNode = ConfigHelper.getNode("commands", "change");

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

        builder.description(Text.of(configNode.getNode("description").getString("MISSING DESCRIPTION")));

        builder.executor(getExecutor());

        final String permission = configNode.getNode("permission").getString("");

        if (!"".equals(permission)) {
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
                        JobsMain.instance().getJobs()
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
    public List<String> getCommandAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add(configNode.getNode("command").getString("change"));

        return aliases;
    }

    /**
     * @param commandSource
     * @param args
     * @see CommandExecutor#execute(CommandSource, CommandContext)
     */
    @Override
    protected CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {
        if (!(commandSource instanceof Player))
            return CommandResult.empty();

        Player player = (Player) args.getOne(
                configNode.getNode("params", "player", "description").getString("player")
        ).orElse(commandSource);

        boolean join = (Boolean)
                args.getOne(configNode.getNode("params", "action", "description").getString("action")).orElse(true);

        Set<String> selectedJobs = player.get(JobKeys.JOB_SELECTED).orElse(new HashSet<>());

        Job job =
                ((Job) args.getOne(configNode.getNode("params", "job", "description").getString("job")).get());


        if (join) {
            if (!selectedJobs.contains(job.getId())) {
                final int maxJobs = ConfigHelper.getNode("setting", "max_selected_jobs").getInt(1);

                if (selectedJobs.size() > maxJobs) {
                    player.sendMessage(
                            TranslationHelper.template(
                                    TextTemplate.of(
                                            TextColors.AQUA,
                                            "You cannot join ", arg("jobName").color(TextColors.GREEN).build(),
                                            ", due to the reach of the max. of jobs you can select (",
                                            arg("maxJobs").color(TextColors.GREEN).build(),
                                            "). If you want to join another job you first need to ",
                                            Text.builder("leave")
                                                    .onClick(TextActions.suggestCommand("/jobs change leave "))
                                                    .onHover(TextActions.showText(Text.of("/jobs change leave ")))
                                                    .style(TextStyles.UNDERLINE).build(),
                                            " another one."
                                    ),
                                    player.getLocale().toLanguageTag(),
                                    "job_join_too_many_jobs"
                            ),
                            ImmutableMap.of(
                                    "jobName", Text.of(job.getName()),
                                    "maxJobs", Text.of(maxJobs)
                            )
                    );

                    return CommandResult.empty();
                }

                selectedJobs.add(job.getId());

                if (!player.supports(JobKeys.JOB_SELECTED)) {
                    player.offer(new JobDataManipulatorBuilder().selectedJobs(selectedJobs).create());
                }

                player.sendMessage(
                        TranslationHelper.template(
                                TextTemplate.of(
                                        TextColors.AQUA,
                                        "You have joined ", arg("jobName").color(TextColors.GREEN).build(), "."
                                ),
                                player.getLocale().toLanguageTag(),
                                "job_join_success"
                        ),
                        ImmutableMap.of(
                                "jobName", Text.of(job.getName())
                        )
                );

                return CommandResult.success();
            } else {
                player.sendMessage(
                        TranslationHelper.template(
                                TextTemplate.of(
                                        TextColors.AQUA,
                                        "You already joined ", arg("jobName").color(TextColors.GREEN).build(), "."
                                ),
                                player.getLocale().toLanguageTag(),
                                "job_join_already_selected"
                        ),
                        ImmutableMap.of(
                                "jobName", Text.of(job.getName())
                        )
                );

                return CommandResult.empty();
            }
        } else {
            String jobId =
                    ((Job) args.getOne(configNode.getNode("params", "job", "description").getString("job")).get()).getId();

            if (selectedJobs.remove(jobId)) {
                player.sendMessage(
                        TranslationHelper.template(
                                TextTemplate.of(
                                        TextColors.AQUA,
                                        "You have leaved ", arg("jobName").color(TextColors.GREEN).build(), "."
                                ),
                                player.getLocale().toLanguageTag(),
                                "job_leave_success"
                        ),
                        ImmutableMap.of(
                                "jobName", Text.of(job.getName())
                        )
                );
            } else {
                player.sendMessage(
                        TranslationHelper.template(
                                TextTemplate.of(
                                        TextColors.AQUA,
                                        "You don't had ", arg("jobName").color(TextColors.GREEN).build(), " selected."
                                ),
                                player.getLocale().toLanguageTag(),
                                "job_leave_not_selected"
                        ),
                        ImmutableMap.of(
                                "jobName", Text.of(job.getName())
                        )
                );
            }

            return CommandResult.success();
        }

    }
}
