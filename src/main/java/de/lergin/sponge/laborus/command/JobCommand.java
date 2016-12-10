package de.lergin.sponge.laborus.command;

import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;

import java.util.List;

/**
 * this class is creating a sponge command without the need of extra code. So the aliases and the {@link org.spongepowered.api.command.spec.CommandSpec}
 * will be created without extra code.
 */
public abstract class JobCommand implements CommandExecutor {
    /**
     * creates the {@link org.spongepowered.api.command.spec.CommandSpec} for the command
     *
     * @return the {@link org.spongepowered.api.command.spec.CommandSpec}
     */
    public abstract CommandSpec getCommandSpec();

    /**
     * creates the list of possible aliases for the command
     *
     * @return a list of aliases
     */
    public abstract List<String> getCommandAliases();

    static CommandElement getCommandElementWithPermission(CommandElement commandElement, String permission) {
        if (permission.equals("")) {
            return commandElement;
        } else {
            return GenericArguments.requiringPermission(
                    commandElement,
                    permission
            );
        }
    }
}