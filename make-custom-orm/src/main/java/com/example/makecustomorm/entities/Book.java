package com.example.makecustomorm.entities;

import com.example.makecustomorm.orm.CustomColumn;
import com.example.makecustomorm.orm.CustomId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Book {

    @CustomId
    long id;

    @CustomColumn
    String category;

    @CustomColumn
    String isbn;

    @CustomColumn
    String title;

    public Book(long id, String category, String isbn, String title) {
        this.id = id;
        this.category = category;
        this.isbn = isbn;
        this.title = title;
    }

    public Book() {
    }
}
