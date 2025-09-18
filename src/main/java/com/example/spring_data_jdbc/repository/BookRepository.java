package com.example.spring_data_jdbc.repository;

import com.example.spring_data_jdbc.model.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Book> bookRowMapper = (rs, rowNum) -> {
        Book book = new Book();
        book.setId(rs.getLong("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setPublicationYear(rs.getInt("publication_year"));
        return book;
    };

    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> findAll() {
        String sql = "SELECT * FROM books";
        return jdbcTemplate.query(sql, bookRowMapper);
    }

    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        List<Book> books = jdbcTemplate.query(sql, bookRowMapper, id);
        return books.isEmpty() ? Optional.empty() : Optional.of(books.get(0));
    }

    public Book save(Book book) {
        if (book.getId() == null) {
            // Insert
            String sql = "INSERT INTO books (title, author, publication_year) VALUES (?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, book.getTitle());
                ps.setString(2, book.getAuthor());
                ps.setInt(3, book.getPublicationYear());
                return ps;
            }, keyHolder);

            book.setId(keyHolder.getKey().longValue());
        } else {
            // Update
            String sql = "UPDATE books SET title = ?, author = ?, publication_year = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    book.getTitle(),
                    book.getAuthor(),
                    book.getPublicationYear(),
                    book.getId());
        }
        return book;
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM books WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM books WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count > 0;
    }
}
