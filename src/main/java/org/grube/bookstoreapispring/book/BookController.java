package org.grube.bookstoreapispring.book;

import org.grube.bookstoreapispring.error.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getBooks() {
        List<Book> books = bookService.readBooks();
        return ResponseEntity.ok().body(books);
    }

    @GetMapping("/books-paged")
    public ResponseEntity<List<Book>> getBooks(
            @RequestParam int page,
            @RequestParam int limit,
            @RequestParam String sortBy) {
        // todo: validation for request params/error handling

        List<Book> books = bookService.readBooks(page, limit, sortBy);
        return ResponseEntity.ok().body(books);
    }

    @PostMapping("/books")
    public ResponseEntity<Object> postBook(@Valid @RequestBody Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ApiException apiException = new ApiException(HttpStatus.BAD_REQUEST, "Validation Error");
            bindingResult.getAllErrors().forEach((error -> apiException.addSubMessage(error.getDefaultMessage())));
            return ResponseEntity.badRequest().body(apiException);
        }
        Book newBook = bookService.createBook(book);
        return ResponseEntity.ok().body(newBook);
    }
}
