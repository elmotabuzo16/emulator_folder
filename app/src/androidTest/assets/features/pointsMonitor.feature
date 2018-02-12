@PointsMonitor
Feature: As a user, I want to view all points
  Scenario: I want to view navigator with points option
    Given I am on the home screen
    When I tap on drawer view
    Then the option list with ‘point’ is shown

  Scenario: I want to view points
    Given I am on the home screen
    And the drawer is shown
    When I tap on points
    Then the points screen is loaded

  Scenario: I want to view points
    Given I am on the home screen
    And the drawer is shown
    When I tap on points
    Then the points screen is loaded
    And the points screen is shown in descending order

  Scenario: I have no points to show
    Given I am on the points screen
    And I have no points
    Then the screen should display message
    And ‘help’ button is shown

  Scenario: I have a generic hard failure issue
    Given I am on the points screen
    When I have a generic hard failure error
    Then the screen should display message and ‘try again’ button

  Scenario: When there is a generic soft failure issue
    Given I am on the points screen
    When I have a generic soft failure error
    Then the points loaded are shown
    And a prompt with message and ‘retry’ button is displayed (Screen 4.2)

  Scenario: I want to view another month
    Given I am on the points screen
    And I have points
    When I tap on any previous month
    Then the month changes to selected Month
    And App shows points for selected Month

  Scenario: I want to view calendar
    Given I am on the points screen
    And I have points
    When I tap on any ‘calendar’ icon
    Then the calendar screen is shown on the month selected

  Scenario: I want to see days with points
    Given I am on the calendar screen
    And I have points
    Then days with points are displayed with a dot underneath them

  Scenario: I want to view a date with points from the calendar
    Given I am on the calendar screen
    And I have points
    When I tap on date with a dot
    Then the points screen is shown for the selected date

  Scenario: I want to view a date without points from the calendar
    Given I am on the calendar screen
    And I have no points
    When I tap on date without a dot
    Then the points screen is shown for the selected date
    And the screen should display “no point” message and ‘help’ button

  Scenario: I want to refresh points
    Given I am on the points screen
    And I have points
    When I pull the window down
    Then the refresh icon is shown
    And the points screen is refreshed

  Scenario: error refreshing the points
    Given I am on the points screen
    And the refresh icon is shown
    When I have No connection
    Then the points loaded are shown
    And a prompt with message and ‘retry’ button is displayed

  Scenario: I want to view help option
    Given I am on the points error screen
    When I tap on help
    Then help screen should show

