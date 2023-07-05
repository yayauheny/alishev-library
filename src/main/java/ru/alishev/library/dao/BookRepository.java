package ru.alishev.library.dao;

import ru.alishev.library.entity.Person;

public interface BookRepository<K, T> extends CrudRepository<K, T> {

    String getReaderName(K id);
    boolean setReader(Person person, K id);
}
