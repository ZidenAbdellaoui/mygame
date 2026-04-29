package it.unicam.cs.mpgc.rpg129774.model.map;

import java.util.List;

/**
 * Represents a named area in the game world.
 * Loaded from locations.json via the repository layer.
 */
public class Location {

    private final String id;
    private final String name;
    private final LocationType type;
    private final String description;
    private final List<String> possibleEnemyIds;   // enemy IDs available here
    private final List<String> connectedLocationIds;

    public Location(String id, String name, LocationType type, String description,
                    List<String> possibleEnemyIds, List<String> connectedLocationIds) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.possibleEnemyIds = List.copyOf(possibleEnemyIds);
        this.connectedLocationIds = List.copyOf(connectedLocationIds);
    }

    public boolean hasEnemies() {
        return !possibleEnemyIds.isEmpty();
    }

    // --- Getters ---

    public String getId() { return id; }
    public String getName() { return name; }
    public LocationType getType() { return type; }
    public String getDescription() { return description; }
    public List<String> getPossibleEnemyIds() { return possibleEnemyIds; }
    public List<String> getConnectedLocationIds() { return connectedLocationIds; }
}
