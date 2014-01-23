Feature: Table asserts
  In order compare an test the content of HTML tables
  as a SenBot user
  I should be able to assert a full HTML table though Cucumber

  Scenario: Assert full table 
    Given I am on webpage "resource_location:/test_pages/exampleTable.html"
    Then the table "table locator" should match
        | Table header 1 | Table header 2 |
        | Table cell 1   | Table cell 2   |
        | Table cell 3   | Table cell 4   |
        | Table cell 5   | Table cell 6   |

  Scenario: Assert full table 
    Given I am on webpage "resource_location:/test_pages/exampleTable.html"
    Then the "table" table on view "table page1" should match
        | Table header 1 | Table header 2 |
        | Table cell 1   | Table cell 2   |
        | Table cell 3   | Table cell 4   |
        | Table cell 5   | Table cell 6   |

  Scenario: Assert selected columns
    Given I am on webpage "resource_location:/test_pages/complexExampleTable.html"
    Then the table "table locator" should contain the columns
        | Table header 1 | Table header 5 |
        | Table cell 1_1 | Table cell 1_5 |
        | <rowspan>      | <colspan>      |
        | Table cell 3_1 | Table cell 3_5 |
        | Table cell 4_1 | Table cell 4_5 |

  Scenario: Assert selected columns
    Given I am on webpage "resource_location:/test_pages/complexExampleTable.html"
    Then the "table" table on view "table page1" should contain the columns
        | Table header 1 | Table header 5 |
        | Table cell 1_1 | Table cell 1_5 |
        | <rowspan>      | <colspan>      |
        | Table cell 3_1 | Table cell 3_5 |
        | Table cell 4_1 | Table cell 4_5 |
