package it.unicam.cs.mpgc.rpg129774.model.item;

/**
 * A weapon that can be equipped by a hero to increase their attack stat.
 */
public class Weapon extends Item {

    private final int attackBonus;
    private final double accuracy;

    public Weapon(String id, String name, String description, int goldValue, int attackBonus, double accuracy) {
        super(id, name, description, goldValue, ItemType.WEAPON);
        this.attackBonus = attackBonus;
        this.accuracy = accuracy == 0.0 ? 1.0 : accuracy;
    }

    public int getAttackBonus() { return attackBonus; }
    public double getAccuracy() { return accuracy == 0.0 ? 1.0 : accuracy; }
}
