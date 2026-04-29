package it.unicam.cs.mpgc.rpg129774.service;

import it.unicam.cs.mpgc.rpg129774.model.character.Hero;
import it.unicam.cs.mpgc.rpg129774.model.item.Armor;
import it.unicam.cs.mpgc.rpg129774.model.item.Item;
import it.unicam.cs.mpgc.rpg129774.model.item.Potion;
import it.unicam.cs.mpgc.rpg129774.model.item.Weapon;

import java.util.List;

/**
 * Delegates inventory operations to the hero's Inventory model.
 */
public class InventoryServiceImpl implements InventoryService {

    private final Hero hero;

    public InventoryServiceImpl(Hero hero) {
        this.hero = hero;
    }

    @Override
    public boolean addItem(Item item) {
        return hero.getInventory().addItem(item);
    }

    @Override
    public boolean removeItem(Item item) {
        return hero.getInventory().removeItem(item);
    }

    @Override
    public void equipWeapon(Weapon weapon) {
        hero.equipWeapon(weapon);
    }

    @Override
    public void equipArmor(Armor armor) {
        hero.equipArmor(armor);
    }

    @Override
    public boolean usePotion(Item item) {
        if (!(item instanceof Potion potion)) return false;
        hero.getStats().heal(potion.getHealAmount());
        hero.getStats().restoreMana(potion.getManaAmount());
        hero.getInventory().removeItem(item);
        return true;
    }

    @Override
    public List<Item> getItems() {
        return hero.getInventory().getItems();
    }
}
