package com.vitalityactive.va.login;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(Parameterized.class)
public class LoginPresenterShowInvalidUsernameMessageTest extends LoginPresenterTestBase
{
    private static final boolean SHOW_MESSAGE = true;
    private static final boolean HIDE_MESSAGE = false;
    private final String usernameText;
    private final boolean showMessage;

    public LoginPresenterShowInvalidUsernameMessageTest(String usernameText, boolean showMessage)
    {
        this.usernameText = usernameText;
        this.showMessage = showMessage;
    }

    @Parameterized.Parameters
    public static Iterable<Object[]> data()
    {
        return Arrays.asList(new Object[][]{
                {"a", SHOW_MESSAGE},
                {"abc@ab.cc", HIDE_MESSAGE},
                {"", HIDE_MESSAGE},
                {"    ", SHOW_MESSAGE},
        });
    }

    @Test
    public void invalidUsernameMessageIsCorrectlyToggledAfterUsernameIsSet()
    {
        assertFalse(loginPresenter.getViewModel().shouldShowInvalidUsernameMessage());

        loginPresenter.onUsernameChanged(usernameText);
        loginPresenter.onUsernameEntered();

        if (showMessage) {
            verify(mockUserInterface).showInvalidEmailAddressMessage();
        } else {
            verify(mockUserInterface, times(2)).hideInvalidUsernameMessage();
        }

        assertEquals(showMessage, loginPresenter.getViewModel().shouldShowInvalidUsernameMessage());
    }
}
