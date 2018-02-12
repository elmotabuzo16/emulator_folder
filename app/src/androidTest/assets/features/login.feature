@VITAAND-65
Feature: As a user I must be able to log in with my registered email address and password
  Scenario: Empty Password
    Given I am on the Login screen
    When I have entered a correct email
    And I have NOT entered a password
    Then the login button is disabled

  Scenario: Login successfully
    Given I am on the Login screen
    And I have entered a correct email
    And I have entered a correct password
    When I tap login
    Then I should be successfully logged in

  Scenario: Empty email
    Given I am on the Login screen
    When I have NOT entered an email
    And I have entered a correct password
    Then the login button is disabled

  Scenario: empty password and email
    Given I am on the Login screen
    When I have NOT entered an email
    And I have NOT entered a password
    Then the login button is disabled

  Scenario: filled password and email
    Given I am on the Login screen
    When I have entered a correct email
    And I have entered a correct password
    Then the login button is enabled

  Scenario: To check the email validation
    Given I am on the Login screen
    When I have entered an invalid email
    Then I should get an invalid email message

  Scenario: To remove invalid username error message
    Given I am on the Login screen
    And I have entered an invalid email
    And I should get an invalid email message
    When I have entered a correct email
    Then the message should be removed


  Scenario: Email icon gets Highlighted
    Given I am on the Login screen
    When I focus on the Email field
    Then The Email icon should get highlighted

  Scenario: Password icon gets Highlighted
    Given I am on the Login screen
    When I focus on the password field
    Then The password icon should get highlighted

  Scenario: incorrect email
    Given I am on the Login screen
    And I have entered an incorrect email
    And I have entered a correct password
    When I tap login
    Then I should get an incorrect email or password message

  Scenario: incorrect password
    Given I am on the Login screen
    And I have entered a correct email
    And I have entered an incorrect password
    When I tap login
    Then I should get an incorrect email or password message

  Scenario: Incorrect username and Password
    Given I am on the Login screen
    And I have entered an incorrect email
    And I have entered an incorrect password
    When I tap login
    Then I should get an incorrect email or password message

  Scenario: To check if app displays forgot password prompt
    Given I am on the Login screen
    And I have entered an incorrect email
    And I have entered an incorrect password
    And I see an incorrect email or password message
    When I click on login again
    Then I should see a message about Forgot Password
    And I tap Forgot password
    Then the Forgot Password screen is loaded

  Scenario: View forgot password screen
    Given I am on the Login screen
    When I tap Forgot password
    Then the Forgot Password screen is loaded

  Scenario: enter my communication email
    Given I am on the Forgot Password screen
    When I enter an email address
    Then the Next button is enabled

  Scenario: To check the email validation
    Given I am on the Forgot Password screen
    When I enter an invalid email
    Then i should get an invalid email message

  Scenario: Empty email
    Given I am on the Forgot Password screen
    When I did NOT enter an email address
    Then the Next button is disabled

#  icon identifier missing
#  Scenario: Email icon gets Highlighted
#    Given I am on the Forgot Password screen
#    When I focus on the Email field
#    Then The icon should get highlighted

  Scenario: To remove invalid username error message
    Given I am on the Forgot Password screen
    And I enter an invalid email
    And i should get an invalid email message
    When I enter an email address
    Then the message should be removed

  Scenario: successful forgot password
    Given I am on the Forgot Password screen
    And I enter a registered email address
    When I tap Next
    Then pop up with email sent should show

  Scenario: Email not registered
    Given I am on the Forgot Password screen
    And I have entered a correct email but not registered
    When I tap Next
    Then pop up with email not registered should show

  Scenario: go back to login
    Given I am on the Forgot Password screen
    And I enter a registered email address
    And I tap Next
    And I have seen an error message
    When I tap OK
    Then the Login screen is loaded

  Scenario: Re-enter email address
    Given I am on the Forgot Password screen
    And I have entered a correct email but not registered
    And I tap Next
    And I have seen an error message
    When I tap OK
    Then the Forgot Password screen is loaded
