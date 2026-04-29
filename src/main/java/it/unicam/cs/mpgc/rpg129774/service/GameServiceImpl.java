package it.unicam.cs.mpgc.rpg129774.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.unicam.cs.mpgc.rpg129774.model.GameState;
import it.unicam.cs.mpgc.rpg129774.model.character.CharacterClass;
import it.unicam.cs.mpgc.rpg129774.model.character.Enemy;
import it.unicam.cs.mpgc.rpg129774.model.character.Hero;
import it.unicam.cs.mpgc.rpg129774.model.character.Stats;
import it.unicam.cs.mpgc.rpg129774.model.map.Location;
import it.unicam.cs.mpgc.rpg129774.model.map.LocationType;
import it.unicam.cs.mpgc.rpg129774.model.quest.Quest;
import it.unicam.cs.mpgc.rpg129774.repository.GameStateRepository;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.Logger;

/**
 * Orchestrates all game systems: character creation, map navigation, encounters, and persistence.
 * Sources map/enemy/quest data from bundled JSON resources at startup.
 */
public class GameServiceImpl implements GameService {

    private static final Logger LOGGER = Logger.getLogger(GameServiceImpl.class.getName());

    private final GameStateRepository stateRepository;
    private final CombatService combatService;
    private final Gson gson;

    private GameState currentState;
    private QuestService questService;
    private InventoryService inventoryService;

    private final Map<String, Location> locationMap = new LinkedHashMap<>();
    private final Map<String, EnemyTemplate> enemyTemplates = new HashMap<>();
    private final List<Quest> questTemplates = new ArrayList<>();

    public GameServiceImpl() {
        this.stateRepository = new GameStateRepository();
        this.combatService = new CombatServiceImpl();
        this.gson = new GsonBuilder().create();
        loadStaticData();
    }

    // ---- Data Loading ----

    private void loadStaticData() {
        loadLocations();
        loadEnemies();
        loadQuests();
    }

    private void loadLocations() {
        try (InputStream is = getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg129774/data/locations.json");
             InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(is))) {
            Type listType = new TypeToken<List<LocationData>>() {}.getType();
            List<LocationData> data = gson.fromJson(reader, listType);
            for (LocationData d : data) {
                Location loc = new Location(d.id, d.name,
                        LocationType.valueOf(d.type), d.description,
                        d.possibleEnemyIds != null ? d.possibleEnemyIds : List.of(),
                        d.connectedLocationIds != null ? d.connectedLocationIds : List.of());
                locationMap.put(loc.getId(), loc);
            }
        } catch (Exception e) {
            LOGGER.severe("Failed to load locations.json: " + e.getMessage());
        }
    }

    private void loadEnemies() {
        try (InputStream is = getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg129774/data/enemies.json");
             InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(is))) {
            Type listType = new TypeToken<List<EnemyTemplate>>() {}.getType();
            List<EnemyTemplate> templates = gson.fromJson(reader, listType);
            templates.forEach(t -> enemyTemplates.put(t.id, t));
        } catch (Exception e) {
            LOGGER.severe("Failed to load enemies.json: " + e.getMessage());
        }
    }

    private void loadQuests() {
        try (InputStream is = getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg129774/data/quests.json");
             InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(is))) {
            Type listType = new TypeToken<List<QuestData>>() {}.getType();
            List<QuestData> data = gson.fromJson(reader, listType);
            for (QuestData d : data) {
                questTemplates.add(new Quest(d.id, d.title, d.description,
                        d.xpReward, d.goldReward, d.requiredKills != null ? d.requiredKills : Map.of()));
            }
        } catch (Exception e) {
            LOGGER.severe("Failed to load quests.json: " + e.getMessage());
        }
    }

    // ---- GameService implementation ----

    @Override
    public GameState newGame(String heroName, CharacterClass characterClass) {
        Hero hero = new Hero(heroName, characterClass);
        String startLocationId = locationMap.isEmpty() ? "town" : locationMap.keySet().iterator().next();
        currentState = new GameState(hero, startLocationId);
        initSubServices();
        return currentState;
    }

    @Override
    public void saveGame() {
        if (currentState != null) {
            currentState.setSaveDate(java.time.LocalDateTime.now().toString());
            stateRepository.save(currentState);
        }
    }

    @Override
    public Optional<GameState> loadGame(String stateId) {
        Optional<GameState> loaded = stateRepository.load(stateId);
        loaded.ifPresent(gs -> {
            currentState = gs;
            initSubServices();
        });
        return loaded;
    }

    @Override
    public List<GameState> listSaves() {
        return stateRepository.loadAll();
    }

    @Override
    public boolean travelTo(String locationId) {
        Location current = getCurrentLocation();
        if (current == null) return false;
        if (!current.getConnectedLocationIds().contains(locationId)) return false;
        currentState.setCurrentLocationId(locationId);
        return true;
    }

    @Override
    public Location getCurrentLocation() {
        if (currentState == null) return null;
        return locationMap.get(currentState.getCurrentLocationId());
    }

    @Override
    public List<Location> getAvailableLocations() {
        Location current = getCurrentLocation();
        if (current == null) return List.of();
        return current.getConnectedLocationIds().stream()
                .map(locationMap::get)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public Optional<Enemy> startEncounter() {
        Location loc = getCurrentLocation();
        if (loc == null || !loc.hasEnemies()) return Optional.empty();

        List<String> enemyIds = loc.getPossibleEnemyIds();
        String randomId = enemyIds.get(new Random().nextInt(enemyIds.size()));
        EnemyTemplate t = enemyTemplates.get(randomId);
        if (t == null) return Optional.empty();

        Stats stats = new Stats(t.hp, 0, t.attack, t.defense, t.speed);
        Enemy enemy = new Enemy(t.id, t.name, stats, t.xpReward, t.goldDrop);
        combatService.initCombat(currentState.getHero(), enemy);
        return Optional.of(enemy);
    }

    @Override
    public GameState getCurrentState() { return currentState; }

    @Override
    public CombatService getCombatService() { return combatService; }

    @Override
    public QuestService getQuestService() { return questService; }

    @Override
    public InventoryService getInventoryService() { return inventoryService; }

    // ---- Helpers ----

    private void initSubServices() {
        Hero hero = currentState.getHero();
        // Fresh quest list each game (completed quests filtered by state)
        List<Quest> activeQuests = new ArrayList<>(questTemplates);
        questService = new QuestServiceImpl(activeQuests, hero);
        inventoryService = new InventoryServiceImpl(hero);
    }

    // ---- Inner DTOs for Gson deserialization ----

    private static class LocationData {
        String id, name, type, description;
        List<String> possibleEnemyIds, connectedLocationIds;
    }

    private static class EnemyTemplate {
        String id, name;
        int hp, attack, defense, speed, xpReward, goldDrop;
    }

    private static class QuestData {
        String id, title, description;
        int xpReward, goldReward;
        Map<String, Integer> requiredKills;
    }
}
