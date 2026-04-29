package it.unicam.cs.mpgc.rpg129774.model.combat;

/**
 * Represents the possible actions a hero can choose during their combat turn.
 * New actions (spells, guard, etc.) can be added here without touching CombatService logic.
 */
public enum CombatAction {
    ATTACK,
    USE_ITEM,
    FLEE
}
