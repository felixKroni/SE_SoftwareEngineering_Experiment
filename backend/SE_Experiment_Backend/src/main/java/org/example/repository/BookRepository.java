package org.example.repository;

import org.example.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    // VULNERABILITY: SQL Injection - Native query with string concatenation
    // FALSE POSITIVE: This could be flagged even though it uses parameters correctly
    @Query(value = "SELECT * FROM books WHERE title LIKE %?1%", nativeQuery = true)
    List<Book> searchByTitle(String title);
}
