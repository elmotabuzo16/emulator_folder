# Android Cucumber tests

## Adding new features

in `app/src/androidTest/assets/features` add a new `.feature` file

## Screens

Screens link back to an Android activity. The screen has activity-specific actions (for example the `LoginScreen` has `enterUsername`) and activity-specific checks (for example the `LoginScreen` has `checkUserValidationErrorMessageShown`)

### BaseScreen

Screens inherit from `BaseScreen` (`com.vitalityactive.va.cucumber.screens`) which provides generic actions (for example `clickOnButtonWithText`) and checks (for example `checkButtonIsEnabled`)

It also provides an `is(Class<T>)` method for checking that the screen object is on a specific screen, and casts it to that screen

### Mappings

Add the mapping for new screens to the name of the screen and the Android activity in `StepDefinitions`

`mapping.put("terms and conditions", new ScreenMapping(TermsAndConditionsActivity.class, TermsAndConditionsScreen.class));`

## StepDefinitions

The step definitions are all added in a single file, `app/src/androidTest/java/com/vitalityactive/va/cucumber/StepDefinitions.java`

Cucumber does not allow sub-classing, but can split it to multiple files if need be. However this is the test case that will be run by the test runner, so all the setup/teardown code is here already (and will need to be duplicated if multiple step definition files are used)

### Steps

The step definitions self are methods, annotated by `@Given`, `@When`, `@Then` and a regular expression that matches the strings in the feature files

For example:

```
Given ...
And I have entered all the registration details
```

links to

```
@Given("^I have entered all the registration details$")
public void enterAllRegistrationDetails() {
    ...
}
```

#### Capturing groups in regular expression

The regex can also include capturing groups, that will (must) be supplied to the method as parameters (not sure if it is only `String` or if it will try to cast the values)

For example:
```
Given ...
And I am on the Registration screen
```

links to

```
@Given("^I am on the (.+) screen$")
public void iAmOnAScreen(String screenName) ... {
    ...
}
```

#### Performing actions and checks on screens

The `StepDefinitions` has a `BaseScreen` field, `currentScreen`

For each step, either perform generic actions or do generic tests directly on the `currentScreen`, for example:

```
@When("^I click in the (.+) field$")
public void clickOnField(String field) {
    currentScreen.focusOnField(field);
}
```

or check that the screen is the expected screen (expected for the step definition) and call the screen's specific actions or checks.

Entering a password for example is expected to be on the login screen, and the login screen has the specific action method:

```
@When("^I have entered a correct password$")
public void enterCorrectPassword() {
    currentScreen = currentScreen.is(LoginScreen.class).enterPassword(TestData.PASSWORD);
}
```

## Running tests

Run the gradle task `UITest VitalityActiveDebug`

You can also run all the tests in `com.vitalityactive.va` (right click on androidTest, select "Run 'Tests in com.vitalityactive.va...") This only works for the `vitalityActivedebug` build variant

### Running specific tests

You can tag features or scenarios with `@tagName` in the `.feature` files

To run only specified tags, change your `app/buildSettings.json` and add/set the `gherkinTags` string value to the tags to run (with the @ from the tag name)

## Mock network handler

See `MockNetworkHandler.java`, and `enqueueLoginResponse` for example higher level mocking

This class mocks out the network requests using `okhttp3.mockwebserver.MockWebServer`

It is started and ended in the `StepDefinition`'s `setUp` and `tearDown`
