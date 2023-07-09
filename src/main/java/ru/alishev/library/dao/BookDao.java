package ru.alishev.library.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.alishev.library.entity.Book;
import ru.alishev.library.entity.Person;
import ru.alishev.library.mapper.BookRowMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class BookDao implements BookRepository<Integer, Book> {

    private final JdbcTemplate jdbcTemplate;
    private final BookRowMapper bookMapper = new BookRowMapper();
    private static final String FIND_ALL = """
            SELECT * FROM book;
            """;
    private static final String FIND_BY_ID = """
            SELECT * FROM book
             WHERE id=?;
            """;
    private static final String SAVE = """
            INSERT INTO book(title, author, year)
            VALUES (?, ?, ?);
            """;
    private static final String UPDATE = """
            UPDATE book
               SET title=?, author=?, year=?
             WHERE id=?
            """;
    private static final String DELETE = """
            DELETE FROM book
             WHERE id=?
            """;
    private static final String FIND_READER_NAME = """
            SELECT reader.full_name
            FROM book
            LEFT JOIN reader ON book.reader_fk=reader.id
            WHERE book.id=?;
            """;
    private static final String SET_READER = """
            UPDATE book
            SET reader_fk=?
            WHERE id=?;
            """;

    @Autowired
    public BookDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String getReaderName(Integer id) {
        return jdbcTemplate.queryForObject(FIND_READER_NAME, String.class, id);
    }

    @Override
    public boolean setReader(Person person, Integer bookId) {
        int updated = jdbcTemplate.update(SET_READER, person.getId(), bookId);
        return updated > 0;
    }

    @Override
    public List<Book> findAll() {
        return jdbcTemplate.query(FIND_ALL, bookMapper);
    }

    @Override
    public Optional<Book> findById(Integer id) {
        return jdbcTemplate.query(FIND_BY_ID, bookMapper, id)
                .stream().findAny();
    }

    @Override
    public Optional<Book> save(Book book) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setInt(3, book.getYear().getValue());
            return ps;
        }, keyHolder);

        if (rowsAffected > 0) {
            book.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
            return Optional.of(book);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void update(Integer id, Book book) {
        jdbcTemplate.update(UPDATE, book.getTitle(), book.getAuthor(), book.getYear(), id);
    }

    @Override
    public boolean delete(Integer id) {
        int deleted = jdbcTemplate.update(DELETE, id);
        return deleted > 0;
    }
}
