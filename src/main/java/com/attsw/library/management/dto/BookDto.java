package com.attsw.library.management.dto;

import jakarta.validation.constraints.NotBlank;

public class BookDto {
    
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    private Integer publishedYear;
    
    private String category;
    
    private Long borrowedByMemberId;

    public BookDto() {
    }

    public BookDto(Long id, String title, String author, String isbn, 
                   Integer publishedYear, String category, Long borrowedByMemberId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publishedYear = publishedYear;
        this.category = category;
        this.borrowedByMemberId = borrowedByMemberId;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(Integer publishedYear) {
        this.publishedYear = publishedYear;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getBorrowedByMemberId() {
        return borrowedByMemberId;
    }

    public void setBorrowedByMemberId(Long borrowedByMemberId) {
        this.borrowedByMemberId = borrowedByMemberId;
    }
}