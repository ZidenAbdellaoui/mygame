package it.unicam.cs.mpgc.rpg129774.model.combat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Immutable value object describing the outcome of one combat turn.
 * Uses a builder to keep construction readable as complexity grows.
 */
public final class CombatResult {

    private final int heroDamageDealt;
    private final int heroDamageTaken;
    private final List<String> messages;
    private final boolean isOver;
    private final boolean heroWon;
    private final boolean heroFled;
    private final int xpGained;
    private final int goldGained;

    private CombatResult(Builder builder) {
        this.heroDamageDealt = builder.heroDamageDealt;
        this.heroDamageTaken = builder.heroDamageTaken;
        this.messages = Collections.unmodifiableList(builder.messages);
        this.isOver = builder.isOver;
        this.heroWon = builder.heroWon;
        this.heroFled = builder.heroFled;
        this.xpGained = builder.xpGained;
        this.goldGained = builder.goldGained;
    }

    public int getHeroDamageDealt() { return heroDamageDealt; }
    public int getHeroDamageTaken() { return heroDamageTaken; }
    public List<String> getMessages() { return messages; }
    public boolean isOver() { return isOver; }
    public boolean isHeroWon() { return heroWon; }
    public boolean isHeroFled() { return heroFled; }
    public int getXpGained() { return xpGained; }
    public int getGoldGained() { return goldGained; }

    // --- Builder ---

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private int heroDamageDealt;
        private int heroDamageTaken;
        private final List<String> messages = new ArrayList<>();
        private boolean isOver;
        private boolean heroWon;
        private boolean heroFled;
        private int xpGained;
        private int goldGained;

        public Builder heroDamageDealt(int v) { heroDamageDealt = v; return this; }
        public Builder heroDamageTaken(int v) { heroDamageTaken = v; return this; }
        public Builder addMessage(String msg) { messages.add(msg); return this; }
        public Builder over(boolean v) { isOver = v; return this; }
        public Builder heroWon(boolean v) { heroWon = v; return this; }
        public Builder heroFled(boolean v) { heroFled = v; return this; }
        public Builder xpGained(int v) { xpGained = v; return this; }
        public Builder goldGained(int v) { goldGained = v; return this; }
        public CombatResult build() { return new CombatResult(this); }
    }
}
