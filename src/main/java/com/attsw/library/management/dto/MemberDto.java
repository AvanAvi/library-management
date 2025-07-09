package com.attsw.library.management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import java.util.List;
import java.util.ArrayList;

public class MemberDto {
    
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    private List<Long> borrowedBookIds = new ArrayList<>();

    public MemberDto() {
    }

    public MemberDto(Long id, String name, String email, List<Long> borrowedBookIds) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.borrowedBookIds = borrowedBookIds != null ? borrowedBookIds : new ArrayList<>();
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Long> getBorrowedBookIds() {
        return borrowedBookIds;
    }

    public void setBorrowedBookIds(List<Long> borrowedBookIds) {
        this.borrowedBookIds = borrowedBookIds != null ? borrowedBookIds : new ArrayList<>();
    }
}