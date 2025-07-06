Feature: Book Borrowing
  As a library member
  I want to borrow and return books
  So that I can read books from the library

  Scenario: Borrow an available book
    Given a book with ID 1 is available
    And a member with ID 2 exists
    When the member borrows the book
    Then the book should be marked as borrowed by the member

  Scenario: Return a borrowed book
    Given a book with ID 1 is borrowed by member with ID 2
    When the member returns the book
    Then the book should be marked as available

  Scenario: Check book availability
    Given a book with ID 1 exists
    When I check if the book is available
    Then I should get the availability status