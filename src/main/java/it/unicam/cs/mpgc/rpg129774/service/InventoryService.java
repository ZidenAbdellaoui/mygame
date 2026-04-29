package it.unicam.cs.mpgc.rpg129774.service;

import it.unicam.cs.mpgc.rpg129774.model.item.Armor;
import it.unicam.cs.mpgc.rpg129774.model.item.Item;
import it.unicam.cs.mpgc.rpg129774.model.item.Weapon;

import java.util.List;

/**
 * Contract for managing the hero's inventory and equipment.
 */
public interface InventoryService {

    boolean addItem(Item item);

    boolean removeItem(Item item);

    void equipWeapon(Weapon weapon);

    void equipArmor(Armor armor);

    /**
     * Uses a potion from the inventory, applying its effects to the hero.
     *
     * @param item the potion item to consume
     * @return true if the item was used successfully
     */
    boolean usePotion(Item item);

    List<Item> getItems();
}
