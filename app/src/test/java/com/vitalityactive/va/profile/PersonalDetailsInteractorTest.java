package com.vitalityactive.va.profile;


import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.cms.CMSContentUploader;
import com.vitalityactive.va.dependencyinjection.DefaultModule;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.login.LoginRepositoryImpl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PersonalDetailsInteractorTest {

    @Mock
    private PersonalDetailsClient personalDetailsClient;
    @Mock
    private EventDispatcher eventDispatcher;
    @Mock
    private PartyInformationRepository partyInformationRepository;
    @Mock
    private CMSContentUploader cmsContentUploader;
    PersonalDetailsInteractor interactor;
    @Mock
    private AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;
    @Mock
    private DeviceSpecificPreferences deviceSpecificPreferences;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        interactor = new PersonalDetailsInteractorImpl(personalDetailsClient, eventDispatcher, partyInformationRepository, cmsContentUploader, accessTokenAuthorizationProvider, deviceSpecificPreferences);
    }

    @Test
    public void should_add_photo_when_all_params_are_null(){
        interactor.addProfilePhoto(null,null,null,null);
        verify(eventDispatcher).dispatchEvent(refEq(new ProfileImageAvailableEvent()));
    }

    @Test
    public void should_change_email_when_all_params_are_null(){
        interactor.changeEmail(null,null, null);
        verify(eventDispatcher).dispatchEvent(refEq(new PersonalDetailsEvent(PersonalDetailsEvent.EventType.CHANGE_EMAIL_SUCCESS)));
    }
}
