package ru.alishev.library.dao;

import ru.alishev.library.entity.Book;

import java.util.List;

public interface PersonRepository<K, T> extends CrudRepository<K, T> {

    List<Book> findBooks(K id);
}
