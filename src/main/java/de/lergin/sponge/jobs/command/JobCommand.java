package de.lergin.sponge.jobs.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;

import java.util.List;

/**
 * this class is creating a sponge command without the need of extra code. So the aliases and the {@link org.spongepowered.api.command.spec.CommandSpec}
 * will be created without extra code.
 */
public abstract class JobCommand {
    /**
     * creates the {@link org.spongepowered.api.command.spec.CommandSpec} for the command
     * @return the {@link org.spongepowered.api.command.spec.CommandSpec}
     */
    public abstract CommandSpec getCommandSpec();

    /**
     * creates the list of possible aliases for the command
     * @return a list of aliases
     */
    public abstract List<String> getCommandAliases();

    /**
     * @see org.spongepowered.api.command.spec.CommandExecutor#execute(CommandSource, CommandContext)
     */
    protected abstract CommandResult execute(CommandSource commandSource, CommandContext commandContext) throws CommandException;

    /**
     * creates a new JobCommandExecutor so the class can be private
     * @return a new CommandExecutor
     */
    public CommandExecutor getExecutor(){
        return new JobCommandExecutor();
    }

    /**
     * class for sending the function {@link org.spongepowered.api.command.spec.CommandExecutor#execute(CommandSource, CommandContext)}
     * to the {@link #execute(CommandSource, CommandContext)} function.
     */
    private class JobCommandExecutor implements CommandExecutor {
        @Override
        public CommandResult execute(CommandSource commandSource, CommandContext commandContext) throws CommandException {
            return JobCommand.this.execute(commandSource, commandContext);
        }
    }

    static CommandElement getCommandElementWithPermission(CommandElement commandElement, String permission){
        if(permission.equals("")){
            return commandElement;
        }else{
            return GenericArguments.requiringPermission(
                    commandElement,
                    permission
            );
        }
    }
}