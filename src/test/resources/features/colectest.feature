@wip
Feature: User Verification

  Scenario: verify information about logged user
    Given I set up Rijksmuseum api
    When I send a request
    Then status code should be 200

  Scenario Outline: Verify data types of art object properties
    When I check the data types of art object properties
    Then the "<property>" should have data type "<expected_type>"

  Examples:
    | property         | expected_type |
    | objectNumber     | string        |
    | title            | string        |
    | hasImage         | boolean       |
    | showImage        | boolean       |
    | permitDownload   | boolean       |
    | productionPlaces | string        |
