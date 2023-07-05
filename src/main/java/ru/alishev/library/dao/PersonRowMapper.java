package ru.alishev.library.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import ru.alishev.library.entity.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class PersonRowMapper implements RowMapper<Person> {
    @Override
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
        Person person = new Person();
        person.setId(rs.getInt("id"));
        person.setFullName(rs.getString("full_name"));
        person.setBirthdayDate(rs.getDate("birthday").toLocalDate());

        return person;
    }
}
