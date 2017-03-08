package de.lergin.sponge.laborus.command;

import com.google.common.collect.ImmutableMap;
import de.lergin.sponge.laborus.JobsMain;
import de.lergin.sponge.laborus.data.JobKeys;
import de.lergin.sponge.laborus.job.Job;
import de.lergin.sponge.laborus.util.ConfigHelper;
import de.lergin.sponge.laborus.util.TranslationHelper;
import javafx.scene.control.Pagination;
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
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextFormat;
import org.spongepowered.api.text.format.TextStyles;

import java.util.*;
import java.util.stream.Collectors;

import static org.spongepowered.api.text.TextTemplate.arg;

/**
 * prints infos about the job
 */
public class InfoCommand extends JobCommand {
    private final static ConfigurationNode configNode = ConfigHelper.getNode("commands", "info");

    public InfoCommand() {
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

        builder.executor(this);

        final String permission = configNode.getNode("permission").getString("");

        if (!"".equals(permission)) {
            builder.permission(permission);
        }

        builder.arguments(GenericArguments.optional(
                GenericArguments.choices(
                        Text.of(configNode.getNode("params", "job", "description").getString("job")),
                        JobsMain.instance().getJobs()
                )
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
        aliases.add(configNode.getNode("command").getString("info"));

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

        if (args.hasAny(configNode.getNode("params", "job", "description").getString("job"))) {
            Job job =
                    ((Job) args.getOne(configNode.getNode("params", "job", "description").getString("job")).get());

            Map<String, Text> vars = new HashMap<>();

            vars.put("name", Text.of(job.getName()));
            vars.put("xp", Text.of(String.format("%1$.2f", job.getXp(player))));
            vars.put("xpTillNextLevel", Text.of(String.format("%1$.2f", job.getXpTillNextLevel(player))));
            vars.put("level", Text.of(job.getLevel(player)));
            vars.put("description", Text.of(job.getDescription()));
            vars.put("selected", Text.of(job.isSelected(player)));

            player.sendMessage(
                    TranslationHelper.template(
                            TextTemplate.of(
                                    "===================== ", arg("name").color(TextColors.GREEN).style(TextStyles.BOLD).build(),
                                    " =====================", "\n",
                                    arg("description").build(), "\n",
                                    "Current XP: ", arg("xp").color(TextColors.GREEN).build(), "\n",
                                    "Current Level: ", arg("level").color(TextColors.GREEN).build(), "\n",
                                    "XP till next Level: ", arg("xpTillNextLevel").color(TextColors.GREEN).build(), "\n",
                                    "Selected: ", arg("selected").color(TextColors.GREEN).build(), "\n"

                            ),
                            player.getLocale().toLanguageTag(),
                            "job_info_job"
                    ),
                    ImmutableMap.copyOf(vars)
            );
        } else {
            PaginationList.Builder builder = PaginationList.builder().header(
                    TranslationHelper.template(
                            TextTemplate.of(
                                    "======================= ",
                                    Text.builder("Jobs").style(TextStyles.BOLD).color(TextColors.GREEN).build(),
                                    " ========================"
                            ),
                            player.getLocale().toLanguageTag(),
                            "job_info_pre"
                    ).toText()
            );

            builder.contents(JobsMain.instance().getJobs().values().stream()
                            .map(job -> TranslationHelper.template(
                                     TextTemplate.of(
                                            TextActions.runCommand("/jobs info " + job.getId()),
                                            arg("name").color(TextColors.GREEN).style(TextStyles.BOLD).build(),
                                            "  Level: ", arg("level").color(TextColors.GREEN).build(), "  Xp: ", arg("xp").color(TextColors.GREEN), " / ", arg("xpForNextLevel").color(TextColors.GREEN),
                                            "  Selected: ", arg("selected").color(TextColors.GREEN).build()
                                    ),
                                    player.getLocale().toLanguageTag(),
                                    "job_info_jobitem"
                                ).apply(ImmutableMap.of(
                                        "name", Text.of(job.getName()),
                                        "level", Text.of(job.getLevel(player)),
                                        "xp", Text.of(String.format("%1$.2f", job.getXp(player))),
                                        "xpForNextLevel", Text.of(String.format("%1$.2f", job.getXp(player) + job.getXpTillNextLevel(player))),
                                        "selected", Text.of(job.isSelected(player))
                                )).toText())
                            .collect(Collectors.toCollection(ArrayList::new)));

            builder.footer(TranslationHelper.template(
                    TextTemplate.of(
                            ""
                    ),
                    player.getLocale().toLanguageTag(),
                    "job_info_post"
            ).toText());

            builder.build().sendTo(player);
        }

        return CommandResult.success();
    }
}
