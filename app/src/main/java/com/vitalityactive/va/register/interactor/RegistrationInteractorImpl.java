package com.vitalityactive.va.register.interactor;

import android.support.annotation.NonNull;

import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.login.BaseURLSwitcher;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.register.RegistrationServiceClient;
import com.vitalityactive.va.register.entity.RegistrationCredentials;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RegistrationInteractorImpl implements RegistrationInteractor, WebServiceResponseParser<String> {
    private final DeviceSpecificPreferences preferences;
    private final RegistrationCredentials registrationCredentials;
    private final EventDispatcher eventDispatcher;
    private final RegistrationServiceClient registrationServiceClient;
    private RegistrationRequestResult registrationRequestResult = RegistrationRequestResult.NONE;
    private BaseURLSwitcher baseURLSwitcher;

    @Inject
    public RegistrationInteractorImpl(DeviceSpecificPreferences preferences, EventDispatcher eventDispatcher, RegistrationServiceClient registrationServiceClient, BaseURLSwitcher baseURLSwitcher) {
        this.preferences = preferences;
        this.eventDispatcher = eventDispatcher;
        this.registrationServiceClient = registrationServiceClient;
        this.baseURLSwitcher = baseURLSwitcher;
        registrationCredentials = new RegistrationCredentials();
    }

    public void setUsername(@NonNull CharSequence username) {
        registrationCredentials.setUsername(username);
    }

    public void setPassword(@NonNull CharSequence password) {
        registrationCredentials.setPassword(password);
    }

    public void setConfirmationPassword(@NonNull CharSequence confirmationPassword) {
        registrationCredentials.setConfirmationPassword(confirmationPassword);
    }

    public void setInsurerCode(@NonNull CharSequence insurerCode) {
        registrationCredentials.setInsurerCode(insurerCode);
    }

    public boolean isUsernameValid() {
        return registrationCredentials.isUsernameValid();
    }

    @Override
    public boolean isPasswordValid() {
        return registrationCredentials.isPasswordValid();
    }

    @Override
    public boolean isInsurerCodeValid() {
        return registrationCredentials.isInsurerCodeValid();
    }

    @Override
    public boolean canRegister() {
        return registrationCredentials.canRegister();
    }

    @Override
    public boolean passwordsDiffer() {
        return registrationCredentials.passwordsDiffer();
    }

    @Override
    public void register() {
        if (!isRegistering() && canRegister()) {
            com.vitalityactive.va.register.entity.Username username = baseURLSwitcher.switchBaseURL(
                    new com.vitalityactive.va.register.entity.Username(preferences.getCurrentEnvironmentPrefix() + registrationCredentials.getUsername())
            );
            registrationServiceClient.register(this, new RegistrationCredentials(username, registrationCredentials.getPassword(), registrationCredentials.getInsurerCode()));
        }
    }

    @Override
    public Username getUsername() {
        return new Username() {
            @Override
            public CharSequence getText() {
                return registrationCredentials.getUsername().getText();
            }

            @Override
            public boolean isValid() {
                return registrationCredentials.isUsernameValid();
            }
        };
    }

    @Override
    public Password getPassword() {
        return new Password() {
            @Override
            public CharSequence getText() {
                return registrationCredentials.getPassword().getText();
            }

            @Override
            public boolean isValid() {
                return registrationCredentials.isPasswordValid();
            }
        };
    }

    @Override
    public ConfirmationPassword getConfirmationPassword() {
        return new ConfirmationPassword() {
            @Override
            public CharSequence getText() {
                return registrationCredentials.getConfirmationPassword().getText();
            }

            @Override
            public boolean isValid() {
                return registrationCredentials.isConfirmationPasswordValid();
            }
        };
    }

    @Override
    public InsurerCode getInsurerCode() {
        return new InsurerCode() {
            @Override
            public CharSequence getText() {
                return registrationCredentials.getInsurerCode().getText();
            }

            @Override
            public boolean isValid() {
                return registrationCredentials.isInsurerCodeValid();
            }
        };
    }

    @Override
    public boolean isRegistering() {
        return registrationServiceClient.isRegistering();
    }

    public synchronized RegistrationRequestResult getRegistrationRequestResult() {
        return registrationRequestResult;
    }

    @Override
    public void parseResponse(String body) {
        setRegistrationRequestResult(RegistrationRequestResult.SUCCESSFUL);
        eventDispatcher.dispatchEvent(new RegistrationSucceededEvent());
    }

    @Override
    public void parseErrorResponse(String errorBody, int code) {
        RegistrationRequestResult requestResult;
        switch (code) {
            case 400:
            case 404:
                requestResult = RegistrationRequestResult.INVALID_EMAIL_INSURER_CODE_ERROR;
                break;
            case 409:
                requestResult = RegistrationRequestResult.ALREADY_REGISTERED_ERROR;
                break;
            default:
                requestResult = RegistrationRequestResult.GENERIC_ERROR;
                break;
        }
        onRequestCompleted(requestResult);
    }

    @Override
    public void handleGenericError(Exception exception) {
        onRequestCompleted(RegistrationRequestResult.GENERIC_ERROR);
    }

    @Override
    public void handleConnectionError() {
        onRequestCompleted(RegistrationRequestResult.CONNECTION_ERROR);
    }

    private void onRequestCompleted(RegistrationRequestResult requestResult) {
        setRegistrationRequestResult(requestResult);
        eventDispatcher.dispatchEvent(new RegistrationFailedEvent(requestResult));
    }

    private synchronized void setRegistrationRequestResult(RegistrationRequestResult registrationRequestResult) {
        this.registrationRequestResult = registrationRequestResult;
    }
}
