package de.lergin.sponge.laborus.command;

import com.google.common.collect.ImmutableMap;
import de.lergin.sponge.laborus.JobsMain;
import de.lergin.sponge.laborus.data.JobKeys;
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
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.spongepowered.api.text.TextTemplate.arg;

/**
 * adds some xp to the {@link Player}
 */
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
    public CommandSpec getCommandSpec() {
        CommandSpec.Builder builder = CommandSpec.builder();

        builder.description(Text.of(configNode.getNode("description").getString()));

        builder.executor(this);

        final String permission = configNode.getNode("permission").getString();

        if (!"".equals(permission)) {
            builder.permission(permission);
        }

        builder.arguments(
                GenericArguments.choices(
                        Text.of(configNode.getNode("params", "job", "description").getString("job")),
                        JobsMain.instance().getJobs()
                ),
                GenericArguments.doubleNum(
                        Text.of(configNode.getNode("params", "xp", "description").getString("xp"))
                ),
                GenericArguments.optional(
                        getCommandElementWithPermission(
                                GenericArguments.player(Text.of(
                                        configNode.getNode("params", "player", "description").getString("player")
                                )),
                                configNode.getNode("params", "player", "permission").getString("")
                        )
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
        aliases.add(configNode.getNode("command").getString("addJobXp"));

        return aliases;
    }

    /**
     * @param commandSource
     * @param args
     * @see CommandExecutor#execute(CommandSource, CommandContext)
     */
    @Override
    public CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {
        if (!(commandSource instanceof Player ||
                args.hasAny(configNode.getNode("params", "player", "description").getString("player"))))
            throw new CommandException(Text.of("Only Players can use this command without a player parameter", true));

        Player player = (Player) args.getOne(
                configNode.getNode("params", "player", "description").getString("player")
        ).orElse(commandSource);

        Map<String, Double> jobData = player.get(JobKeys.JOB_DATA).orElse(new HashMap<>());

        Job job =
                ((Job) args.getOne(configNode.getNode("params", "job", "description").getString("job")).get());
        double addXp = (double) args.getOne(configNode.getNode("params", "xp", "description").getString("xp")).get();
        double newXp = jobData.getOrDefault(job.getId(), 0.0) + addXp;


        jobData.put(job.getId(), newXp);

        player.offer(JobKeys.JOB_DATA, jobData);

        if (!(commandSource instanceof Player) || !commandSource.equals(player)) {
            commandSource.sendMessage(
                    TranslationHelper.template(
                            TextTemplate.of(
                                    TextColors.AQUA, "You added ",
                                    arg("addXp").color(TextColors.GREEN).build(),
                                    Text.of(TextColors.GREEN, "xp"),
                                    " to ", arg("playerName").color(TextColors.GREEN).build(),
                                    " to the job ", arg("jobName").color(TextColors.GREEN).build(),
                                    "."
                            ),
                            commandSource.getLocale().toLanguageTag(),
                            "add_xp_other"
                    ),
                    ImmutableMap.of(
                            "jobName", Text.of(job.getName()),
                            "addXp", Text.of(addXp),
                            "playerName", Text.of(player.getName())
                    )
            );

            player.sendMessage(
                    TranslationHelper.template(
                            TextTemplate.of(
                                    TextColors.AQUA,
                                    arg("commandSource").color(TextColors.GREEN).build(),
                                    " added ", arg("addXp").color(TextColors.GREEN).build(),
                                    Text.of(TextColors.GREEN, "xp"),
                                    " to your job ", arg("jobName").color(TextColors.GREEN).build(),
                                    "."
                            ),
                            player.getLocale().toLanguageTag(),
                            "add_xp_other_get"
                    ),
                    ImmutableMap.of(
                            "jobName", Text.of(job.getName()),
                            "addXp", Text.of(addXp),
                            "commandSource", Text.of(commandSource.getName())
                    )
            );
        } else {
            commandSource.sendMessage(
                    TranslationHelper.template(
                            TextTemplate.of(
                                    TextColors.AQUA, "You added ",
                                    arg("addXp").color(TextColors.GREEN).build(),
                                    Text.of(TextColors.GREEN, "xp"),
                                    " to ", arg("jobName").color(TextColors.GREEN).build(),
                                    "."
                            ),
                            commandSource.getLocale().toLanguageTag(),
                            "add_xp_self"
                    ),
                    ImmutableMap.of(
                            "jobName", Text.of(job.getName()),
                            "addXp", Text.of(addXp)
                    )
            );
        }


        return CommandResult.success();
    }
}
