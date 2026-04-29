package it.unicam.cs.mpgc.rpg129774.model.character;

/**
 * An enemy character loaded from enemies.json.
 * Defines combat AI as a simple enum-driven pattern.
 */
public class Enemy extends Character {

    private final int xpReward;
    private final int goldDrop;

    public Enemy(String id, String name, Stats stats, int xpReward, int goldDrop) {
        super(id, name, stats);
        this.xpReward = xpReward;
        this.goldDrop = goldDrop;
    }

    @Override
    public void takeDamage(int rawDamage) {
        // Enemies have no defense reduction by default; subclasses could override
        int finalDamage = Math.max(1, rawDamage - stats.getDefense());
        stats.takeDamage(finalDamage);
    }

    @Override
    public String getDisplayName() { return name; }

    /**
     * Calculates the damage this enemy deals in a turn.
     */
    public int calculateAttackDamage() {
        int base = stats.getAttack();
        // ±20% random variance
        double variance = 0.8 + Math.random() * 0.4;
        return (int) (base * variance);
    }

    public int getXpReward() { return xpReward; }
    public int getGoldDrop() { return goldDrop; }
}
