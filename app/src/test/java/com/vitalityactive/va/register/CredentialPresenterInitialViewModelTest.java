package com.vitalityactive.va.register;

import com.vitalityactive.va.R;
import com.vitalityactive.va.register.interactor.RegistrationInteractor;
import com.vitalityactive.va.register.presenter.ConfirmationPasswordPresenter;
import com.vitalityactive.va.register.presenter.CredentialPresenter;
import com.vitalityactive.va.register.presenter.CredentialPresenterBase;
import com.vitalityactive.va.register.presenter.InsurerCodePresenter;
import com.vitalityactive.va.register.presenter.PasswordPresenter;
import com.vitalityactive.va.register.presenter.UsernamePresenter;
import com.vitalityactive.va.register.view.CredentialUserInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class CredentialPresenterInitialViewModelTest extends CredentialPresenterTestBase
{
    private final CredentialPresenterBase.CredentialPresenterBaseBuilder builder;
    private final CharSequence value;
    private final boolean canRegister;
    private CredentialPresenter presenter;

    @Mock
    private RegistrationInteractor mockRegistrationInteractor;

    public CredentialPresenterInitialViewModelTest(CredentialPresenterBase.CredentialPresenterBaseBuilder builder, CharSequence value, boolean canRegister)
    {
        this.builder = builder;
        this.value = value;
        this.canRegister = canRegister;
    }

    @Parameterized.Parameters
    public static Iterable<Object[]> data()
    {
        return Arrays.asList(new Object[][]{
                {new PasswordPresenter.Builder(), "asdf", true},
                {new UsernamePresenter.Builder(), "Password1", false},
                {new ConfirmationPasswordPresenter.Builder(), "whatever", true},
                {new InsurerCodePresenter.Builder(), "asdfasdf3433LASFKSD", false},
        });
    }

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        presenter = builder
                .setRegistrationInteractor(mockRegistrationInteractor)
                .setRegistrationUserInterface(mockUserInterface)
                .setHint(R.string.UKE_activate_authentication_code_355)
                .setValidationMessage("Some random message")
                .setIconResourceId(123465436)
                .setInputType(563434)
                .build();

        when(mockRegistrationInteractor.getUsername()).thenReturn(new RegistrationInteractor.Username() {
            @Override
            public CharSequence getText() {
                return value;
            }

            @Override
            public boolean isValid() {
                return false;
            }
        });
        when(mockRegistrationInteractor.getPassword()).thenReturn(new RegistrationInteractor.Password() {
            @Override
            public CharSequence getText() {
                return value;
            }

            @Override
            public boolean isValid() {
                return false;
            }
        });
        when(mockRegistrationInteractor.getConfirmationPassword()).thenReturn(new RegistrationInteractor.ConfirmationPassword() {
            @Override
            public CharSequence getText() {
                return value;
            }

            @Override
            public boolean isValid() {
                return false;
            }
        });
        when(mockRegistrationInteractor.getInsurerCode()).thenReturn(new RegistrationInteractor.InsurerCode() {
            @Override
            public CharSequence getText() {
                return value;
            }

            @Override
            public boolean isValid() {
                return false;
            }
        });

        when(mockRegistrationInteractor.canRegister()).thenReturn(canRegister);
    }

    @Test
    public void viewModelIsCorrectWhenInitiallyShown()
    {
        int expectedHint = R.string.UKE_activate_authentication_code_355;
        String expectedValidationMessage = "Some random message";
        int expectedIconResourceId = 123465436;
        int expectedInputType = 563434;

        assertFalse(presenter.shouldShowValidationErrorMessage());
        assertEquals(expectedHint, presenter.getHintResourceId());
        assertEquals(expectedValidationMessage, presenter.getValidationMessage());
        assertEquals(expectedIconResourceId, presenter.getIconResourceId());
        assertEquals(expectedInputType, presenter.getInputType());
    }

    @Test
    public void user_interface_is_configured_correctly()
    {
        CredentialUserInterface credentialUserInterface = mock(CredentialUserInterface.class);
        presenter.bindWith(credentialUserInterface);
        verify(credentialUserInterface).setValue(value);
        if (canRegister) {
            verify(mockUserInterface).allowRegistration();
        } else {
            verify(mockUserInterface).disallowRegistration();
        }
    }
}
