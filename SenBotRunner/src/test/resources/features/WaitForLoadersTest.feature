Feature: Table asserts
  In order to be able to synchronize the test with the application
  as a SenBot user
  i want to be able to tehh SenBot to waitFor all loaders to close

  @focus
  Scenario: Wait for loaders in the correct order
    Given I am on webpage "resource_location:/test_pages/loaderIcons.html"
      And in the source code the loder icons are assigned in the correct order
     When I click the "Button" button
      And in the source code I wait for the loaders
     Then The button "Button" is visible   

  @focus
  Scenario: Wait for loaders in the wrong order
    Given I am on webpage "resource_location:/test_pages/loaderIcons.html"
      And in the source code the loder icons are assigned in the wrong order
     When I click the "Button" button
      And in the source code I wait for the loaders
     Then The button "Button" is visible   

		