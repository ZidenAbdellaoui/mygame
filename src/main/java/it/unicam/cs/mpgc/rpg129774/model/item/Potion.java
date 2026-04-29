package it.unicam.cs.mpgc.rpg129774.model.item;

/**
 * A consumable potion that restores HP or Mana when used.
 */
public class Potion extends Item {

    private final int healAmount;
    private final int manaAmount;

    public Potion(String id, String name, String description, int goldValue,
                  int healAmount, int manaAmount) {
        super(id, name, description, goldValue, ItemType.POTION);
        this.healAmount = healAmount;
        this.manaAmount = manaAmount;
    }

    public int getHealAmount() { return healAmount; }
    public int getManaAmount() { return manaAmount; }
}
