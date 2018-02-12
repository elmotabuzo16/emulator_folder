package com.vitalityactive.va;

import android.support.test.InstrumentationRegistry;

import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.dependencyinjection.DefaultModule;
import com.vitalityactive.va.dto.PersonalDetailsDTO;
import com.vitalityactive.va.login.LoginRepositoryImpl;
import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.onboarding.LoginRepository;
import com.vitalityactive.va.register.entity.Username;
import com.vitalityactive.va.testutilities.annotations.RepositoryTests;

import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RepositoryTests
public class PartyInformationRepositoryTest extends RepositoryTestBase {

    public static final String USERNAME = "samcooper@gmail.com";

    private PartyInformationRepository repository;
    private LoginServiceResponse.PartyDetails partyDetails;
    private DeviceSpecificPreferences deviceSpecificPreferences;

    @Override
    public void setUp() throws IOException {
        super.setUp();
        setupLoginResponse();
        final DefaultModule defaultModule = new DefaultModule(MockJUnitRunner.getInstance().getApplication());
        deviceSpecificPreferences = defaultModule.provideDeviceSpecificPreferences();
        repository = new PartyInformationRepositoryImpl(dataStore, deviceSpecificPreferences);
    }

    private void setupLoginResponse() throws IOException {
        LoginRepository loginRepository = new LoginRepositoryImpl(dataStore, new AppConfigRepository(dataStore, InstrumentationRegistry.getTargetContext()));
        LoginServiceResponse response = getResponse(LoginServiceResponse.class, "login/Login_successful_with_party_details.json");
        partyDetails = response.partyDetails;
        boolean persisted = loginRepository.persistLoginResponse(response, new Username(USERNAME));
        assertTrue(persisted);
    }

    @Test
    public void should_return_tenant_id() throws Exception {
        assertThat(repository.getTenantId(), is(partyDetails.tenantId));
    }

    @Test
    public void should_return_party_id() throws Exception {
        assertThat(repository.getPartyId(), is(partyDetails.partyId));
    }

    @Test
    public void should_return_username() throws Exception {
        assertThat(repository.getUsername(), is(USERNAME));
    }

    @Test
    public void should_return_user_initials() throws Exception {
        assertThat(repository.getUserInitials(), is("S"));
    }

    @Test
    public void should_return_user_given_name() throws Exception {
        assertThat(repository.getUserGivenName(), is(partyDetails.person.givenName));
    }

    @Test
    public void should_return_user_family_name() throws Exception {
        assertThat(repository.getUserFamilyName(), is(partyDetails.person.familyName));
    }

    @Test
    public void should_return_email_address() throws Exception {
        assertThat(repository.getEmailAddress(), is(partyDetails.emailAddresses.get(0).value));
    }

    @Test
    public void should_return_personal_details() throws Exception {
        PersonalDetailsDTO details = repository.getPersonalDetails();
        assertThat(details.getGivenName(), is(partyDetails.person.givenName));
        assertThat(details.getFamilyName(), is(partyDetails.person.familyName));
        assertThat(details.getInitials(), is("S"));
    }
}