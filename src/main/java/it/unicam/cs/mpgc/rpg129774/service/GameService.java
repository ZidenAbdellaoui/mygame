package it.unicam.cs.mpgc.rpg129774.service;

import it.unicam.cs.mpgc.rpg129774.model.GameState;
import it.unicam.cs.mpgc.rpg129774.model.character.CharacterClass;
import it.unicam.cs.mpgc.rpg129774.model.character.Enemy;
import it.unicam.cs.mpgc.rpg129774.model.map.Location;

import java.util.List;
import java.util.Optional;

/**
 * Top-level game orchestration: character creation, navigation, save/load, and encounter start.
 */
public interface GameService {

    /**
     * Creates a new hero and initialises a fresh game state.
     */
    GameState newGame(String heroName, CharacterClass characterClass);

    void saveGame();

    Optional<GameState> loadGame(String stateId);

    List<GameState> listSaves();

    /**
     * Moves the hero to the given location if it is reachable.
     *
     * @return true if travel was successful
     */
    boolean travelTo(String locationId);

    Location getCurrentLocation();

    List<Location> getAvailableLocations();

    /**
     * Picks a random enemy from the current location's pool and begins combat.
     *
     * @return the enemy to fight, or empty if the location has no enemies
     */
    Optional<Enemy> startEncounter();

    GameState getCurrentState();

    CombatService getCombatService();

    QuestService getQuestService();

    InventoryService getInventoryService();
}
