@LearnMoreAndOnboarding
Feature: As a user i want to start VHC and learn more
  Scenario: I want to start VHR
    Given I am on the home screen
    When i tap on VHR
    Then the VHR Onboarding is loaded

  Scenario: I want to view the Disclaimer
    Given I am on the VHR Onboarding screen
    When I tap Disclaimer
    Then the Disclaimer screen is loaded

  Scenario: I want to go back to Onboarding From Dislaimer
    Given I am on the VHR onboarding
    When I tap on Disclaimer button
    And I tap on back button
    Then I see VHR onboarding modal

  Scenario: I want to learn more
    Given I am on the VHR Landing screen
    When I tap Learn More
    Then the VHR Learn More screen is loaded

  Scenario: I can go back to VHR landing screen from learn more screen
    Given I am on the VHR Learn more screen
    When I tap on Back
    Then the VHR Landing screen is loaded

  Scenario: Check the information shown on the learn more screen is correct and carrier insurers are also listed
    Given I am on the VHR Landing screen
    When I tap on Learn more
    Then I will see the correct information shown

  Scenario: I want to start the VHR
    Given I am on the VHR onboarding
    When I tap on got it
    Then I will be navigated to VHC landing screen

  Scenario: Check Onboarding content
    Given I am on the home screen
    When I tap on VHR card
    Then I will see the heading Vitality Health Review
    And I will see the Disclaimer button
