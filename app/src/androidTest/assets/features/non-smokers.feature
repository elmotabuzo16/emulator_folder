@non-smokers
Feature: As a user, I want to earn points for non-smokers
  Scenario: To read the non-smokers declaration
    Given I have navigated to the home screen
    When I tap NSD
    Then the NonSmokers screen is loaded

  Scenario: To declare that I am a non-smoker
    Given I am on the NonSmokers screen
    When I tap Get Started
    Then the NonSmokersDeclaration screen is loaded

  Scenario: To learn more about the non-smoker’s declaration
    Given I am on the NonSmokers screen
    When I tap Learn More
    Then the learn more screen is loaded


  Scenario: To go back to home screen
    Given I have navigated to the home screen
    When I tap NSD
    And i tap on the Back button
    Then the home screen is loaded

#  Scenario: To view participation partners
#    Given I have launched the VA app
#    And I am on the learn more screen
#    When I click on
#    Then Participating Partners screen is loaded

  Scenario: To close the learn more screen
    Given I am on the NonSmokers screen
    When I tap Learn More
    When i tap on the Back button
    Then the NonSmokers screen is loaded

  Scenario:  To declare that the user is a non-smoker
    Given I have navigated to the home screen
    When I tap NSD
    And I tap Get Started
    Then the NonSmokersDeclaration screen is loaded
    When i tap on More then I Declare
    Then the privacy policy screen is loaded

  Scenario: To disagree with the non-smokers declaration policy
    Given I have navigated to the home screen
    When I tap NSD
    And I tap Get Started
    Then the NonSmokersDeclaration screen is loaded
    When i tap on More then I Declare
    And i tap on the Back button
    Then the NonSmokersDeclaration screen is loaded

  Scenario: To complete the declaration for non-smokers
    Given I have navigated to the home screen
    When I tap NSD
    And I tap Get Started
    Then the NonSmokersDeclaration screen is loaded
    When i tap on More then I Declare
    Then the privacy policy screen is loaded
    When i tap on More then i Agree
    Then the nonSmoking completion screen is loaded
    When I tap Great
    Then the home screen is loaded

  @Mocks-only
  Scenario: To check If the user can cancel on connection error
    Given The privacy policy is loaded
    And I am on the privacy policy screen
    And I have No internet connection
    And I tap Agree
    And I have seen an error message with Cancel and Try again options
    When I tap Cancel
    Then The error message should be removed and control should return to Privacy Policy screen
  @Mocks-only
  Scenario: To check If the user can cancel when unable to complete
    Given The privacy policy is loaded
    And I am on the privacy policy screen
    And There is an error on the service
    And I tap Agree
    And I have seen an error message with Cancel and Try again options
    When I tap Cancel
    Then The error message should be removed and control should return to Privacy Policy screen

  @Mocks-only
  Scenario: To check If the user can try again on connection error
    Given The privacy policy is loaded
    And I am on the privacy policy screen
    And I have No internet connection
    And I tap Agree
    And I have seen an error message with Cancel and Try again options
    And I have an internet connection
    And The privacy policy is loaded
    And the agree response is loaded
    When I tap Try again
    And The privacy policy is loaded
    And I have seen an error message with Cancel and Try again options
    And I have an internet connection
    And the agree response is loaded
    When I tap Try again
    Then the nonSmoking completion screen is loaded
  @Mocks-only
  Scenario: To check If the user can try again when unable to complete
    Given The privacy policy is loaded
    And I am on the privacy policy screen
    And There is an error on the service
    And I tap Agree
    And I have seen an error message with Cancel and Try again options
    And The service is working again
    And the agree response is loaded
    When I tap Try again
    Then the nonSmoking completion screen is loaded
