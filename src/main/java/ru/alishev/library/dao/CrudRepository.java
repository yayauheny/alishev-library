package ru.alishev.library.dao;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<K, T> {

    List<T> findAll();

    Optional<T> findById(K id);

    Optional<T> save(T entity);

    void update(T entity);

    boolean delete(K id);

}
