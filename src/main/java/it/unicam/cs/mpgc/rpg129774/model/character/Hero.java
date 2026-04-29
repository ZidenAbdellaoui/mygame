package it.unicam.cs.mpgc.rpg129774.model.character;

import it.unicam.cs.mpgc.rpg129774.model.item.Armor;
import it.unicam.cs.mpgc.rpg129774.model.item.Inventory;
import it.unicam.cs.mpgc.rpg129774.model.item.Weapon;

import java.util.UUID;

/**
 * Represents the player's character.
 * Manages inventory, equipped gear, and class-specific stat bonuses.
 */
public class Hero extends Character {

    private final CharacterClass characterClass;
    private final Inventory inventory;
    private Weapon equippedWeapon;
    private Armor equippedArmor;
    private int gold;

    public Hero(String name, CharacterClass characterClass) {
        super(UUID.randomUUID().toString(), name, characterClass.createStats());
        this.characterClass = characterClass;
        this.inventory = new Inventory();
        this.gold = 50;
    }

    @Override
    public void takeDamage(int rawDamage) {
        int effectiveDefense = stats.getDefense()
                + (equippedArmor != null ? equippedArmor.getDefenseBonus() : 0);
        int finalDamage = Math.max(1, rawDamage - effectiveDefense);
        stats.takeDamage(finalDamage);
    }

    @Override
    public String getDisplayName() {
        return name + " the " + characterClass.getDisplayName();
    }

    /**
     * Returns the hero's total effective attack (base + weapon bonus).
     */
    public int getEffectiveAttack() {
        return stats.getAttack()
                + (equippedWeapon != null ? equippedWeapon.getAttackBonus() : 0);
    }

    /**
     * Returns true if a level-up occurred.
     */
    public boolean gainXp(int amount) {
        return stats.addXp(amount);
    }

    public void equipWeapon(Weapon weapon) {
        if (equippedWeapon != null) inventory.addItem(equippedWeapon);
        this.equippedWeapon = weapon;
        inventory.removeItem(weapon);
    }

    public void unequipWeapon() {
        if (equippedWeapon != null) {
            inventory.addItem(equippedWeapon);
            equippedWeapon = null;
        }
    }

    public void equipArmor(Armor armor) {
        if (equippedArmor != null) inventory.addItem(equippedArmor);
        this.equippedArmor = armor;
        inventory.removeItem(armor);
    }

    public void unequipArmor() {
        if (equippedArmor != null) {
            inventory.addItem(equippedArmor);
            equippedArmor = null;
        }
    }

    public void addGold(int amount) { this.gold += amount; }
    public boolean spendGold(int amount) {
        if (gold < amount) return false;
        gold -= amount;
        return true;
    }

    // --- Getters ---

    public CharacterClass getCharacterClass() { return characterClass; }
    public Inventory getInventory() { return inventory; }
    public Weapon getEquippedWeapon() { return equippedWeapon; }
    public Armor getEquippedArmor() { return equippedArmor; }
    public int getGold() { return gold; }
}
