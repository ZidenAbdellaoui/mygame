package it.unicam.cs.mpgc.rpg129774.model.item;

/**
 * Abstract base class for all game items.
 * Subclasses define their specific effect and usage rules.
 */
public abstract class Item {

    protected String id;
    protected String name;
    protected String description;
    protected int goldValue;
    protected ItemType type;

    protected Item(String id, String name, String description, int goldValue, ItemType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.goldValue = goldValue;
        this.type = type;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getGoldValue() { return goldValue; }
    public ItemType getType() { return type; }

    @Override
    public String toString() {
        return name;
    }
}
