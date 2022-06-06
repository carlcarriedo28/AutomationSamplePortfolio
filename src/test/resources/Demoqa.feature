Feature: Automation Practice - Shopping Cart
  Scenario: Successful searching of Programming JavaScript Applications book
    Given Testcase "tc1" from sheet "Testdata" Books list page is displayed
    When I search and click on the book title
    And I check the book details
    Then Go back to the Book Search Page