@to-implement
Feature: Wait for Loaders
  In order to be able to synchronize the test with the application
  As a test developer
  I want to be able to let the SenBot wait for all application loaders to disappear

  Scenario: Wait for loaders in the correct order
    Given I am on webpage "resource_location:/test_pages/loaderIcons.html"
      And in the source code the loader icons are assigned in the correct order
     When I click the "Button" button
      And in the source code I wait for the loaders
     Then The button "Button" is visible   

  Scenario: Wait for loaders in the wrong order
    Given I am on webpage "resource_location:/test_pages/loaderIcons.html"
      And in the source code the loader icons are assigned in the wrong order
     When I click the "Button" button
      And in the source code I wait for the loaders
     Then The button "Button" is visible   

		