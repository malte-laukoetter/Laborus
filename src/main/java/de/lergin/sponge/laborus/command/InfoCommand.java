package de.lergin.sponge.laborus.command;

import com.google.common.collect.ImmutableMap;
import de.lergin.sponge.laborus.Laborus;
import de.lergin.sponge.laborus.config.TranslationKeys;
import de.lergin.sponge.laborus.job.Job;
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
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import java.util.*;
import java.util.stream.Collectors;

/**
 * prints infos about the job
 */
@ConfigSerializable
public class InfoCommand extends JobCommand {
    @Setting(value = "command", comment = "The command")
    private String COMMAND = "info";

    @Setting(value = "description", comment = "The description of the command")
    private Text DESCRIPTION = Text.of("Infos about the Job");

    @Setting(value = "permission", comment = "The permission needed to use the command")
    private String PERMISSION = "laborus.commands.info";

    @Setting(value = "paramJobDescription", comment = "")
    private String PARAM_JOB_DESCRIPTION = "Job";

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

        builder.description(this.DESCRIPTION);

        builder.executor(this);

        if (!"".equals(this.PERMISSION)) {
            builder.permission(this.PERMISSION);
        }

        builder.arguments(GenericArguments.optional(
                GenericArguments.choices(
                        Text.of(this.PARAM_JOB_DESCRIPTION),
                        Laborus.instance().getJobs()
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

        if (args.hasAny(this.PARAM_JOB_DESCRIPTION)) {
            Job job = ((Job) args.getOne(this.PARAM_JOB_DESCRIPTION).get());

            player.sendMessage(
                    Laborus.instance().translationHelper.get(
                            TranslationKeys.COMMAND_INFO_SINGLE,
                            player
                    ),
                    job.textArgs(player)
            );
        } else {
            PaginationList.Builder builder = PaginationList.builder().header(
                    Laborus.instance().translationHelper.get(
                            TranslationKeys.COMMAND_INFO_HEADER,
                            player
                    ).apply(ImmutableMap.of()).build()
            );

            builder.contents(Laborus.instance().getJobs().values().stream()
                            .map(job -> Laborus.instance().translationHelper.get(
                                    TranslationKeys.COMMAND_INFO_LINE,
                                    player
                            ).apply(job.textArgs(player)).onClick(
                                    TextActions.runCommand("/" + Laborus.instance().config.base.commandsConfig.command + " " + this.COMMAND + " " + job.getId())
                            ).build())
                            .collect(Collectors.toCollection(ArrayList::new)));

            builder.footer(Laborus.instance().translationHelper.get(
                    TranslationKeys.COMMAND_INFO_FOOTER,
                    player
            ).apply(ImmutableMap.of()).build());

            builder.build().sendTo(player);
        }

        return CommandResult.success();
    }
}
