@vhc
Feature: As a user i want to start VHC and learn more
  @test1
  Scenario: I want to start VHC
    Given I am on home screen
    When i tap VHC
    Then the VHC Onboarding screen is loaded

  Scenario: I want to learn more
    Given I am on the VHC Onboarding screen
    When I tap Learn More
    Then the VHC Learn More screen is loaded

  Scenario: I can go back to VHC landing screen from learn more screen
    Given I am on the VHC Onboarding screen
    When I tap Learn More
    And i tap on the Back button
    Then the VHC Onboarding screen is loaded

#  ======Participating Partners is not implemented yet=========
#  Scenario: I can view the participating partners
#    Given I am on the Learn More screen
#    When I tap on Participating Partner
#    Then I will see the Participating Partner screen

#  Scenario: I can read more about each participating partner
#    Given I am on the Learn More screen
#    And I tap on ‘Participating Partners’ Link
#    When I tap on a Participating Partner
#    Then more information about that partner should show

#  Scenario: I can go back form the participating partners screen to the learn more screen
#    Given I am on the Participating Partners screen
#    When I tap on Back
#    Then I am on the Learn More screen
#
#  Scenario: I can go back to the participating screen from the more information screen
#    Given I am on the Participating Partner information screen
#    When I tap on Back
#    Then I am on the Participating Partners screen

  Scenario: I can read more about 'Body Mass Index'
    Given I am on the VHC Onboarding screen
    And I tap Learn More
    When I tap Body Mass Index
    Then I will read more about Body Mass Index

  Scenario: I can read more about 'Waist Circumference'
    Given I am on the VHC Learn More screen
    When I tap Waist Circumference
    Then I will read more about Waist Circumference

  Scenario: I can read more about 'Blood Glucose'
    Given I am on the VHC Learn More screen
    When I tap Blood Glucose
    Then I will read more about Blood Glucose

  Scenario: I can read more about 'Blood Pressure'
    Given I am on the VHC Learn More screen
    When I tap Blood Pressure
    Then I will read more about Blood Pressure

  Scenario: I can read more about 'Cholesterol'
    Given I am on the VHC Learn More screen
    When I tap Cholesterol
    Then I will read more about Cholesterol

  Scenario: I can read more about 'HbA1c'
    Given I am on the VHC Learn More screen
    When I tap HbA1c
    Then I will read more about HbA1c

#  Scenario: I can go back to the Learn more screen from the more information screen
#    Given I am on the VHC Learn More screen
#    And I tap on one of the metrics
#    When I tap on Back
#    Then the VHC Learn More screen is loaded

  Scenario: I can view the learn more screen from the VHC landing screen
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    And I tap Learn More
    Then the VHC Learn More screen is loaded

  Scenario: I can go back to VHC onboarding screen from learn more screen
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    And I tap Learn More
    And i tap on the Back button
    Then the VHC Landing screen is loaded

  Scenario: navigate to the landing page
    Given I am on home screen
    When i tap VHC
    When I tap Got it
    Then the VHC Landing screen is loaded

  Scenario: navigate back to the onboarding screen
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on the Back button
    Then the home screen is loaded

  Scenario: I can view the help screen
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Help
    Then the help screen is loaded

  Scenario: I can go back to VHC onboarding screen from help screen
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Help
    When I tap on Back
    Then the VHC Landing screen is loaded

  Scenario: check user can Capture result
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    Then the VHC Capture screen is loaded

  Scenario: I can go back to VHC onboarding screen from capture screen
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    When I tap on Back
    Then the VHC Landing screen is loaded

#  Scenario: view more about captured bmi
#    Given I am on the VHC Landing screen
#    And I had previously captured BMI
#    When I tap on BMI
#    Then the BMI details screen is loaded
#
#  Scenario: view more about captured Waist Circumference
#    Given I am on the VHC Landing screen
#    And I had previously captured Waist Circumference
#    When I tap on Waist Circumference
#    Then the Waist Circumference details screen is loaded
#
#  Scenario: view more about captured blood pressure
#    Given I am on the VHC Landing screen
#    And I had previously captured blood pressure
#    When I tap on blood pressure
#    Then the blood pressure details screen is loaded
#
#  Scenario: view more about captured glucose
#    Given I am on the VHC Landing screen
#    And I had previously captured glucose
#    When I tap on glucose
#    Then the glucose details screen is loaded
#
#  Scenario: view more about captured HbA1c
#    Given I am on the VHC Landing screen
#    And I had previously captured HbA1c
#    When I tap on HbA1c
#    Then the HbA1c details screen is loaded

  Scenario: check if next is disabled
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    When I have not entered any value
    Then the Next button is disabled

  Scenario: check if next is enabled
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    When I have entered a value
    Then the Next button is enabled

  Scenario: check I can capture BMI
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    When I have enter correct value for weight
    And I have enter correct value for height
    Then the Next button is enabled

  Scenario: check I can capture WC
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    When I have enter correct value for WC
    Then the Next button is enabled

  Scenario: check I can capture Glucose
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    When I have enter correct value for FG
    And I have enter correct value for RG
    Then the Next button is enabled

  Scenario: check I can capture Blood Pressure
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    When I have enter correct value for DP
    And I have enter correct value for SP
    Then the Next button is enabled

  Scenario: check I can capture Cholesterol
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    When I have enter correct value for HDLC
    And I have enter correct value for Triglycerides
    And I have enter correct value for LDLC
    And I have enter correct value for Total
    Then the Next button is enabled

  Scenario: check I can capture HbA1c
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    When I have enter correct value for HbA1c
    Then the Next button is enabled

  Scenario: I can go to VHC Photo upload screen from capture screen
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    And I have entered required data
    When I tap on Next
    Then the VHC Photo Upload screen is loaded

  Scenario: Check the BMI one value warning weight only
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    And I have enter correct value for weight
    When I tap on Next
    Then the BMI one value is shown

  Scenario: Check the BMI one value warning height only
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    And I have enter correct value for height
    When I tap on Next
    Then the BMI one value is shown

  Scenario: cancel on BMI arlet
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    And I have seen the BMI alert
    When i tap cancel
    Then the VHC Capture screen is loaded

  Scenario: continue on BMI alert
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    And I have seen the BMI alert
    When i tap continue
    Then the VHC Photo upload screen is loaded

  Scenario: error for BMI out of range values
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    When I have entered an incorrect value for weight
    And I have entered an incorrect value for height
    Then error alert are shown

  Scenario: error for Waist Circumference out of range values
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    When I have entered an incorrect value for WC
    Then error alert are shown

  Scenario: error for Blood Glucose out of range values
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    When I have entered an incorrect value for FG
    And I have entered an incorrect value for RG
    Then error alert are shown

  Scenario: error for Blood Pessure out of range values
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    When I have entered an incorrect value for DP
    And I have entered an incorrect value for SP
    Then error alert are shown

  Scenario: error for Cholesterol out of range values
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    When I have enter correct value for HDLC
    And I have entered an incorrect value for Triglycerides
    And I have entered an incorrect value for LDLC
    And I have entered an incorrect value for Total
    Then error alert are shown

  Scenario: error for HbA1c out of range values
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    When I have entered an incorrect value for HbA1c
    Then terror alert are shown

  Scenario: check that the screen displays add button
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    And I have entered required data
    When i tap Next
    Then the VHC Photo Upload screen is loaded
    And I have seen the add button

  Scenario: tap on add to add proof
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    And I have entered required data
    When i tap Next
    When i tap add
    Then a pop up with option to take a picture is shown

  Scenario: check permissions to take a photo for first time users
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    And I have entered required data
    When i tap Next
    And i tap add
    And a pop up with option to take a picture is shown
    When I have never accepted permissions
    And i tap Take a Photo
    Then the pop up for permission is displayed

  Scenario: check permissions to take a photo if user had previously accepted permissions
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    And I have entered required data
    When i tap Next
    And i tap add
    And a pop up with option to take a picture is shown
    When I have accepted permissions before
    And i tap Take a Photo
    Then the camera is opened

  Scenario: agree to permissions to take a photo
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    And I have entered required data
    When i tap Next
    And i tap add
    And a pop up with option to take a picture is shown
    And I have never accepted permissions
    And i tap Take a Photo
    And the pop up for permission is displayed
    When i tap Agree
    Then the camera is opened

  Scenario: disagree to permissions
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    And I have entered required data
    When i tap Next
    And i tap add
    And a pop up with option to take a picture is shown
    And I have never accepted permissions
    And i tap Take a Photo
    And the pop up for permission is displayed
    When i tap disagree
    Then the VHC Photo Upload is loaded

  Scenario: check permissions to choose from Library for first time users
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    And I have entered required data
    When i tap Next
    And i tap add
    And a pop up with option to take a picture is shown
    When I have never accepted permissions
    And i tap choose from Library
    Then the pop up for permission is displayed

  Scenario: check permissions to choose from Library if user had previously accepted permissions
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    And I have entered required data
    When i tap Next
    And i tap add
    And a pop up with option to take a picture is shown
    When I have accepted permissions before
    And i tap choose from Library
    Then the library is opened

  Scenario: agree to permissions to choose from Library
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    And I have entered required data
    When i tap Next
    And i tap add
    And a pop up with option to take a picture is shown
    And I have never accepted permissions
    And i tap choose from Library
    And the pop up for permission is displayed
    When i tap Agree
    Then the Library is opened

  Scenario: disagree to permissions
    Given I am on home screen
    When i tap VHC
    And I tap Got it
    When I tap on Capture Result
    And I have entered required data
    When i tap Next
    And i tap add
    And a pop up with option to take a picture is shown
    And I have never accepted permissions
    And i tap choose from Library
    And the pop up for permission is displayed
    When i tap disagree
    Then the VHC Photo Upload is loaded


