package it.unicam.cs.mpgc.rpg129774.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Abstract JSON-file-backed repository. Subclasses provide the entity type,
 * the file-naming strategy, and an ID extractor.
 *
 * @param <T> the entity type
 */
public abstract class JsonRepository<T> implements Repository<T> {

    private static final Logger LOGGER = Logger.getLogger(JsonRepository.class.getName());

    protected final Gson gson;
    protected final Path baseDir;

    protected JsonRepository(Path baseDir) {
        this.baseDir = baseDir;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            Files.createDirectories(baseDir);
        } catch (IOException e) {
            LOGGER.severe("Cannot create save directory: " + e.getMessage());
        }
    }

    /**
     * Returns the unique identifier of the given entity.
     */
    protected abstract String getId(T entity);

    /**
     * Returns the Gson type token for deserialization.
     */
    protected abstract Type getEntityType();

    protected Path filePath(String id) {
        return baseDir.resolve(id + ".json");
    }

    @Override
    public void save(T entity) {
        Path file = filePath(getId(entity));
        try (Writer writer = Files.newBufferedWriter(file)) {
            gson.toJson(entity, writer);
        } catch (IOException e) {
            LOGGER.severe("Failed to save entity " + getId(entity) + ": " + e.getMessage());
        }
    }

    @Override
    public Optional<T> load(String id) {
        Path file = filePath(id);
        if (!Files.exists(file)) return Optional.empty();
        try (Reader reader = Files.newBufferedReader(file)) {
            return Optional.ofNullable(gson.fromJson(reader, getEntityType()));
        } catch (Exception e) {
            LOGGER.severe("Failed to load entity " + id + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<T> loadAll() {
        List<T> results = new ArrayList<>();
        if (!Files.exists(baseDir)) return results;
        try (var stream = Files.list(baseDir)) {
            stream.filter(p -> p.toString().endsWith(".json")).forEach(p -> {
                try (Reader reader = Files.newBufferedReader(p)) {
                    T entity = gson.fromJson(reader, getEntityType());
                    if (entity != null) results.add(entity);
                } catch (Exception e) {
                    LOGGER.warning("Skipping unreadable or incompatible file: " + p);
                }
            });
        } catch (IOException e) {
            LOGGER.severe("Failed to list save files: " + e.getMessage());
        }
        return results;
    }

    @Override
    public void delete(String id) {
        try {
            Files.deleteIfExists(filePath(id));
        } catch (IOException e) {
            LOGGER.warning("Failed to delete entity " + id + ": " + e.getMessage());
        }
    }
}
