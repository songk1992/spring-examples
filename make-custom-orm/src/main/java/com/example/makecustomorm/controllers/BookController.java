package com.example.makecustomorm.controllers;

import com.example.makecustomorm.entities.Book;
import com.example.makecustomorm.repositories.BookRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping(value = "")
    public List<Book> findBooks() {
        return bookRepository.read();

    }

    @PostMapping(value = "")
    public boolean createBooks(@RequestBody Book book) {
        return bookRepository.create(book);
    }

}
