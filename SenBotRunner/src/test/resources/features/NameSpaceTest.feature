Feature: Name spacing test runs to prevent interference with other tests
	In order to run the same tests multiple times against a single environment
	As a Test Runner
	I want that each test runs in its own namespace

  Scenario: Namespace form and table test
    Given I am on webpage "resource_location:/test_pages/namespaceTest.html"
     When I fill the field "id:textField" with "NS:My nice value"
      And I click the "Button" button
     Then table cell "id:cell_2_1" should have a namespaced value "My nice value"
      And the table "id:exampleTable" should contain the columns
     	| Table header 1   |
     	| Table cell 1     |
     	| NS:My nice value |
     	| Table cell 5     |