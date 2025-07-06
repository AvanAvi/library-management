Feature: Member Management
  As a librarian
  I want to manage library members
  So that I can track who can borrow books

  Scenario: Register a new member
    Given I have a member with name "John Doe" and email "john@email.com"
    When I register the member
    Then the member should be registered with an ID

  Scenario: Find an existing member
    Given a member exists with ID 1
    When I search for the member by ID 1
    Then I should receive the member details

  Scenario: List all members
    Given there are members in the library
    When I request all members
    Then I should receive a list of members