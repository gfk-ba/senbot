Feature: Test All Gherkin features
  In order to ensure SenBot users can develop according to the Behavioral Driven Developmen methodology
  Developers and product owners should be able to 
  write Stories using Gherkin/Cucumber

  Background: Navigate to start page for all scenarios
  	Given I am on webpage "resource_location:/test_pages/cucumber_test_page1.html"

  Scenario Outline: The navigation links are on the page
    When I click on the link with id "<link_id>"
  	Then the page title is "<page_title>"
  
    Examples: link result pairs
      |  link_id  |     page_title       |
      | testPage1 | Cucumber test page 1 |
      | testPage2 | Cucumber test page 2 |
      | testPage3 | Cucumber test page 3 |

  Scenario: The navigation is removed from the view
    When I click on the link with id "testPage1"
     And I click on the link with id "hide_navigation_link"
    Then the element "id:hide_navigation_link" is "invisible"
     But the element "id:navigation" is "visible"
   
  Scenario: Table data argument test
    When I visit the pages:
      | resource_location:/test_pages/cucumber_test_page1.html |
	  | resource_location:/test_pages/cucumber_test_page2.html |
	  | resource_location:/test_pages/cucumber_test_page3.html |
	Then the page title is "Cucumber test page 3"
	