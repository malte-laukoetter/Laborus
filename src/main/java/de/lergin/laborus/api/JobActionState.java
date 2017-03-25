package de.lergin.laborus.api;

/**
 * the states of a JobAction
 */
public enum JobActionState {
    /**
     * the action was successful awarded
     */
    SUCCESS,
    /**
     * the action was blocked, most likely because the level of the player wasn't high enough
     */
    BLOCK,
    /**
     * the action was blocked due to another job
     */
    BLOCK_OTHER,
    /**
     * the action was ignored, maybe because the item wasn't one of the JobItems or another prerequisite wasn't true
     */
    IGNORE
}
