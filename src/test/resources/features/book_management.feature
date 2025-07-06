Feature: Book Management
  As a librarian
  I want to manage books in the library
  So that I can maintain the book collection

  Scenario: Add a new book
    Given I have a book with title "Clean Code" and author "Robert Martin"
    When I save the book
    Then the book should be saved with an ID

  Scenario: Find an existing book
    Given a book exists with ID 1
    When I search for the book by ID 1
    Then I should receive the book details

  Scenario: List all books
    Given there are books in the library
    When I request all books
    Then I should receive a list of books

  Scenario: Delete a book
    Given a book exists with ID 1
    When I delete the book with ID 1
    Then the book should be removed from the library