@first-timePreference
Feature: As a user i want to set my preferences

  Scenario: I want to read communication preference instruction
    Given I have logged in for the first time
    Then the preference screen is loaded
    And communication instruction is shown
    And i see email header
    And i see app notification header

  Scenario: I want to switch on email communication
    Given I have logged in for the first time
    Then the preference screen is loaded
    And email toggle is off by default
    When i toggle email on
    Then email is set on

  Scenario: I want to switch off email communication
    Given I am on the preference screen
    And email toggle is off by default
    And I toggle email on
    When I toggle email off
    Then email is set off

   Scenario: i want to set app notification
     Given I have logged in for the first time
     When the preference screen is loaded
     Then I tap the link

   Scenario: I want to read privacy  instruction
     Given I have logged in for the first time
     When the preference screen is loaded
     And I scroll down
     Then privacy instruction is shown
     Then i see analytics header
     And i see crash report header

#   Scenario: I want to switch off analytics
#     Given I am on the preference screen
#     And analytics toggle is on by default
#     When i toggle analytics off
#     Then analytics is set off
#
#  Scenario: I want to switch on analytics
#    Given I am on the preference screen
#    And analytics toggle is on by default
#    And I toggle analytics off
#    When I toggle analytics on
#    Then analytics is set on

#  Scenario: I want to switch on crash report
#    Given I am on the preference screen
#    And crash report toggle is off by default
#    When i toggle crash report on
#    Then crash report is set on
#
#  Scenario: I want to switch off crash report
#    Given I am on the preference screen
#    And crash report toggle is off by default
#    And I toggle crash report on
#    When I toggle crash report off
#    Then crash report is set off
  @test1
  Scenario: I want to read security instruction
    Given I have logged in for the first time
    When the preference screen is loaded
    And I scroll down
    And security instruction is shown
    Then i see remember me header

#  Scenario: I want to switch on remember me
#    Given I am on the preference screen
#    And remember me toggle is off by default
#    When i toggle remember me on
#    Then remember me is set on
#
#  Scenario: I want to switch off remember me
#    Given I am on the preference screen
#    And remember me toggle is off by default
#    And I toggle remember me on
#    When I toggle remember me off
#    Then remember me is set off

  Scenario: I want to save my off settings
    Given I have logged in for the first time
    When the preference screen is loaded
    And I scroll down
    And I toggle email off
    And I toggle analytics off
    And I toggle crash report off
    And I toggle remember me off
    And i tap Next
    Then a pop up with cancel and continue should show

  Scenario: I want to cancel saving my settings
    Given I have logged in for the first time
    When the preference screen is loaded
    And I scroll down
    And I toggle email off
    And I toggle analytics off
    And I toggle crash report off
    And I toggle remember me off
    And i tap Next
    Then a pop up with cancel and continue should show
    And i tap cancel
    And pop up is closed

  Scenario: I want to continue saving my settings
    Given I have logged in for the first time
    When the preference screen is loaded
    And I scroll down
    And I toggle email off
    And I toggle analytics off
    And I toggle crash report off
    And I toggle remember me off
    And i tap Next
    Then a pop up with cancel and continue should show
    And I tap continue
    And the home screen is loaded

  Scenario: I want to save my settings
    Given I have logged in for the first time
    When the preference screen is loaded
    And i tap Next
    Then the home screen is loaded