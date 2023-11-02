@wip
Feature: Rijksmuseum Collection test

 Background:
  Scenario: Verify endpoint returns 200
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

    Scenario: Verify the presence of required properties in art objects
      When I check the data types of art object properties
      Then the "objectNumber" should not be null
      And the "title" should not be null
      And the "hasImage" should not be null
      And the "showImage" should not be null
      And the "permitDownload" should not be null

    Scenario: Verify the format of URLs in art object properties
        When I check the data types of art object properties
        Then the "webImage.url" should match the URL format
        And the "headerImage.url" should match the URL format

    Scenario: Verify that all art objects have production places
      When I check the data types of art object properties
      Then all "productionPlaces" should not be null

    Scenario: Verify that the count matches the number of art objects
      Then the count should be equal to the count of art objects

    Scenario: Verify that the count of art objects with images matches the count in "countFacets.hasimage"
      Then the count of art objects with "hasImage" as true should be equal to "countFacets.hasimage"

    Scenario: Verify objectNumber format
        Given I set up Rijksmuseum api
        When I send a request
        Then status code should be 200
        And the "objectNumber" should contain all parts except "nl-"
