package de.lergin.sponge.laborus.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import static org.spongepowered.api.text.TextTemplate.arg;

@ConfigSerializable
public class TranslationConfig {
    @Setting(value = "JOB_XP_ACTION_BAR", comment = "The Text shown in the action bar after getting xp")
    private TextTemplate JOB_XP_ACTION_BAR = TextTemplate.of(
            arg("job.name").build(),
            ": ",
            arg("job.xp").build()
    );

    @Setting(value = "JOB_LEVEL_UP", comment = "The Text shown when a player gets a level up")
    private TextTemplate JOB_LEVEL_UP = TextTemplate.of(
            "You have reached level ",
            arg("job.level").color(TextColors.GREEN).build(),
            " in the job ",
            arg("job.name").color(TextColors.GREEN).build()
    );

    @Setting(value = "JOB_LEVEL_NOT_HIGH_ENOUGH", comment = "The Text shown when a player has a to small level to do something")
    private TextTemplate JOB_LEVEL_NOT_HIGH_ENOUGH = TextTemplate.of(
            "Your level is too low to do this."
    );

    @Setting(value = "JOB_ABILITY_CANNOT_START_COOLDOWN", comment = "")
    private TextTemplate JOB_ABILITY_CANNOT_START_COOLDOWN = TextTemplate.of(
            "You used the ability ", arg("ability.name").color(TextColors.GREEN).build(),
            " from ",
            arg("job.name").color(TextColors.GREEN).build(),
            " recently so you need to wait ",
            arg("ability.remaining_cooldown").color(TextColors.GREEN), "s until you can use it again."
    );

    @Setting(value = "JOB_ABILITY_START", comment = "")
    private TextTemplate JOB_ABILITY_START = TextTemplate.of(
            "You started the ability ",
            arg("ability.name").color(TextColors.GREEN).build(),
            " from ", arg("job.name").color(TextColors.GREEN).build(),
            ". You can use it again in ",
            arg("ability.remaining_cooldown").color(TextColors.GREEN), "s."
    );

    @Setting(value = "COMMAND_ADDXP_SEND_OTHER", comment = "")
    private TextTemplate COMMAND_ADDXP_SEND_OTHER = TextTemplate.of(
            arg("xp").color(TextColors.GREEN).build(),
            Text.of(TextColors.GREEN, "xp"),
            " to ", arg("player.display_name").build(),
            " to the job ", arg("job.name").color(TextColors.GREEN).build(),
            "."
    );

    @Setting(value = "COMMAND_ADDXP_RECEIVE_OTHER", comment = "")
    private TextTemplate COMMAND_ADDXP_RECEIVE_OTHER = TextTemplate.of(
            arg("source").color(TextColors.GREEN).build(),
            " added ", arg("xp").color(TextColors.GREEN).build(),
            Text.of(TextColors.GREEN, "xp"),
            " to your job ", arg("job.name").color(TextColors.GREEN).build(),
            "."
    );

    @Setting(value = "COMMAND_ADDXP_SELF", comment = "")
    private TextTemplate COMMAND_ADDXP_SELF = TextTemplate.of(
            "You added ",
            arg("xp").color(TextColors.GREEN).build(),
            Text.of(TextColors.GREEN, "xp"),
            " to ", arg("job.name").color(TextColors.GREEN).build(),
            "."
    );

    @Setting(value = "COMMAND_CHANGE_JOINED", comment = "")
    private TextTemplate COMMAND_CHANGE_JOINED = TextTemplate.of(
            "You have joined ", arg("job.name").color(TextColors.GREEN).build(), "."
    );

    @Setting(value = "COMMAND_CHANGE_ALREADY_JOINED", comment = "")
    private TextTemplate COMMAND_CHANGE_ALREADY_JOINED = TextTemplate.of(
            "You already joined ", arg("job.name").color(TextColors.GREEN).build(), "."
    );

    @Setting(value = "COMMAND_CHANGE_LEAVED", comment = "")
    private TextTemplate COMMAND_CHANGE_LEAVED = TextTemplate.of(
            "You have leaved ", arg("job.name").color(TextColors.GREEN).build(), "."
    );

    @Setting(value = "COMMAND_CHANGE_NOT_SELECTED", comment = "")
    private TextTemplate COMMAND_CHANGE_NOT_SELECTED = TextTemplate.of(
            "You don't had ", arg("job.name").color(TextColors.GREEN).build(), " selected."
    );

    @Setting(value = "COMMAND_CHANGE_MISSING_JOB_PERMISSION", comment = "")
    private TextTemplate COMMAND_CHANGE_MISSING_JOB_PERMISSION = TextTemplate.of(
            "You cannot join ", arg("job.name").color(TextColors.GREEN).build(),
            " (missing permission)"
    );

    @Setting(value = "COMMAND_CHANGE_TOO_MANY_SELECTED", comment = "")
    private TextTemplate COMMAND_CHANGE_TOO_MANY_SELECTED = TextTemplate.of(
            "You cannot join ", arg("job.name").color(TextColors.GREEN).build(),
            ", due to the reach of the max. of jobs you can select (",
            arg("maxjobs").color(TextColors.GREEN).build(),
            "). If you want to join another job you first need to ",
            Text.builder("leave")
                    .onClick(TextActions.suggestCommand("/jobs change leave "))
                    .onHover(TextActions.showText(Text.of("/jobs change leave ")))
                    .style(TextStyles.UNDERLINE).build(),
            " another one."
    );

    @Setting(value = "COMMAND_TOGGLE_ACTIVATED", comment = "")
    private TextTemplate COMMAND_TOGGLE_ACTIVATED = TextTemplate.of("Enabled JobSystem!");

    @Setting(value = "COMMAND_TOGGLE_DEACTIVATED", comment = "")
    private TextTemplate COMMAND_TOGGLE_DEACTIVATED = TextTemplate.of("Disabled JobSystem!");

    @Setting(value = "COMMAND_INFO_SINGLE", comment = "")
    private TextTemplate COMMAND_INFO_SINGLE = TextTemplate.of(
            "==================== ", arg("job.name").color(TextColors.GREEN).style(TextStyles.BOLD).build(),
            " ====================", "\n",
            arg("job.description").build(), "\n",
            "Current XP: ", arg("job.xp").color(TextColors.GREEN).build(), "\n",
            "Current Level: ", arg("job.level").color(TextColors.GREEN).build(), "\n",
            "XP till next Level: ", arg("job.xp_till_next_level").color(TextColors.GREEN).build(), "\n",
            "Selected: ", arg("job.selected").color(TextColors.GREEN).build(), "\n"

    );

    @Setting(value = "COMMAND_INFO_HEADER", comment = "")
    private TextTemplate COMMAND_INFO_HEADER = TextTemplate.of(
            "======================= ",
            Text.builder("Jobs").style(TextStyles.BOLD).color(TextColors.GREEN).build(),
            " ========================"
    );


    @Setting(value = "COMMAND_INFO_LINE", comment = "")
    private TextTemplate COMMAND_INFO_LINE = TextTemplate.of(
            arg("job.name").color(TextColors.GREEN).style(TextStyles.BOLD).build(),
            "  Level: ", arg("job.level").color(TextColors.GREEN).build(), "  Xp: ",
            arg("job.xp").color(TextColors.GREEN), " / ", arg("job.xp_for_next_level").color(TextColors.GREEN),
            "  Selected: ", arg("job.selected").color(TextColors.GREEN).build()
    );

    @Setting(value = "COMMAND_INFO_FOOTER", comment = "")
    private TextTemplate COMMAND_INFO_FOOTER = TextTemplate.of("");

    public TranslationConfig(){}

    public TextTemplate get(TranslationKeys key){
        switch (key){
            case JOB_XP_ACTION_BAR:                     return JOB_XP_ACTION_BAR;
            case JOB_LEVEL_UP:                          return JOB_LEVEL_UP;
            case JOB_LEVEL_NOT_HIGH_ENOUGH:             return JOB_LEVEL_NOT_HIGH_ENOUGH;

            case JOB_ABILITY_CANNOT_START_COOLDOWN:     return JOB_ABILITY_CANNOT_START_COOLDOWN;
            case JOB_ABILITY_START:                     return JOB_ABILITY_START;

            case COMMAND_ADDXP_SEND_OTHER:              return COMMAND_ADDXP_SEND_OTHER;
            case COMMAND_ADDXP_RECEIVE_OTHER:           return COMMAND_ADDXP_RECEIVE_OTHER;
            case COMMAND_ADDXP_SELF:                    return COMMAND_ADDXP_SELF;

            case COMMAND_CHANGE_JOINED:                 return COMMAND_CHANGE_JOINED;
            case COMMAND_CHANGE_ALREADY_JOINED:         return COMMAND_CHANGE_ALREADY_JOINED;
            case COMMAND_CHANGE_LEAVED:                 return COMMAND_CHANGE_LEAVED;
            case COMMAND_CHANGE_NOT_SELECTED:           return COMMAND_CHANGE_NOT_SELECTED;
            case COMMAND_CHANGE_MISSING_JOB_PERMISSION: return COMMAND_CHANGE_MISSING_JOB_PERMISSION;
            case COMMAND_CHANGE_TOO_MANY_SELECTED:      return COMMAND_CHANGE_TOO_MANY_SELECTED;

            case COMMAND_INFO_SINGLE:                   return COMMAND_INFO_SINGLE;
            case COMMAND_INFO_HEADER:                   return COMMAND_INFO_HEADER;
            case COMMAND_INFO_LINE:                     return COMMAND_INFO_LINE;
            case COMMAND_INFO_FOOTER:                   return COMMAND_INFO_FOOTER;

            case COMMAND_TOGGLE_ACTIVATED:              return COMMAND_TOGGLE_ACTIVATED;
            case COMMAND_TOGGLE_DEACTIVATED:            return COMMAND_TOGGLE_DEACTIVATED;
        }

        return TextTemplate.of();
    }
}
