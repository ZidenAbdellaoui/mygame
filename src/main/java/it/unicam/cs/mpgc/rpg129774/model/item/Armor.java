package it.unicam.cs.mpgc.rpg129774.model.item;

/**
 * Armor that can be equipped by a hero to increase their defense stat.
 */
public class Armor extends Item {

    private final int defenseBonus;

    public Armor(String id, String name, String description, int goldValue, int defenseBonus) {
        super(id, name, description, goldValue, ItemType.ARMOR);
        this.defenseBonus = defenseBonus;
    }

    public int getDefenseBonus() { return defenseBonus; }
}
