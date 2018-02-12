package com.vitalityactive.va.help;

import com.vitalityactive.va.BaseTest;
import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.help.presenter.HelpPresenter;
import com.vitalityactive.va.help.presenter.HelpPresenterImpl;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;

import retrofit2.Call;

/**
 * Created by christian.j.p.capin on 12/6/2017.
 */

public class HelpFragmentTest extends BaseTest {
    @Mock
    Call<String> mockCall;

    @Mock
    private HelpPresenter.UI userInterface;
    @Mock
    private EventDispatcher eventDispatcher;
    @Mock
    private MainThreadScheduler scheduler;
    private HelpPresenterImpl presenter;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        presenter= new HelpPresenterImpl(eventDispatcher, scheduler);
        presenter.setUserInterface(userInterface);
    }

    @Test
    public void is_suggestions_created() throws Exception {
        InOrder inOrder = Mockito.inOrder(eventDispatcher,scheduler);
    }
}
