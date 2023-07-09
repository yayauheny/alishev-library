package ru.alishev.library.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.alishev.library.entity.Book;
import ru.alishev.library.entity.Person;
import ru.alishev.library.mapper.BookRowMapper;
import ru.alishev.library.mapper.PersonRowMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class PersonDao implements PersonRepository<Integer, Person> {

    private final JdbcTemplate jdbcTemplate;
    private final PersonRowMapper personMapper = new PersonRowMapper();
    private static final String FIND_ALL = """
            SELECT * FROM reader;
            """;
    private static final String FIND_BY_ID = """
            SELECT * FROM reader
             WHERE id=?;
            """;
    private static final String SAVE = """
            INSERT INTO reader(full_name, birthday)
             VALUES(?, ?);
            """;
    private static final String UPDATE = """
            UPDATE reader
               SET full_name=?, birthday=?
             WHERE id=?;
            """;
    private static final String DELETE = """
            DELETE FROM reader
             WHERE id=?;
            """;
    private static final String FIND_BOOKS = """
            SELECT * FROM book
            WHERE reader_fk=?;
            """;

    @Autowired
    public PersonDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Person> findAll() {
        return jdbcTemplate.query(FIND_ALL, personMapper);
    }

    @Override
    public Optional<Person> findById(Integer id) {
        return jdbcTemplate.query(FIND_BY_ID, personMapper, id)
                .stream().findAny();
    }

    @Override
    public Optional<Person> save(Person person) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, person.getFullName());
            ps.setDate(2, person.getBirthdayDate());
            return ps;
        }, keyHolder);

        if (rowsAffected > 0) {
            person.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
            return Optional.of(person);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void update(Integer id, Person person) {
        jdbcTemplate.update(UPDATE, person.getFullName(), person.getBirthdayDate(), id);
    }

    @Override
    public boolean delete(Integer id) {
        int deleted = jdbcTemplate.update(DELETE, id);
        return deleted > 0;
    }

    @Override
    public List<Book> findBooks(Integer id) {
        return jdbcTemplate.query(FIND_BOOKS, new BookRowMapper(), id);
    }
}
