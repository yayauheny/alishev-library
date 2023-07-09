package ru.alishev.library.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.alishev.library.entity.Book;
import ru.alishev.library.entity.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;

public class BookRowMapper implements RowMapper<Book> {
    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setYear(Year.of(rs.getInt("year")));

        return book;
    }
}
