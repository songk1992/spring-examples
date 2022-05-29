package com.example.makecustomorm.repositories;

import com.example.makecustomorm.entities.Book;
import com.example.makecustomorm.orm.CustomOrm;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepository {
    CustomOrm<Book> customOrm = CustomOrm.connect();

    public List<Book> read() {
        return customOrm.read(Book.class);
    }

    public boolean create(Book book) {
        return customOrm.create(book);
    }
}
