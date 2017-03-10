package de.lergin.sponge.laborus.config;

import org.spongepowered.api.text.TextTemplate;

public enum TranslationKeys {
    JOB_XP_ACTION_BAR,
    JOB_LEVEL_UP,
    JOB_LEVEL_NOT_HIGH_ENOUGH,

    JOB_ABILITY_CANNOT_START_COOLDOWN,
    JOB_ABILITY_START,

    COMMAND_ADDXP_SEND_OTHER,
    COMMAND_ADDXP_RECEIVE_OTHER,
    COMMAND_ADDXP_SELF,

    COMMAND_CHANGE_JOINED,
    COMMAND_CHANGE_ALREADY_JOINED,
    COMMAND_CHANGE_LEAVED,
    COMMAND_CHANGE_NOT_SELECTED,
    COMMAND_CHANGE_MISSING_JOB_PERMISSION,
    COMMAND_CHANGE_TOO_MANY_SELECTED,

    COMMAND_INFO_SINGLE,
    COMMAND_INFO_HEADER,
    COMMAND_INFO_LINE,
    COMMAND_INFO_FOOTER,

    COMMAND_RELOAD_START,
    COMMAND_RELOAD_SUCCESS,
    COMMAND_RELOAD_ERROR,

    COMMAND_TOGGLE_ACTIVATED,
    COMMAND_TOGGLE_DEACTIVATED;

    TextTemplate get(TranslationConfig config){
        switch (this){
            case JOB_XP_ACTION_BAR:                     return config.JOB_XP_ACTION_BAR;
            case JOB_LEVEL_UP:                          return config.JOB_LEVEL_UP;
            case JOB_LEVEL_NOT_HIGH_ENOUGH:             return config.JOB_LEVEL_NOT_HIGH_ENOUGH;

            case JOB_ABILITY_CANNOT_START_COOLDOWN:     return config.JOB_ABILITY_CANNOT_START_COOLDOWN;
            case JOB_ABILITY_START:                     return config.JOB_ABILITY_START;

            case COMMAND_ADDXP_SEND_OTHER:              return config.COMMAND_ADDXP_SEND_OTHER;
            case COMMAND_ADDXP_RECEIVE_OTHER:           return config.COMMAND_ADDXP_RECEIVE_OTHER;
            case COMMAND_ADDXP_SELF:                    return config.COMMAND_ADDXP_SELF;

            case COMMAND_CHANGE_JOINED:                 return config.COMMAND_CHANGE_JOINED;
            case COMMAND_CHANGE_ALREADY_JOINED:         return config.COMMAND_CHANGE_ALREADY_JOINED;
            case COMMAND_CHANGE_LEAVED:                 return config.COMMAND_CHANGE_LEAVED;
            case COMMAND_CHANGE_NOT_SELECTED:           return config.COMMAND_CHANGE_NOT_SELECTED;
            case COMMAND_CHANGE_MISSING_JOB_PERMISSION: return config.COMMAND_CHANGE_MISSING_JOB_PERMISSION;
            case COMMAND_CHANGE_TOO_MANY_SELECTED:      return config.COMMAND_CHANGE_TOO_MANY_SELECTED;

            case COMMAND_INFO_SINGLE:                   return config.COMMAND_INFO_SINGLE;
            case COMMAND_INFO_HEADER:                   return config.COMMAND_INFO_HEADER;
            case COMMAND_INFO_LINE:                     return config.COMMAND_INFO_LINE;
            case COMMAND_INFO_FOOTER:                   return config.COMMAND_INFO_FOOTER;

            case COMMAND_RELOAD_START:                  return config.COMMAND_RELOAD_START;
            case COMMAND_RELOAD_SUCCESS:                return config.COMMAND_RELOAD_SUCCESS;
            case COMMAND_RELOAD_ERROR:                  return config.COMMAND_RELOAD_ERROR;

            case COMMAND_TOGGLE_ACTIVATED:              return config.COMMAND_TOGGLE_ACTIVATED;
            case COMMAND_TOGGLE_DEACTIVATED:            return config.COMMAND_TOGGLE_DEACTIVATED;
        }

        return TextTemplate.of();
    }
}
