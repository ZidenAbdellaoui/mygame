package it.unicam.cs.mpgc.rpg129774.model.item;

/**
 * A weapon that can be equipped by a hero to increase their attack stat.
 */
public class Weapon extends Item {

    private final int attackBonus;

    public Weapon(String id, String name, String description, int goldValue, int attackBonus) {
        super(id, name, description, goldValue, ItemType.WEAPON);
        this.attackBonus = attackBonus;
    }

    public int getAttackBonus() { return attackBonus; }
}
