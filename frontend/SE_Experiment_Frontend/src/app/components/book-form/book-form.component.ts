import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { BookService } from '../../services/book.service';
import { Book } from '../../models/book.model';

@Component({
  selector: 'app-book-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './book-form.component.html',
  styleUrls: ['./book-form.component.css']
})
export class BookFormComponent implements OnInit {
  book: Book = {
    title: '',
    author: '',
    isbn: '',
    price: 0,
    year: new Date().getFullYear(),
    description: ''
  };
  
  isEditMode = false;
  errorMessage: string = '';

  constructor(
    private bookService: BookService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.bookService.getBookById(+id).subscribe({
        next: (data) => this.book = data,
        error: (error) => this.errorMessage = error.message
      });
    }
  }

  onSubmit(): void {
    // VULNERABILITY: No input validation - TRUE POSITIVE
    // User input is not validated before sending to backend
    if (this.isEditMode && this.book.id) {
      this.bookService.updateBook(this.book.id, this.book).subscribe({
        next: () => this.router.navigate(['/books']),
        error: (error) => this.errorMessage = error.message
      });
    } else {
      this.bookService.createBook(this.book).subscribe({
        next: () => this.router.navigate(['/books']),
        error: (error) => this.errorMessage = error.message
      });
    }
  }

  onCancel(): void {
    this.router.navigate(['/books']);
  }
}
