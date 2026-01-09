# Quick Start Guide

## Prerequisites

### Backend
- Java 17 or higher
- Maven 3.6 or higher

### Frontend
- Node.js 18 or higher
- npm 9 or higher

## Installation & Running

### Step 1: Start the Backend

```bash
# Navigate to backend directory
cd backend/SE_Experiment_Backend

# Install dependencies and run
mvn clean install
mvn spring-boot:run
```

The backend API will be available at: `http://localhost:8080`

**Expected output:**
```
Started Main in X.XXX seconds
```

### Step 2: Start the Frontend

Open a new terminal:

```bash
# Navigate to frontend directory
cd frontend/SE_Experiment_Frontend

# Install dependencies
npm install

# Start development server
npm start
```

The frontend application will be available at: `http://localhost:4200`

## Verify Installation

1. Open browser to `http://localhost:4200`
2. You should see the Book Management System with a list of 4 pre-loaded books
3. Try adding a new book to test the full functionality

## API Testing

You can test the API directly with curl or Postman:

### Get all books
```bash
curl http://localhost:8080/api/books
```

### Get book by ID
```bash
curl http://localhost:8080/api/books/1
```

### Create a new book
```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Book",
    "author": "Test Author",
    "isbn": "123-456-789",
    "price": 29.99,
    "year": 2025,
    "description": "A test book"
  }'
```

### Test vulnerable endpoint (SQL Injection)
```bash
curl "http://localhost:8080/api/books/search?query=test"
```

## Database Console

H2 Database console is available at: `http://localhost:8080/h2-console`

**Connection details:**
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## Troubleshooting

### Backend won't start
- Check Java version: `java -version` (should be 17+)
- Check Maven version: `mvn -version` (should be 3.6+)
- Clear Maven cache: `mvn clean`

### Frontend won't start
- Check Node version: `node -v` (should be 18+)
- Clear npm cache: `npm cache clean --force`
- Delete node_modules and reinstall: `rm -rf node_modules && npm install`

### Port already in use
- Backend: Change port in `application.properties`: `server.port=8081`
- Frontend: Use different port: `ng serve --port 4201`

### CORS errors
- Ensure backend is running on port 8080
- Check browser console for specific error messages
- Backend has CORS enabled for all origins (`@CrossOrigin(origins = "*")`)

## Running SAST Tools

### Example: Semgrep

```bash
# Scan backend
cd backend/SE_Experiment_Backend
semgrep --config auto src/

# Scan frontend
cd frontend/SE_Experiment_Frontend
semgrep --config auto src/
```

### Example: SonarQube

```bash
# Backend
mvn sonar:sonar

# Frontend
npm run sonar
```

## Development Notes

- Backend uses H2 in-memory database - data resets on restart
- Frontend uses Angular standalone components
- Both include intentional vulnerabilities for testing purposes
- Never deploy this application to production!
