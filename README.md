# SE_SoftwareEngineering_Experiment
This repository is an experiment for using a CI/CD Pipeline with SAST

## Book Management System - SAST Evaluation Application

This application is designed for evaluating SAST (Static Application Security Testing) tools in a DevSecOps context. It contains intentional vulnerabilities to test the detection capabilities of security analysis tools using the following metrics:

- **True Positives (TP)**: Real vulnerabilities that should be detected
- **False Positives (FP)**: Safe code that might be incorrectly flagged
- **False Negatives (FN)**: Real vulnerabilities that tools might miss
- **True Negatives (TN)**: Safe code that should not be flagged

## Application Overview

A full-stack book management application with:
- **Backend**: Java Spring Boot REST API (Port 8080)
- **Frontend**: Angular 21 (Port 4200)
- **Database**: H2 in-memory database

## Running the Application

### Backend (Java Spring Boot)

```bash
cd backend/SE_Experiment_Backend
mvn clean install
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### Frontend (Angular)

```bash
cd frontend/SE_Experiment_Frontend
npm install
npm start
```

The frontend will be available at `http://localhost:4200`

## Vulnerability Catalog

### Backend Vulnerabilities (Java)

#### 1. SQL Injection (TRUE POSITIVE)
**Location**: `BookController.java` - `searchBooks()` method
```java
String sql = "SELECT * FROM books WHERE title LIKE '%" + query + "%'";
```
**Description**: Direct string concatenation in SQL query allows SQL injection attacks.

#### 2. Hardcoded Credentials (TRUE POSITIVE)
**Location**: `BookController.java` - `searchBooks()` method
```java
String username = "sa";
String password = "password";
```
**Description**: Database credentials hardcoded in source code.

#### 3. Path Traversal (TRUE POSITIVE)
**Location**: `BookController.java` - `exportBook()` method
```java
String content = new String(Files.readAllBytes(Paths.get("/tmp/" + filename)));
```
**Description**: No sanitization of filename parameter allows reading arbitrary files.

#### 4. Information Disclosure (TRUE POSITIVE)
**Location**: `BookController.java` - `exportBook()` catch block
```java
return ResponseEntity.internalServerError().body(e.getMessage() + "\n" + e.getStackTrace()[0]);
```
**Description**: Exposing stack traces to users reveals internal application structure.

#### 5. Command Injection (TRUE POSITIVE)
**Location**: `BookController.java` - `backupDatabase()` method
```java
String command = "cp /data/books.db " + path;
Process process = Runtime.getRuntime().exec(command);
```
**Description**: User input directly concatenated into system command.

#### 6. Insecure Deserialization (TRUE POSITIVE)
**Location**: `BookController.java` - `importBooks()` method
```java
ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
Object obj = ois.readObject();
```
**Description**: Deserializing untrusted data can lead to remote code execution.

#### 7. XML External Entity (XXE) (TRUE POSITIVE)
**Location**: `BookController.java` - `uploadXml()` method
```java
DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
// Missing: factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
```
**Description**: XML parser configured without XXE protection.

#### 8. Weak Cryptography (TRUE POSITIVE)
**Location**: `BookController.java` - `encryptText()` method
```java
Cipher cipher = Cipher.getInstance("DES");
```
**Description**: Using deprecated DES encryption algorithm.

#### 9. Cross-Site Scripting (XSS) (TRUE POSITIVE)
**Location**: `BookController.java` - `getMessage()` method
```java
return ResponseEntity.ok("<html><body><h1>Message: " + msg + "</h1></body></html>");
```
**Description**: User input embedded in HTML without sanitization.

#### 10. Insecure Random (TRUE POSITIVE)
**Location**: `BookController.java` - `generateToken()` method
```java
java.util.Random random = new java.util.Random();
```
**Description**: Using predictable Random instead of SecureRandom for security token.

#### 11. Race Condition (TRUE POSITIVE)
**Location**: `BookController.java` - `incrementCounter()` method
```java
private static int counter = 0;
counter++;
```
**Description**: Non-thread-safe counter operation.

#### 12. Overly Permissive CORS (TRUE POSITIVE)
**Location**: `BookController.java` - Class annotation
```java
@CrossOrigin(origins = "*")
```
**Description**: Allows requests from any origin.

#### 13. Format String Vulnerability (POTENTIAL FALSE NEGATIVE)
**Location**: `BookController.java` - `getStatistics()` method
```java
String formatted = String.format("Total: %d", bookService.getAllBooks().size());
```
**Description**: Subtle vulnerability that some tools might miss.

### Backend Safe Code (TRUE NEGATIVES)

**Location**: `BookController.java` - CRUD methods
- `getAllBooks()`, `getBookById()`, `createBook()`, `updateBook()`, `deleteBook()`
- These use proper JPA methods with parameter binding
- No direct SQL manipulation or user input concatenation

**Location**: `BookService.java` - All methods
- Standard service layer with proper data handling
- Uses repository pattern correctly

### Frontend Vulnerabilities (TypeScript/Angular)

#### 1. No Input Validation (TRUE POSITIVE)
**Location**: `book.service.ts` - `searchBooks()`, `exportBook()`, `backupDatabase()`
```typescript
return this.http.get<any>(`${this.apiUrl}/search?query=${query}`);
```
**Description**: User input not validated before sending to backend.

#### 2. Missing Form Validation (TRUE POSITIVE)
**Location**: `book-form.component.html` - ISBN, Price, Description fields
```html
<input type="text" id="isbn" [(ngModel)]="book.isbn">
```
**Description**: No format validation for ISBN, no negative number check for price, no length limit for description.

#### 3. Path Traversal Risk (TRUE POSITIVE)
**Location**: `book.service.ts` - `exportBook()` method
```typescript
exportBook(filename: string): Observable<string> {
  return this.http.get(`${this.apiUrl}/export/${filename}`, { responseType: 'text' });
}
```
**Description**: Filename not validated before use in URL.

### Frontend Safe Code (TRUE NEGATIVES)

**Location**: `book-list.component.html`
```html
<td>{{ book.title }}</td>
```
**Description**: Angular automatically sanitizes template interpolation, preventing XSS.

**Location**: `BookService` - Standard CRUD operations
- `getAllBooks()`, `getBookById()`, `createBook()`, `updateBook()`, `deleteBook()`
- Properly use HttpClient with type safety

## Testing SAST Tools

When evaluating SAST tools with this application:

1. **True Positive Rate**: Count how many of the 16+ documented vulnerabilities are detected
2. **False Positive Rate**: Check if safe code (TRUE NEGATIVES) is incorrectly flagged
3. **False Negative Rate**: Verify if subtle vulnerabilities (like format string) are missed
4. **Precision**: Measure the accuracy of vulnerability classification

## Sample Exploits (For Testing Only)

### SQL Injection
```
GET /api/books/search?query=' OR '1'='1
```

### Path Traversal
```
GET /api/books/export/../../etc/passwd
```

### Command Injection
```
GET /api/books/backup?path=test; rm -rf /
```

### XSS
```
GET /api/books/message?msg=<script>alert('XSS')</script>
```

## API Endpoints

### Safe Endpoints
- `GET /api/books` - List all books
- `GET /api/books/{id}` - Get book by ID
- `POST /api/books` - Create new book
- `PUT /api/books/{id}` - Update book
- `DELETE /api/books/{id}` - Delete book

### Vulnerable Endpoints (For Testing)
- `GET /api/books/search?query=` - SQL Injection
- `GET /api/books/export/{filename}` - Path Traversal
- `GET /api/books/backup?path=` - Command Injection
- `POST /api/books/import` - Insecure Deserialization
- `POST /api/books/upload-xml` - XXE
- `GET /api/books/encrypt/{text}` - Weak Crypto
- `GET /api/books/message?msg=` - XSS
- `GET /api/books/generate-token` - Insecure Random
- `GET /api/books/counter` - Race Condition
- `GET /api/books/stats?format=` - Potential False Negative

## Security Notes

⚠️ **WARNING**: This application contains intentional security vulnerabilities and should NEVER be deployed to a production environment or exposed to the internet. It is designed solely for educational and testing purposes in a controlled environment.

## License

This is an educational project for SAST tool evaluation.
