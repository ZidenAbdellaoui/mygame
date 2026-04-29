package it.unicam.cs.mpgc.rpg129774.service;

import it.unicam.cs.mpgc.rpg129774.model.character.Enemy;
import it.unicam.cs.mpgc.rpg129774.model.character.Hero;
import it.unicam.cs.mpgc.rpg129774.model.combat.CombatAction;
import it.unicam.cs.mpgc.rpg129774.model.combat.CombatResult;
import it.unicam.cs.mpgc.rpg129774.model.item.Item;

/**
 * Defines the contract for resolving turn-based combat between a hero and an enemy.
 * Future implementations can add spell casting, status effects, or multiplayer logic
 * without changing dependent controllers.
 */
public interface CombatService {

    /**
     * Initialises a new combat encounter.
     */
    void initCombat(Hero hero, Enemy enemy);

    /**
     * Executes the hero's chosen action and the enemy's counter-action.
     *
     * @param action the hero's chosen action
     * @param item   the item to use (only relevant for USE_ITEM action, may be null)
     * @return a CombatResult describing what happened this turn
     */
    CombatResult executeAction(CombatAction action, Item item);

    boolean isCombatOver();

    Hero getCurrentHero();

    Enemy getCurrentEnemy();
}
