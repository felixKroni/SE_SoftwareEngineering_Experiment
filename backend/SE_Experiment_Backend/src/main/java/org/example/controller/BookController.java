package org.example.controller;

import org.example.model.Book;
import org.example.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*") // VULNERABILITY: Overly permissive CORS - TRUE POSITIVE
public class BookController {

    @Autowired
    private BookService bookService;

    // TRUE NEGATIVE: This is secure code using proper JPA methods
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    // TRUE NEGATIVE: Proper parameter binding, no vulnerability
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // TRUE NEGATIVE: Safe POST endpoint with validation
    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookService.createBook(book);
    }

    // TRUE NEGATIVE: Safe PUT endpoint
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookService.updateBook(id, book)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // TRUE NEGATIVE: Safe DELETE endpoint
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    // VULNERABILITY: SQL Injection - TRUE POSITIVE
    // Direct string concatenation in SQL query
    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> searchBooks(@RequestParam String query) throws SQLException {
        // VULNERABILITY: Hardcoded database credentials - TRUE POSITIVE
        String url = "jdbc:h2:mem:testdb";
        String username = "sa";
        String password = "password";
        
        Connection conn = DriverManager.getConnection(url, username, password);
        Statement stmt = conn.createStatement();
        
        // VULNERABILITY: SQL Injection - Direct concatenation - TRUE POSITIVE
        String sql = "SELECT * FROM books WHERE title LIKE '%" + query + "%' OR author LIKE '%" + query + "%'";
        ResultSet rs = stmt.executeQuery(sql);
        
        List<Map<String, Object>> results = new java.util.ArrayList<>();
        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", rs.getLong("id"));
            row.put("title", rs.getString("title"));
            row.put("author", rs.getString("author"));
            results.add(row);
        }
        
        rs.close();
        stmt.close();
        conn.close();
        
        return ResponseEntity.ok(results);
    }

    // VULNERABILITY: Path Traversal - TRUE POSITIVE
    // Allows reading arbitrary files from the filesystem
    @GetMapping("/export/{filename}")
    public ResponseEntity<String> exportBook(@PathVariable String filename) {
        try {
            // VULNERABILITY: Path Traversal - No sanitization of filename - TRUE POSITIVE
            String content = new String(Files.readAllBytes(Paths.get("/tmp/" + filename)));
            return ResponseEntity.ok(content);
        } catch (IOException e) {
            // VULNERABILITY: Information Disclosure - Stack trace exposure - TRUE POSITIVE
            return ResponseEntity.internalServerError().body(e.getMessage() + "\n" + e.getStackTrace()[0]);
        }
    }

    // VULNERABILITY: Command Injection - TRUE POSITIVE
    // Executes system commands with user input
    @GetMapping("/backup")
    public ResponseEntity<String> backupDatabase(@RequestParam String path) {
        try {
            // VULNERABILITY: Command Injection - TRUE POSITIVE
            String command = "cp /data/books.db " + path;
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            return ResponseEntity.ok("Backup completed to " + path);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // VULNERABILITY: Insecure Deserialization - TRUE POSITIVE
    @PostMapping("/import")
    public ResponseEntity<String> importBooks(@RequestBody String serializedData) {
        try {
            // VULNERABILITY: Insecure Deserialization - TRUE POSITIVE
            byte[] data = java.util.Base64.getDecoder().decode(serializedData);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            Object obj = ois.readObject();
            return ResponseEntity.ok("Import completed");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // VULNERABILITY: XML External Entity (XXE) potential - TRUE POSITIVE
    @PostMapping("/upload-xml")
    public ResponseEntity<String> uploadXml(@RequestBody String xmlContent) {
        try {
            // VULNERABILITY: XXE - No external entity restriction - TRUE POSITIVE
            javax.xml.parsers.DocumentBuilderFactory factory = 
                javax.xml.parsers.DocumentBuilderFactory.newInstance();
            // Missing: factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(
                new java.io.ByteArrayInputStream(xmlContent.getBytes())
            );
            return ResponseEntity.ok("XML processed");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // VULNERABILITY: Weak cryptography - TRUE POSITIVE
    @GetMapping("/encrypt/{text}")
    public ResponseEntity<String> encryptText(@PathVariable String text) {
        try {
            // VULNERABILITY: Using weak DES encryption - TRUE POSITIVE
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DES");
            javax.crypto.KeyGenerator keyGen = javax.crypto.KeyGenerator.getInstance("DES");
            javax.crypto.SecretKey key = keyGen.generateKey();
            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(text.getBytes());
            return ResponseEntity.ok(java.util.Base64.getEncoder().encodeToString(encrypted));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // VULNERABILITY: Reflected XSS potential - TRUE POSITIVE
    // Returns user input without sanitization
    @GetMapping("/message")
    public ResponseEntity<String> getMessage(@RequestParam String msg) {
        // VULNERABILITY: XSS - No HTML escaping - TRUE POSITIVE
        return ResponseEntity.ok("<html><body><h1>Message: " + msg + "</h1></body></html>");
    }

    // VULNERABILITY: Insecure Random - TRUE POSITIVE
    @GetMapping("/generate-token")
    public ResponseEntity<String> generateToken() {
        // VULNERABILITY: Using insecure Random instead of SecureRandom - TRUE POSITIVE
        java.util.Random random = new java.util.Random();
        String token = String.valueOf(random.nextLong());
        return ResponseEntity.ok(token);
    }

    // VULNERABILITY: Race Condition - TRUE POSITIVE
    private static int counter = 0;
    
    @GetMapping("/counter")
    public ResponseEntity<Integer> incrementCounter() {
        // VULNERABILITY: Race condition - non-thread-safe counter - TRUE POSITIVE
        counter++;
        return ResponseEntity.ok(counter);
    }

    // FALSE NEGATIVE EXAMPLE: This looks safe but has a subtle vulnerability
    // Some SAST tools might miss this
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStatistics(@RequestParam(required = false) String format) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBooks", bookService.getAllBooks().size());
        
        // VULNERABILITY: Format string vulnerability (subtle) - Potential FALSE NEGATIVE
        if (format != null && format.equals("detailed")) {
            try {
                // This could be exploited with specific format strings
                String formatted = String.format("Total: %d", bookService.getAllBooks().size());
                stats.put("formatted", formatted);
            } catch (Exception e) {
                stats.put("error", e.getMessage());
            }
        }
        return ResponseEntity.ok(stats);
    }
}
