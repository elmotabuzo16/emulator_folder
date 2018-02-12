package com.vitalityactive.va.register;

import com.vitalityactive.va.register.interactor.RegistrationInteractor;
import com.vitalityactive.va.register.presenter.CredentialPresenterBase;
import com.vitalityactive.va.register.presenter.CredentialPresenterCallback;
import com.vitalityactive.va.register.presenter.InsurerCodePresenter;
import com.vitalityactive.va.register.presenter.PasswordPresenter;
import com.vitalityactive.va.register.presenter.UsernamePresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class CredentialPresenterEnableRegistrationTest extends CredentialPresenterTestBase
{
    private final CredentialPresenterBase.CredentialPresenterBaseBuilder builder;
    private final String value;

    public CredentialPresenterEnableRegistrationTest(CredentialPresenterBase.CredentialPresenterBaseBuilder builder, String value)
    {
        this.builder = builder;
        this.value = value;
    }

    @Parameterized.Parameters
    public static Iterable<Object[]> data()
    {
        return Arrays.asList(new Object[][]{
                {new PasswordPresenter.Builder(), VALID_PASSWORD},
                {new UsernamePresenter.Builder(), VALID_USERNAME},
                {new InsurerCodePresenter.Builder(), VALID_INSURER_CODE},
        });
    }

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        createRegistrationInteractor();
    }

    @Test
    public void itDoesNotTryToEnableRegistrationIfOtherFieldsAreInvalid()
    {
        RegistrationInteractor mockRegistrationInteractor = mock(RegistrationInteractor.class);
        when(mockRegistrationInteractor.canRegister()).thenReturn(false);

        presenter = builder
                .setRegistrationInteractor(mockRegistrationInteractor)
                .setRegistrationUserInterface(mockUserInterface)
                .build();

        presenter.onValueChanged(value);
        verify(mockUserInterface, never()).allowRegistration();
    }

    @Test
    public void itDisablesRegistrationIfAnInvalidValueIsSet()
    {
        presenter = builder
                .setRegistrationInteractor(registrationInteractor)
                .setRegistrationUserInterface(mockUserInterface)
                .build();

        presenter.onValueChanged(value);

        if (registrationInteractor.canRegister()) {
            verify(mockUserInterface).allowRegistration();
        } else {
            verify(mockUserInterface).disallowRegistration();
        }
    }

    @Test
    public void itNotifiesItsCallbackWhenItsValueChanges()
    {
        CredentialPresenterCallback mockCallback = mock(CredentialPresenterCallback.class);
        presenter = builder
                .setRegistrationUserInterface(mockUserInterface)
                .setCallback(mockCallback)
                .build();

        presenter.onValueChanged(value);
        verify(mockCallback).onCredentialChanged();
    }
}
