package it.unicam.cs.mpgc.rpg129774.repository;

import it.unicam.cs.mpgc.rpg129774.model.GameState;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Persists GameState objects as JSON files in saves/games/.
 */
public class GameStateRepository extends JsonRepository<GameState> {

    public GameStateRepository() {
        super(Paths.get("saves", "games"));
    }

    @Override
    protected String getId(GameState entity) {
        return entity.getId();
    }

    @Override
    protected Type getEntityType() {
        return GameState.class;
    }
}
