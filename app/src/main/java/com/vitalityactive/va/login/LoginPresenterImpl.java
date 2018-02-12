package com.vitalityactive.va.login;

import android.support.annotation.NonNull;

import com.pushwoosh.Pushwoosh;
import com.pushwoosh.notification.PushwooshNotificationSettings;
import com.pushwoosh.tags.TagsBundle;
import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.EnvironmentPrefix;
import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.register.entity.Password;
import com.vitalityactive.va.register.entity.Username;
import com.vitalityactive.va.utilities.TextUtilities;

public class LoginPresenterImpl implements LoginViewModel, LoginPresenter {
    private final EventDispatcher eventDispatcher;
    private LoginInteractor interactor;
    private UserInterface userInterface;
    private Username username;
    private final DeviceSpecificPreferences deviceSpecificPreferences;
    private Password password;
    private Scheduler scheduler;
    private volatile int loginAttempts;
    private boolean isUserInterfaceVisible;
    private boolean didShowLoginErrorMessage;
    private EventListener<AuthenticationFailedEvent> authenticationFailedEventEventListener;
    private EventListener<AuthenticationSucceededEvent> authenticationSucceededEventEventListener;

    public LoginPresenterImpl(EventDispatcher eventDispatcher,
                              LoginInteractor interactor,
                              final Scheduler scheduler,
                              DeviceSpecificPreferences deviceSpecificPreferences) {
        this.eventDispatcher = eventDispatcher;
        this.interactor = interactor;
        this.scheduler = scheduler;
        this.username = new Username(deviceSpecificPreferences.getRememberedUsername());
        this.deviceSpecificPreferences = deviceSpecificPreferences;

        password = new Password("");

        createEventListeners();
    }

    private void createEventListeners() {
        authenticationSucceededEventEventListener = new EventListener<AuthenticationSucceededEvent>() {
            @Override
            public void onEvent(AuthenticationSucceededEvent event) {
                
                if (BuildConfig.SHOW_PUSH_NOTIFICATIONS) {
                    // Register device for Pushwoosh notification
                    Pushwoosh.getInstance().registerForPushNotifications();
                    PushwooshNotificationSettings.setMultiNotificationMode(true);
                    TagsBundle.Builder tagsBuilder = new TagsBundle.Builder();
                    tagsBuilder.putString("Party ID", interactor.getPartyId());
                    tagsBuilder.putString("Tenant ID", interactor.getTenantId());
                    tagsBuilder.putString("Environment",deviceSpecificPreferences.getCurrentEnvironmentPrefix().replaceAll("-", ""));
                    Pushwoosh.getInstance().sendTags(tagsBuilder.build());
                }

                if (isUserInterfaceVisible) {
                    String oldUsername = deviceSpecificPreferences.getRememberedUsername();
                    String newUsername = username.getText().toString();

                    if (!oldUsername.equalsIgnoreCase(newUsername)) {
                        userInterface.deleteProfilePhoto();
                    }

                    navigateAfterSuccessfulLogin();
                }
            }
        };

        authenticationFailedEventEventListener = new EventListener<AuthenticationFailedEvent>() {
            @Override
            public void onEvent(final AuthenticationFailedEvent event) {
                scheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        incrementLoginAttempts(event);
                        if (isUserInterfaceVisible) {
                            userInterface.hideLoadingIndicator();
                            showLoginErrorMessage(event.getLoginRequestResult());
                        }
                    }
                });
            }
        };
    }

    private void incrementLoginAttempts(AuthenticationFailedEvent event) {
        if (event.getLoginRequestResult() == LoginInteractor.LoginRequestResult.INCORRECT_USERNAME_OR_PASSWORD) {
            ++loginAttempts;
        }
    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public boolean isLoginEnabled() {
        return interactor.areLoginCredentialsValid(username, password) && !TextUtilities.isNullOrWhitespace(password.getText());
    }

    public CharSequence getUsername() {
        return username.getText();
    }

    @Override
    public CharSequence getPassword() {
        return password.getText();
    }

    @Override
    public boolean shouldShowInvalidUsernameMessage() {
        return !username.isValid() && username.getText().length() != 0;
    }

    @Override
    public boolean shouldShowLoadingIndicator() {
        return interactor.isBusyLoggingIn();
    }

    @Override
    public LoginViewModel getViewModel() {
        return this;
    }

    @Override
    public void onUsernameChanged(@NonNull CharSequence usernameText) {
        username.setText(usernameText);
        userInterface.updateLoginEnabled(isLoginEnabled());
        if (username.isValid() || username.getText().length() == 0) {
            userInterface.hideInvalidUsernameMessage();
        }
    }

    @Override
    public void onPasswordChanged(@NonNull CharSequence passwordText) {
        password.setText(passwordText);
        userInterface.updateLoginEnabled(isLoginEnabled());
    }

    @Override
    public void onUsernameEntered() {
        if (username.isValid() || username.getText().length() == 0) {
            userInterface.hideInvalidUsernameMessage();
        } else {
            userInterface.showInvalidEmailAddressMessage();
        }
    }

    @Override
    public void setUsernameFromIntent(String username) {
        this.username.setText(username);
    }

    @Override
    public void onUserTriesToLogIn() {
        didShowLoginErrorMessage = false;
        userInterface.showLoadingIndicator();
        interactor.logIn(username, password);
    }

    private void addEventListeners() {
        eventDispatcher.addEventListener(AuthenticationSucceededEvent.class, authenticationSucceededEventEventListener);
        eventDispatcher.addEventListener(AuthenticationFailedEvent.class, authenticationFailedEventEventListener);
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        addEventListeners();
    }

    @Override
    public void onUserInterfaceAppeared() {

        isUserInterfaceVisible = true;

        interactor.setCurrentUserHasSeenOnboarding();

        if (shouldShowLoadingIndicator()) {
            userInterface.showLoadingIndicator();
            return;
        }

        userInterface.hideLoadingIndicator();

        if (!didShowLoginErrorMessage) {
            showLoginErrorMessage(interactor.getLoginRequestResult());
        }

        if (interactor.getLoginRequestResult() == LoginInteractor.LoginRequestResult.SUCCESSFUL) {
            userInterface.navigateAfterSuccessfulLogin();
        }
    }

    private void showLoginErrorMessage(LoginInteractor.LoginRequestResult loginRequestResult) {
        didShowLoginErrorMessage = true;
        switch (loginRequestResult) {
            case CONNECTION_ERROR:
                userInterface.showConnectionErrorMessage();
                break;
            case GENERIC_ERROR:
                userInterface.showGenericLoginErrorMessage();
                break;
            case INCORRECT_USERNAME_OR_PASSWORD:
                if (loginAttempts > 1) {
                    userInterface.showInvalidCredentialsLoginErrorMessageWithForgotPassword();
                } else {
                    userInterface.showInvalidCredentialsLoginErrorMessage();
                }
                break;
            case LOCKED_ACCOUNT:
                userInterface.showLockedAccountLoginErrorMessage();
                break;
        }
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        isUserInterfaceVisible = false;
        scheduler.cancel();
    }

    private void removeEventListeners() {
        eventDispatcher.removeEventListener(AuthenticationSucceededEvent.class, authenticationSucceededEventEventListener);
        eventDispatcher.removeEventListener(AuthenticationFailedEvent.class, authenticationFailedEventEventListener);
    }

    @Override
    public void onUserInterfaceDestroyed() {
        removeEventListeners();
    }

    private void navigateAfterSuccessfulLogin() {
        final String finalUserName = switchUserNameStringBaseURL(username.toString());
        deviceSpecificPreferences.setRememberedUsername(finalUserName);
        deviceSpecificPreferences.setRememberedPassword(password.toString());

        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                userInterface.navigateAfterSuccessfulLogin();
            }
        });
    }

    public void logInAfterRegistration(CharSequence usernameText, CharSequence passwordText) {
        username.setText(usernameText);
        password.setText(passwordText);
        interactor.logIn(username, password);
    }

    @NonNull
    private String switchUserNameStringBaseURL(@NonNull String username) {
        if (switchBaseURL(username, EnvironmentPrefix.DEV)) {
            return getNewUsernameWithoutPrefix(username, EnvironmentPrefix.DEV);
        }

        if (switchBaseURL(username, EnvironmentPrefix.TEST)) {
            return getNewUsernameWithoutPrefix(username, EnvironmentPrefix.TEST);
        }

        if (switchBaseURL(username, EnvironmentPrefix.CA_TEST)) {
            return getNewUsernameWithoutPrefix(username, EnvironmentPrefix.CA_TEST);
        }

        if (switchBaseURL(username, EnvironmentPrefix.QA)) {
            return getNewUsernameWithoutPrefix(username, EnvironmentPrefix.QA);
        }

        if (switchBaseURL(username, EnvironmentPrefix.QA_CA)) {
            return getNewUsernameWithoutPrefix(username, EnvironmentPrefix.QA_CA);
        }

        if (switchBaseURL(username, EnvironmentPrefix.EAGLE)) {
            return getNewUsernameWithoutPrefix(username, EnvironmentPrefix.EAGLE);
        }

        if (BuildConfig.FLAVOR.contains("ukessentials")) {
            if (switchBaseURL(username, EnvironmentPrefix.QA_FF)) {
                return getNewUsernameWithoutPrefix(username, EnvironmentPrefix.QA_FF);
            }
        }

        return username;
    }


    @NonNull
    private String getNewUsernameWithoutPrefix(@NonNull String username, @EnvironmentPrefix String prefix) {
        deviceSpecificPreferences.setCurrentEnvironmentPrefix(prefix);
        return username.replace(prefix, "");
    }

    private boolean switchBaseURL(@NonNull String username, String prefix) {
        return hasPrefix(username, prefix);
    }

    private boolean hasPrefix(@NonNull String username, String desiredPrefix) {
        return username.trim().toLowerCase().startsWith(desiredPrefix);
    }


}
