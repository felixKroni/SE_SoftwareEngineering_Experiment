import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Book } from '../models/book.model';

@Injectable({
  providedIn: 'root'
})
export class BookService {
  // VULNERABILITY: Hardcoded API URL - could be flagged by some tools (FALSE POSITIVE in dev context)
  private apiUrl = 'http://localhost:8080/api/books';

  constructor(private http: HttpClient) { }

  // TRUE NEGATIVE: Standard HTTP methods
  getAllBooks(): Observable<Book[]> {    
    return this.http.get<Book[]>(this.apiUrl);
  }

  getBookById(id: number): Observable<Book> {
    return this.http.get<Book>(`${this.apiUrl}/${id}`);
  }

  createBook(book: Book): Observable<Book> {
    return this.http.post<Book>(this.apiUrl, book);
  }

  updateBook(id: number, book: Book): Observable<Book> {
    return this.http.put<Book>(`${this.apiUrl}/${id}`, book);
  }

  deleteBook(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // VULNERABILITY: Potential XSS - unsanitized user input sent to backend - TRUE POSITIVE
  searchBooks(query: string): Observable<any> {
    // No input sanitization before sending to backend
    return this.http.get<any>(`${this.apiUrl}/search?query=${query}`);
  }

  // VULNERABILITY: Path Traversal - user input used in URL path - TRUE POSITIVE
  exportBook(filename: string): Observable<string> {
    // No validation of filename parameter
    return this.http.get(`${this.apiUrl}/export/${filename}`, { responseType: 'text' });
  }

  // VULNERABILITY: Command Injection risk - user input for path - TRUE POSITIVE
  backupDatabase(path: string): Observable<string> {
    return this.http.get(`${this.apiUrl}/backup?path=${path}`, { responseType: 'text' });
  }

  // VULNERABILITY: Insecure data handling - TRUE POSITIVE
  generateToken(): Observable<string> {
    return this.http.get(`${this.apiUrl}/generate-token`, { responseType: 'text' });
  }
}
