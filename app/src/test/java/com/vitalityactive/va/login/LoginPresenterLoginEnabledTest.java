package com.vitalityactive.va.login;

import com.vitalityactive.va.register.entity.Password;
import com.vitalityactive.va.register.entity.Username;
import com.vitalityactive.va.utilities.TextUtilities;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.InOrder;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class LoginPresenterLoginEnabledTest extends LoginPresenterTestBase
{
    private static final boolean LOGIN_ENABLED = true;
    private static final boolean LOGIN_DISABLED = false;
    private final boolean doesServiceReportLoginCredentialsValid;
    private final String usernameText;
    private final String passwordText;

    public LoginPresenterLoginEnabledTest(String usernameText, String passwordText, boolean doesServiceReportLoginCredentialsValid)
    {
        this.usernameText = usernameText;
        this.passwordText = passwordText;
        this.doesServiceReportLoginCredentialsValid = doesServiceReportLoginCredentialsValid;
    }

    @Parameterized.Parameters
    public static Iterable<Object[]> data()
    {
        return Arrays.asList(new Object[][]{
                {"a", "1", LOGIN_DISABLED},
                {"aasdfasSDFAASD121", "1asdfasfwe2e", LOGIN_DISABLED},
                {"", "", LOGIN_DISABLED},
                {"   ", "   ", LOGIN_DISABLED},
                {"", "", LOGIN_DISABLED},
                {"username@blarg.com", "", LOGIN_DISABLED},
                {"valid@email.address", "someRandomPassword", LOGIN_ENABLED},
        });
    }

    @Test
    public void isLoginCorrectlyEnabled()
    {
        checkViewModel("", "", false);

        when(mockLoginInteractor.areLoginCredentialsValid(any(Username.class), any(Password.class))).thenReturn(doesServiceReportLoginCredentialsValid);

        InOrder inOrder = inOrder(mockUserInterface, mockUserInterface);

        loginPresenter.onUsernameChanged(usernameText);

        if (usernameText.length() == 0 || new Username(usernameText).isValid()) {
            verify(mockUserInterface).hideInvalidUsernameMessage();
        }

        inOrder.verify(mockUserInterface).updateLoginEnabled(false);
        checkViewModel(usernameText, "", false);

        loginPresenter.onPasswordChanged(passwordText);

        inOrder.verify(mockUserInterface).updateLoginEnabled(isLoginEnabled());
        checkViewModel(usernameText, passwordText, isLoginEnabled());
    }

    private boolean isLoginEnabled()
    {
        return doesServiceReportLoginCredentialsValid && !TextUtilities.isNullOrWhitespace(passwordText);
    }

    private void checkViewModel(String emailAddressText, String passwordText, boolean isLoginEnabled)
    {
        assertEquals(emailAddressText, loginPresenter.getViewModel().getUsername());
        assertEquals(passwordText, loginPresenter.getViewModel().getPassword());
        assertEquals(isLoginEnabled, loginPresenter.getViewModel().isLoginEnabled());
    }
}
