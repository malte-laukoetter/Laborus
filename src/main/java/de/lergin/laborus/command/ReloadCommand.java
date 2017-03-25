package de.lergin.laborus.command;

import de.lergin.laborus.Laborus;
import de.lergin.laborus.config.TranslationKeys;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ConfigSerializable
public class ReloadCommand extends JobCommand {
    @Setting(value = "command", comment = "command")
    private String COMMAND = "reload";

    @Setting(value = "description", comment = "description of the command")
    private Text DESCRIPTION = Text.of("Reloads the configuration of the plugin");

    @Setting(value = "permission", comment = "permission needed to use the command")
    private String PERMISSION = "laborus.commands.reload";

    public ReloadCommand(){super();}

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
    public CommandResult execute(CommandSource src, CommandContext commandContext) throws CommandException {
        src.sendMessage(Laborus.instance().translationHelper.get(TranslationKeys.COMMAND_RELOAD_START, src));

        try {
            Laborus.instance().config.reload();
            src.sendMessage(Laborus.instance().translationHelper.get(TranslationKeys.COMMAND_RELOAD_SUCCESS, src));
        } catch (IOException | ObjectMappingException e) {
            src.sendMessage(Laborus.instance().translationHelper.get(TranslationKeys.COMMAND_RELOAD_ERROR, src));
            e.printStackTrace();
        }
        return CommandResult.success();
    }
}
