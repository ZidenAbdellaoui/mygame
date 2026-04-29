package it.unicam.cs.mpgc.rpg129774.model.character;

/**
 * Enumerates all playable character classes, each with unique base stat distributions.
 * To add a new class, simply add a new constant here — no other class changes required.
 */
public enum CharacterClass {

    WARRIOR("Warrior",
            "A mighty fighter skilled in melee combat. High HP and defense.",
            120, 20, 15, 8, 5),

    MAGE("Mage",
            "A powerful spellcaster with vast magical knowledge. High mana and attack.",
            70, 100, 18, 4, 6),

    ROGUE("Rogue",
            "A nimble trickster who strikes from the shadows. High speed and attack.",
            90, 40, 14, 5, 11),

    PALADIN("Paladin",
            "A holy warrior who balances sword and healing magic.",
            100, 60, 12, 7, 7);

    private final String displayName;
    private final String description;
    private final int baseHp;
    private final int baseMana;
    private final int baseAttack;
    private final int baseDefense;
    private final int baseSpeed;

    CharacterClass(String displayName, String description,
                   int baseHp, int baseMana, int baseAttack, int baseDefense, int baseSpeed) {
        this.displayName = displayName;
        this.description = description;
        this.baseHp = baseHp;
        this.baseMana = baseMana;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.baseSpeed = baseSpeed;
    }

    public Stats createStats() {
        return new Stats(baseHp, baseMana, baseAttack, baseDefense, baseSpeed);
    }

    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public int getBaseHp() { return baseHp; }
    public int getBaseMana() { return baseMana; }
    public int getBaseAttack() { return baseAttack; }
    public int getBaseDefense() { return baseDefense; }
    public int getBaseSpeed() { return baseSpeed; }
}
