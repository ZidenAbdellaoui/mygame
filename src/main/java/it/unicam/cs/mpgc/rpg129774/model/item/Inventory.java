package it.unicam.cs.mpgc.rpg129774.model.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The hero's item container. Enforces capacity limits and provides
 * query methods for UI and service layers.
 */
public class Inventory {

    private static final int DEFAULT_CAPACITY = 20;

    private final List<Item> items;
    private final int capacity;

    public Inventory() {
        this(DEFAULT_CAPACITY);
    }

    public Inventory(int capacity) {
        this.capacity = capacity;
        this.items = new ArrayList<>();
    }

    /**
     * @return true if the item was added, false if the inventory is full
     */
    public boolean addItem(Item item) {
        if (items.size() >= capacity) return false;
        items.add(item);
        return true;
    }

    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    public boolean removeItemById(String id) {
        return items.removeIf(i -> i.getId().equals(id));
    }

    public Optional<Item> findById(String id) {
        return items.stream().filter(i -> i.getId().equals(id)).findFirst();
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public List<Item> getItemsByType(ItemType type) {
        return items.stream().filter(i -> i.getType() == type).toList();
    }

    public int size() { return items.size(); }
    public boolean isFull() { return items.size() >= capacity; }
    public int getCapacity() { return capacity; }
}
