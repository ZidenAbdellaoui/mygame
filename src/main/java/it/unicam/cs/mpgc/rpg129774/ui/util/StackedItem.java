package it.unicam.cs.mpgc.rpg129774.ui.util;

import it.unicam.cs.mpgc.rpg129774.model.item.Item;

/**
 * A simple wrapper to display items with a quantity in the UI.
 */
public record StackedItem(Item item, int count) {
    @Override
    public String toString() {
        return item.getName() + (count > 1 ? " x" + count : "");
    }
}
