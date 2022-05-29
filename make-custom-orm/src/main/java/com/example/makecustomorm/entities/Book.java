package com.example.makecustomorm.entities;

import com.example.makecustomorm.orm.CustomColumn;
import com.example.makecustomorm.orm.CustomId;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
