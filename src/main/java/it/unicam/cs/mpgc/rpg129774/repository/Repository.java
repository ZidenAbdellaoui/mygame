package it.unicam.cs.mpgc.rpg129774.repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic persistence contract.
 * Implementations can swap storage backends (JSON, SQLite, REST) without
 * touching any service or model code — satisfying the Dependency Inversion Principle.
 *
 * @param <T> the entity type
 */
public interface Repository<T> {

    void save(T entity);

    Optional<T> load(String id);

    List<T> loadAll();

    void delete(String id);
}
