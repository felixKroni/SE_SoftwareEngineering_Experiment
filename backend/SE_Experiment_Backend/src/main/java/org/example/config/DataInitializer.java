package org.example.config;

import jakarta.annotation.PostConstruct;
import org.example.model.Book;
import org.example.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class DataInitializer {

    @Autowired
    private BookRepository repository;

    @PostConstruct
    public void initDatabase() {
        // Run data initialization asynchronously after a short delay
        // to ensure schema is created first
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000); // Wait 1 second for schema creation
                
                // Initialize with some sample books
                Book book1 = new Book();
                book1.setTitle("Clean Code");
                book1.setAuthor("Robert C. Martin");
                book1.setIsbn("978-0132350884");
                book1.setPrice(32.99);
                book1.setYear(2008);
                book1.setDescription("A Handbook of Agile Software Craftsmanship");
                repository.save(book1);

                Book book2 = new Book();
                book2.setTitle("The Pragmatic Programmer");
                book2.setAuthor("David Thomas, Andrew Hunt");
                book2.setIsbn("978-0201616224");
                book2.setPrice(29.99);
                book2.setYear(1999);
                book2.setDescription("From Journeyman to Master");
                repository.save(book2);

                Book book3 = new Book();
                book3.setTitle("Design Patterns");
                book3.setAuthor("Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides");
                book3.setIsbn("978-0201633610");
                book3.setPrice(54.99);
                book3.setYear(1994);
                book3.setDescription("Elements of Reusable Object-Oriented Software");
                repository.save(book3);

                Book book4 = new Book();
                book4.setTitle("Refactoring");
                book4.setAuthor("Martin Fowler");
                book4.setIsbn("978-0134757599");
                book4.setPrice(47.99);
                book4.setYear(2018);
                book4.setDescription("Improving the Design of Existing Code");
                repository.save(book4);
                
                System.out.println("Sample data initialized successfully!");
            } catch (Exception e) {
                System.err.println("Failed to initialize sample data: " + e.getMessage());
            }
        });
    }
}
