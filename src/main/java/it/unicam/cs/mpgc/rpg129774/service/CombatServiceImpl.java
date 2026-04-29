package it.unicam.cs.mpgc.rpg129774.service;

import it.unicam.cs.mpgc.rpg129774.model.character.Enemy;
import it.unicam.cs.mpgc.rpg129774.model.character.Hero;
import it.unicam.cs.mpgc.rpg129774.model.combat.CombatAction;
import it.unicam.cs.mpgc.rpg129774.model.combat.CombatResult;
import it.unicam.cs.mpgc.rpg129774.model.item.Item;
import it.unicam.cs.mpgc.rpg129774.model.item.Potion;

/**
 * Turn-based combat implementation.
 * Hero always acts first; enemy retaliates if still alive.
 * Flee success chance = 40% + speed advantage bonus.
 */
public class CombatServiceImpl implements CombatService {

    private Hero hero;
    private Enemy enemy;
    private boolean combatOver;

    @Override
    public void initCombat(Hero hero, Enemy enemy) {
        this.hero = hero;
        this.enemy = enemy;
        this.combatOver = false;
    }

    @Override
    public CombatResult executeAction(CombatAction action, Item item) {
        if (combatOver) throw new IllegalStateException("Combat is already over.");

        CombatResult.Builder result = CombatResult.builder();

        switch (action) {
            case ATTACK -> handleAttack(result);
            case USE_ITEM -> handleUseItem(result, item);
            case FLEE -> {
                handleFlee(result);
                return result.build();
            }
        }

        // Enemy counter-attack if still alive
        if (enemy.isAlive() && hero.isAlive()) {
            int enemyDmg = enemy.calculateAttackDamage();
            hero.takeDamage(enemyDmg);
            result.heroDamageTaken(enemyDmg);
            result.addMessage(enemy.getDisplayName() + " attacks for " + enemyDmg + " damage!");
        }

        checkCombatEnd(result);
        return result.build();
    }

    private void handleAttack(CombatResult.Builder result) {
        int dmg = calcHeroDamage();
        enemy.takeDamage(dmg);
        result.heroDamageDealt(dmg);
        result.addMessage(hero.getName() + " attacks for " + dmg + " damage!");
    }

    private void handleUseItem(CombatResult.Builder result, Item item) {
        if (item instanceof Potion potion) {
            hero.getStats().heal(potion.getHealAmount());
            hero.getStats().restoreMana(potion.getManaAmount());
            hero.getInventory().removeItem(item);
            result.addMessage(hero.getName() + " uses " + potion.getName()
                    + " and restores " + potion.getHealAmount() + " HP!");
        } else {
            result.addMessage("Cannot use that item in combat.");
        }
    }

    private void handleFlee(CombatResult.Builder result) {
        int speedDiff = hero.getStats().getSpeed() - enemy.getStats().getSpeed();
        double fleeChance = 0.4 + (speedDiff * 0.03);
        boolean fled = Math.random() < fleeChance;
        result.heroFled(fled).over(fled);
        if (fled) {
            combatOver = true;
            result.addMessage(hero.getName() + " managed to escape!");
        } else {
            result.addMessage(hero.getName() + " failed to escape!");
            // Enemy retaliates
            int enemyDmg = enemy.calculateAttackDamage();
            hero.takeDamage(enemyDmg);
            result.heroDamageTaken(enemyDmg);
            result.addMessage(enemy.getDisplayName() + " attacks for " + enemyDmg + " damage!");
            if (!hero.isAlive()) {
                result.over(true);
                combatOver = true;
            }
        }
    }

    private void checkCombatEnd(CombatResult.Builder result) {
        if (!enemy.isAlive()) {
            combatOver = true;
            result.over(true).heroWon(true)
                    .xpGained(enemy.getXpReward())
                    .goldGained(enemy.getGoldDrop());
            result.addMessage(enemy.getDisplayName() + " is defeated! +"
                    + enemy.getXpReward() + " XP, +" + enemy.getGoldDrop() + " gold.");
        } else if (!hero.isAlive()) {
            combatOver = true;
            result.over(true).heroWon(false);
            result.addMessage(hero.getName() + " has been defeated...");
        }
    }

    private int calcHeroDamage() {
        int base = hero.getEffectiveAttack();
        double variance = 0.85 + Math.random() * 0.3;
        return Math.max(1, (int) (base * variance));
    }

    @Override
    public boolean isCombatOver() { return combatOver; }

    @Override
    public Hero getCurrentHero() { return hero; }

    @Override
    public Enemy getCurrentEnemy() { return enemy; }
}
