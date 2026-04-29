package it.unicam.cs.mpgc.rpg129774.model.map;

/**
 * Classifies the type of location, used by the UI and GameService to determine
 * available activities (e.g., only DUNGEON/FOREST/CAVE locations spawn enemies).
 */
public enum LocationType {
    TOWN,
    DUNGEON,
    FOREST,
    CAVE,
    BOSS
}
