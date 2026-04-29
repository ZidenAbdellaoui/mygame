package it.unicam.cs.mpgc.rpg129774.model;

import it.unicam.cs.mpgc.rpg129774.model.character.Hero;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Aggregates all data that must be persisted between sessions.
 * This is the root object serialised to JSON for each save slot.
 */
public class GameState {

    private String id;
    private String saveDate;
    private Hero hero;
    private String currentLocationId;
    private int totalPlayTimeSeconds;
    private int dayCounter;
    private final List<String> completedQuestIds;

    public GameState(Hero hero, String currentLocationId) {
        this.id = java.util.UUID.randomUUID().toString();
        this.saveDate = LocalDateTime.now().toString();
        this.hero = hero;
        this.currentLocationId = currentLocationId;
        this.totalPlayTimeSeconds = 0;
        this.dayCounter = 1;
        this.completedQuestIds = new ArrayList<>();
    }

    public void addCompletedQuest(String questId) {
        if (!completedQuestIds.contains(questId)) completedQuestIds.add(questId);
    }

    // --- Getters & Setters ---

    public String getId() { return id; }
    public String getSaveDate() { return saveDate; }
    public Hero getHero() { return hero; }
    public String getCurrentLocationId() { return currentLocationId; }
    public void setCurrentLocationId(String id) { this.currentLocationId = id; }
    public int getTotalPlayTimeSeconds() { return totalPlayTimeSeconds; }
    public void addPlayTime(int seconds) { this.totalPlayTimeSeconds += seconds; }
    public int getDayCounter() { return dayCounter; }
    public void incrementDay() { this.dayCounter++; }
    public List<String> getCompletedQuestIds() { return List.copyOf(completedQuestIds); }
    public void setSaveDate(String date) { this.saveDate = date; }
}
