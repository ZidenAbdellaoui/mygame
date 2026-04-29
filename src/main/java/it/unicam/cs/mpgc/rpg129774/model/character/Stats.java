package it.unicam.cs.mpgc.rpg129774.model.character;

/**
 * Encapsulates all numerical statistics for a character.
 * Responsible for managing stat values and the level-up mechanism.
 */
public class Stats {

    private int hp;
    private int maxHp;
    private int mana;
    private int maxMana;
    private int attack;
    private int defense;
    private int speed;
    private int level;
    private int xp;
    private int xpToNextLevel;

    public Stats(int maxHp, int maxMana, int attack, int defense, int speed) {
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.maxMana = maxMana;
        this.mana = maxMana;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.level = 1;
        this.xp = 0;
        this.xpToNextLevel = 100;
    }

    /**
     * Grants XP and triggers a level-up if the threshold is reached.
     *
     * @param amount XP to add
     * @return true if a level-up occurred
     */
    public boolean addXp(int amount) {
        this.xp += amount;
        if (this.xp >= this.xpToNextLevel) {
            levelUp();
            return true;
        }
        return false;
    }

    private void levelUp() {
        this.level++;
        this.xp -= this.xpToNextLevel;
        this.xpToNextLevel = (int) (this.xpToNextLevel * 1.5);
        this.maxHp += 10;
        this.hp = this.maxHp;
        this.maxMana += 5;
        this.mana = this.maxMana;
        this.attack += 2;
        this.defense += 1;
        this.speed += 1;
    }

    public void takeDamage(int amount) {
        this.hp = Math.max(0, this.hp - amount);
    }

    public void heal(int amount) {
        this.hp = Math.min(maxHp, this.hp + amount);
    }

    public void restoreMana(int amount) {
        this.mana = Math.min(maxMana, this.mana + amount);
    }

    public boolean isAlive() {
        return this.hp > 0;
    }

    // --- Getters & Setters ---

    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = Math.max(0, Math.min(maxHp, hp)); }
    public int getMaxHp() { return maxHp; }
    public int getMana() { return mana; }
    public void setMana(int mana) { this.mana = Math.max(0, Math.min(maxMana, mana)); }
    public int getMaxMana() { return maxMana; }
    public int getAttack() { return attack; }
    public void setAttack(int attack) { this.attack = attack; }
    public int getDefense() { return defense; }
    public void setDefense(int defense) { this.defense = defense; }
    public int getSpeed() { return speed; }
    public int getLevel() { return level; }
    public int getXp() { return xp; }
    public int getXpToNextLevel() { return xpToNextLevel; }
}
