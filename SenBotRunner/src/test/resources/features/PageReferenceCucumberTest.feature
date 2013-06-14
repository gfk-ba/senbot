Feature: Page reference instantiation tests 
  In order to be able to define page representation classes and reference them in my step definitions
  Developers and product owners should be able to 
  setup page definition files and reference them in their feature files

  Scenario: Case insensitive reference
   Given I am on webpage "resource_location:/test_pages/exampleTable.html"
    Then the "table page" view should show the element "table Row 1"
    Then the "Table Page" view should show the element "Table Row 1"	
    Then the "Table Page" view should not show the element "Non existing"	

  Scenario: Click steps
   Given I am on webpage "resource_location:/test_pages/exampleTable.html"
    Then the "Table Page" view should not show the element "New value cell"	
    When I click "table row 1" on the "Table Page" view
    Then the "table page" view should show the element "New value cell"
	