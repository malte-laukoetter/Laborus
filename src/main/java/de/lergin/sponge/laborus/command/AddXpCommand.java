package de.lergin.sponge.laborus.command;

import de.lergin.sponge.laborus.Laborus;
import de.lergin.sponge.laborus.config.TranslationKeys;
import de.lergin.sponge.laborus.data.JobKeys;
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
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * adds some xp to the {@link Player}
 */
@ConfigSerializable
public class AddXpCommand extends JobCommand {
    @Setting(value = "command", comment = "The description of the command")
    private String COMMAND = "addJobXp";

    @Setting(value = "description", comment = "The description of the command")
    private Text DESCRIPTION = Text.of("adds some xp to the job");

    @Setting(value = "permission", comment = "The permission needed to use the command")
    private String PERMISSION = "laborus.commands.addXp";

    @Setting(value = "paramJobDescription", comment = "")
    private String PARAM_JOB_DESCRIPTION = "job";

    @Setting(value = "paramXpDescription", comment = "")
    private String PARAM_XP_DESCRIPTION = "xp";

    @Setting(value = "paramPlayerDescription", comment = "")
    private String PARAM_PLAYER_DESCRIPTION = "player";

    @Setting(value = "paramPlayerPermission", comment = "")
    private String PARAM_PLAYER_PERMISSION = "laborus.commands.addXp.outher_player";

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

        builder.description(this.DESCRIPTION);

        builder.executor(this);

        if (!"".equals(this.PERMISSION)) {
            builder.permission(this.PERMISSION);
        }

        builder.arguments(
                GenericArguments.choices(
                        Text.of(this.PARAM_JOB_DESCRIPTION),
                        Laborus.instance().getJobs()
                ),
                GenericArguments.doubleNum(
                        Text.of(this.PARAM_XP_DESCRIPTION)
                ),
                GenericArguments.optional(
                        getCommandElementWithPermission(
                                GenericArguments.player(Text.of(this.PARAM_PLAYER_DESCRIPTION)),
                                this.PARAM_PLAYER_PERMISSION
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
        if (!(commandSource instanceof Player || args.hasAny(this.PARAM_PLAYER_DESCRIPTION)))
            throw new CommandException(Text.of("Only Players can use this command without a player parameter", true));

        Player player = (Player) args.getOne(this.PARAM_PLAYER_DESCRIPTION).orElse(commandSource);

        Map<String, Double> jobData = player.get(JobKeys.JOB_DATA).orElseGet(HashMap::new);

        Job job = ((Job) args.getOne(this.PARAM_JOB_DESCRIPTION).get());
        double addXp = (double) args.getOne(this.PARAM_XP_DESCRIPTION).get();
        double newXp = jobData.getOrDefault(job.getId(), 0.0) + addXp;


        jobData.put(job.getId(), newXp);

        player.offer(JobKeys.JOB_DATA, jobData);

        if (!(commandSource instanceof Player) || !commandSource.equals(player)) {
            commandSource.sendMessage(
                    Laborus.instance().translationHelper.get(
                            TranslationKeys.COMMAND_ADDXP_SEND_OTHER,
                            commandSource
                    ),
                    this.textArgs(job, player, commandSource, addXp)
            );

            player.sendMessage(
                    Laborus.instance().translationHelper.get(
                            TranslationKeys.COMMAND_ADDXP_RECEIVE_OTHER,
                            player
                    ),
                    this.textArgs(job,player,commandSource,addXp)
            );
        } else {
            commandSource.sendMessage(
                    Laborus.instance().translationHelper.get(
                            TranslationKeys.COMMAND_ADDXP_SELF,
                            commandSource
                    ),
                    this.textArgs(job,player,commandSource,addXp)
            );
        }

        return CommandResult.success();
    }

    public Map<String, TextElement> textArgs(Job job, Player player, CommandSource source, double xp){
        Map<String, TextElement> args = job.textArgs(player);

        args.put("xp", Text.of(xp));
        args.put("player.name", Text.of(player.getName()));
        args.put("player.display_name", player.getDisplayNameData().displayName().get());
        args.put("source", Text.of(source.getName()));
        args.put("xp", Text.of(String.format("%1$f.2", xp)));

        return args;
    }
}
