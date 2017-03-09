package de.lergin.sponge.laborus.command;

import de.lergin.sponge.laborus.Laborus;
import de.lergin.sponge.laborus.job.Job;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
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

@ConfigSerializable
public class AbilityStartCommand extends JobCommand {
    @Setting(value = "command", comment = "The description of the command")
    private String COMMAND = "ability";

    @Setting(value = "description", comment = "The description of the command")
    private Text DESCRIPTION = Text.of("Activates the JobAbility");

    @Setting(value = "permission", comment = "The permission needed to use the command")
    private String PERMISSION = "laborus.commands.ability";

    @Setting(value = "paramJobDescription", comment = "The permission needed to use the command")
    private Text PARAM_JOB_DESCRIPTION = Text.of("job");


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

        builder.description(this.DESCRIPTION);

        builder.executor(this);

        if (!"".equals(this.PERMISSION)) {
            builder.permission(this.PERMISSION);
        }

        //only add jobs that have a ability
        Map<String, Job> jobs = new HashMap<>();
        Laborus.instance().getJobs().values().stream()
                .filter(Job::hasJobAbility)
                .forEach(job -> jobs.put(job.getId(), job));

        builder.arguments(GenericArguments.choices(this.PARAM_JOB_DESCRIPTION, jobs));

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

    @Override
    public CommandResult execute(CommandSource commandSource, CommandContext args) throws CommandException {
        if (!(commandSource instanceof Player))
            throw new CommandException(Text.of("Only Players can use this command", false));

        final Player player = (Player) commandSource;

        final Job job =
                (Job) args.getOne(this.PARAM_JOB_DESCRIPTION).get();

        job.getJobAbility().startAbility(job, player);

        return CommandResult.success();
    }
}
