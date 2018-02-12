@VITAAND-64
Feature: Registration Error scenarios
  Scenario: To check user can navigate to register screen
    Given I am on the Login screen
    When i tap Register
    Then the Registration screen is loaded

  Scenario: To check error message for incorrect Email and correct insurer code
    Given I am on the Registration screen
    And I have entered an incorrect email address
    And I have entered a valid password
    And I have entered a correct insurer code
    When I tap Register
    Then I should get an error message about incorrect insurer code

  Scenario: To check error message for correct Email and incorrect insurer code
    Given I am on the Registration screen
    And I have entered a correct email address
    And I have entered a valid password
    And I have entered an incorrect insurer code
    When I tap Register
    Then I should get an error message about incorrect insurer code

  Scenario: To check error message for an incorrect Email and an incorrect insurer code
    Given I am on the Registration screen
    And I have entered an incorrect email address
    And I have entered a valid password
    And I have entered an incorrect insurer code
    When I tap Register
    Then I should get an error message about incorrect insurer code

  @Mocks-only
  Scenario: To check if user can Cancel the error message
    Given I am on the Registration screen
    And I have entered all the registration details
    And There is an error on the service
    And I have clicked on Register
    And I have seen an error message with Cancel and Try again options
    When I tap Cancel
    Then The error message should be removed and control should return to Registration screen

  @Mocks-only
  Scenario: Check if user is able to Resubmit the registration details
    Given I am on the Registration screen
    And I have entered all the registration details
    And There is an error on the service
    And I have clicked on Register
    And I have seen an error message with Cancel and Try again options
    And The service is working again
    And the agree response is loaded
    When I tap Try again
    Then I should be successfully logged in
  @Mocks-only
  Scenario: Check if user is able to Cancel the No connection error
    Given I have No internet connection
    And I am on the Registration screen
    And I have entered all the registration details
    And I have clicked on Register
    And I have seen an error message with Cancel and Try again options
    When I tap Cancel
    Then The error message should be removed and control should return to Registration screen

# this fails, since the mock server does not record connection attempts
  @Mocks-only
  Scenario: Check if user is able to Resubmit the details after the No connection error
    Given I have No internet connection
    And I am on the Registration screen
    And I have entered all the registration details
    And I have clicked on Register
    And I have seen an error message with Cancel and Try again options
    When I click on Try again
    Then The app should resubmit the registration details

  Scenario: To check when register without email address
    Given I am on the Registration screen
    When I have NOT entered an email address
    And I have entered a password
    And I have entered a confirm password
    And I have entered an insurer code
    Then the register button is disabled

  Scenario: To check when register without Password
    Given I am on the Registration screen
    When I have NO password
    And I have entered an email address
    And I have entered a confirm password
    And I have entered an insurer code
    Then the register button is disabled

  Scenario: To check when register without confirm password
    Given I am on the Registration screen
    When I have NO confirm password
    And I have entered a password
    And I have entered an email address
    And I have entered an insurer code
    Then the register button is disabled

  Scenario: To check when register without an insurer code
    Given I am on the Registration screen
    When I have NO insurer code
    And I have entered a password
    And I have entered a confirm password
    And I have entered an email address
    Then the register button is disabled

  Scenario: To check register link is enabled when fields are all entered
    Given I am on the Registration screen
    When I have entered an email address
    And I have entered a password
    And I have entered a confirm password
    And I have entered an insurer code
    Then the register button is enabled

  Scenario: To check user can submit and is logged in successfully
    Given I am on the Registration screen
    And I have entered an email address
    And I have entered a password
    And I have entered a confirm password
    And I have entered an insurer code
    And The privacy policy is loaded
    When I tap Register
    Then I should be Registered in successfully

  Scenario: Invalid Password without number
    Given I am on the Registration screen
    When I have entered password with only alpha characters
    Then i should get an invalid password error message

  Scenario: Invalid Password without lowercase
    Given I am on the Registration screen
    When I have entered password with only uppercase
    Then i should get an invalid password error message

  Scenario: Invalid Password without uppercase
    Given I am on the Registration screen
    When I have entered password with only lowercase
    Then i should get an invalid password error message

  Scenario: Invalid Password with less than 7 characters
    Given I am on the Registration screen
    And I have entered password with less than 7 characters
    Then i should get an invalid password error message

#   ======Cannot check incorrect insurer=======
#  Scenario: invalid insurer code
#    Given I am on the Registration screen
#    When I have entered invalid insurer code
#    Then i should get an invalid insurer code error message

  Scenario: Already registered user
    Given I am on the Registration screen
    And I have entered a registered email address
    And I have entered a password
    And I have entered a confirm password
    And I have entered an insurer code
    And The privacy policy is loaded
    When I tap Register
    Then I should get an error message that I am already registered

#  Scenario: Register with short code
#    Given I am on the Registration screen
#    And I have entered a short code email
#    And I have entered a password
#    And I have entered a confirm password
#    And I have entered a short insurer code
#    And The privacy policy is loaded
#    When I tap Register
#    Then I should be Registered in successfully
