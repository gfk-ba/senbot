Feature: Test the referenced view capabilities
  In order to keep all DOM element locators like xpath, id and css identifiers organized within their own context
  As a Developer 
  I should be able to contribute views containing view element locators describing a page or part thereof

  Scenario: Test referenced view available step definitions
   Given I am on webpage "resource_location:/test_pages/cucumber_test_page1.html"
    Then the "Test page1" view should contain the element "link2"
     And the "Test page1" view should show the element "navigation"
    When I click "hide navigation link" on the "Test page1" view
    Then the "Test page1" view should not show the element "link2"
     And the "Test page1" view should not show the element "link2"
   
  Scenario: Test referenced view logic though custom stepdefintions
   Given I am on webpage "resource_location:/test_pages/cucumber_test_page1.html"
    When I call a custom function in my view definition
    
   	