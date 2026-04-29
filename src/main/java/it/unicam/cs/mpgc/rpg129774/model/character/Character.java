package it.unicam.cs.mpgc.rpg129774.model.character;

/**
 * Abstract base class for all characters in the game (heroes and enemies).
 * Defines the common structure and behaviour contract; subclasses add specialised logic.
 */
public abstract class Character {

    protected String id;
    protected String name;
    protected Stats stats;

    protected Character(String id, String name, Stats stats) {
        this.id = id;
        this.name = name;
        this.stats = stats;
    }

    /**
     * Applies incoming damage after any class-specific mitigation.
     *
     * @param rawDamage damage amount before mitigation
     */
    public abstract void takeDamage(int rawDamage);

    /**
     * Returns the name shown in the combat log and UI.
     */
    public abstract String getDisplayName();

    public boolean isAlive() {
        return stats.isAlive();
    }

    // --- Getters ---

    public String getId() { return id; }
    public String getName() { return name; }
    public Stats getStats() { return stats; }
}
