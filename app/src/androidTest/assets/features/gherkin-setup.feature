@dev-test
Feature: Test that gherkin tests work
  Scenario: Starting the application
    Given I am on the Login screen
    When I have entered a correct email
    Then the Login screen is loaded
    And the "Log in" button is disabled
