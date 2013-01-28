Feature: Web services test demo
   In order to showcase how web services can be tested using SenBot
   as a SenBot user
   I can execute cucumber tests that do not use the multi-browser selenium setup

  Scenario: failed testServiceAuthentication though API call
    When I set the System property "SenBotCustomProp" to "IsSet"
    Then the System property "SenBotCustomProp" should be "IsSet"