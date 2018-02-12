package com.vitalityactive.va.login;

import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;
import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.MockJUnitRunner;
import com.vitalityactive.va.PartyInformationRepositoryImpl;
import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.constants.UserInstructions;
import com.vitalityactive.va.dependencyinjection.DefaultModule;
import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.onboarding.LoginRepository;
import com.vitalityactive.va.persistence.models.CurrentVitalityMembershipPeriod;
import com.vitalityactive.va.persistence.models.EmailAddress;
import com.vitalityactive.va.persistence.models.GeneralPreference;
import com.vitalityactive.va.persistence.models.GeographicalAreaPreference;
import com.vitalityactive.va.persistence.models.InsurerConfiguration;
import com.vitalityactive.va.persistence.models.LanguagePreference;
import com.vitalityactive.va.persistence.models.MeasurementSystemPreference;
import com.vitalityactive.va.persistence.models.ProductFeature;
import com.vitalityactive.va.persistence.models.Reference;
import com.vitalityactive.va.persistence.models.StreetAddress;
import com.vitalityactive.va.persistence.models.Telephone;
import com.vitalityactive.va.persistence.models.TimeZonePreference;
import com.vitalityactive.va.persistence.models.User;
import com.vitalityactive.va.persistence.models.UserInstruction;
import com.vitalityactive.va.persistence.models.WebAddress;
import com.vitalityactive.va.register.entity.Username;
import com.vitalityactive.va.testutilities.ModelVerifier;
import com.vitalityactive.va.testutilities.annotations.RepositoryTests;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SmallTest
@RepositoryTests
public class LoginRepositoryTest extends RepositoryTestBase {

    private static final String USERNAME = "username@server.com";
    private LoginRepository repository;
    private DeviceSpecificPreferences deviceSpecificPreferences;

    public void setUp() throws IOException {
        super.setUp();
        final DefaultModule defaultModule = new DefaultModule(MockJUnitRunner.getInstance().getApplication());
        repository = new LoginRepositoryImpl(dataStore, new AppConfigRepository(dataStore, InstrumentationRegistry.getTargetContext()));
        deviceSpecificPreferences = defaultModule.provideDeviceSpecificPreferences();
    }

    @Test
    public void login_response_is_persisted_correctly() throws Exception {
        LoginServiceResponse response = getResponse(LoginServiceResponse.class, "login/Login_successful.json");
        boolean persisted = repository.persistLoginResponse(response, new Username(USERNAME));

        assertTrue(persisted);
        verifyUser();
        verifyInsurerConfiguration();
        verifyGeneralPreferences();
        verifyReferences();
        verifyMeasurementSystemPreference();
        verifyTelephones();
        verifyTimeZonePreference();
        verifyWebAddresses();
        verifyGeographicalAreaPreference();
        verifyLanguagePreference();
        verifyEmailAddresses();
        verifyPhysicalAddresses();
        verifyUserInstructions();
        verifyAppConfigFeatures();
        verifyProductFeatures();
        verifyCurrentMembershipPeriod();
        verifyAppConfigVersion();
    }

    @Test
    public void parsing_succeeds_if_membership_products_is_missing() throws Exception {
        assertLoginSucceedsWithoutProductFeatures("login/Login_membershipProducts_missing.json");
    }

    @Test
    public void parsing_succeeds_if_membership_products_is_empty() throws Exception {
        assertLoginSucceedsWithoutProductFeatures("login/Login_membershipProducts_empty.json");
    }

    @Test
    public void parsing_succeeds_if_product_feature_applicabilities_is_missing() throws Exception {
        assertLoginSucceedsWithoutProductFeatures("login/Login_productFeatureApplicabilities_missing.json");
    }

    @Test
    public void parsing_succeeds_if_product_feature_applicabilities_is_empty() throws Exception {
        assertLoginSucceedsWithoutProductFeatures("login/Login_productFeatureApplicabilities_empty.json");
    }

    @Test
    public void parsing_fails_if_app_config_is_missing() throws Exception {
        assertNothingIsPersisted("login/Login_missing_app_config.json");
    }

    @Test
    public void username_is_persisted_correctly() throws IOException {
        repository.persistLoginResponse(getResponse(LoginServiceResponse.class, "login/Login_successful.json"), new Username(USERNAME));
        final PartyInformationRepositoryImpl partyInformationRepository = new PartyInformationRepositoryImpl(dataStore, deviceSpecificPreferences);
        String persistedUsername = partyInformationRepository.getUsername();

        assertEquals(USERNAME, persistedUsername);
    }

    @Test
    public void if_user_cannot_be_persisted_nothing_is_persisted() throws Exception {
        assertNothingIsPersisted("login/Login_no_partyId.json");
    }

    @Test
    public void if_insurer_configuration_cannot_be_persisted_nothing_is_persisted() throws Exception {
        assertNothingIsPersisted("login/Login_no_tenantId.json");
    }

    @Test
    public void if_app_config_has_missing_fields_for_insurer_nothing_is_persisted() throws Exception {
        assertNothingIsPersisted("login/Login_missing_app_config_field.json");
    }

    @Test
    public void if_something_else_cannot_be_persisted_the_rest_are_still_persisted() throws Exception {
        assertTrue(repository.persistLoginResponse(getResponse(LoginServiceResponse.class, "login/Login_missing_email_addresses.json"), new Username(USERNAME)));

        verifyUser();
        verifyInsurerConfiguration();
        verifyReferences();
        verifyGeneralPreferences();
        verifyMeasurementSystemPreference();
        verifyTelephones();
        verifyTimeZonePreference();
        verifyWebAddresses();
        verifyGeographicalAreaPreference();
        verifyLanguagePreference();
        verifyPhysicalAddresses();
        verifyUserInstructions();
        assertModelIsNotCreated(EmailAddress.class);
    }

    @Test
    public void login_response_without_body_is_handled_correctly() throws Exception {
        assertFalse(repository.persistLoginResponse(getResponse(LoginServiceResponse.class, "login/Login_no_response_body.json"), null));

        assertUserModelIsNotCreated();
    }

    @Test
    public void login_response_without_partyId_is_handled_correctly() throws Exception {
        assertFalse(repository.persistLoginResponse(getResponse(LoginServiceResponse.class, "login/Login_no_partyId.json"), null));

        assertUserModelIsNotCreated();
    }

    private void verifyCurrentMembershipPeriod() throws Exception {
        new ModelVerifier<>(CurrentVitalityMembershipPeriod.class, getRealm(), 1, new ModelVerifier.Verifier<CurrentVitalityMembershipPeriod>() {
            @Override
            public void verifyModel(CurrentVitalityMembershipPeriod model, int index) throws Exception {
                assertEquals("2017-01-01", model.getEffectiveFrom());
                assertEquals("2017-12-31", model.getEffectiveTo());
            }
        });
    }

    private void verifyProductFeatures() throws Exception {
        new ProductFeatureModelVerifier(ProductFeature.class, getRealm(), 27, new ModelVerifier.Verifier<ProductFeature>() {
            @Override
            public void verifyModel(ProductFeature model, int index) throws Exception {
                if (index == 0) {
                    assertEquals(1, model.getType());
                    assertEquals(7, model.getFeatureType());
                    assertEquals("2017-01-01", model.getEffectiveFrom());
                    assertEquals("9999-12-31", model.getEffectiveTo());
                    assertEquals(88, model.getFeatureLinks().get(0).getLinkedKey());
                    assertEquals(1, model.getFeatureLinks().get(0).getTypeKey());
                    assertNotNull(model.getFeatureLinks().get(0).getProductFeature());
                } else if (index == 6) {
                    assertTrue(model.getFeatureLinks().isEmpty());
                }
            }
        });
    }

    private void verifyAppConfigFeatures() throws Exception {
        AppConfigRepositoryTest.verifyAppConfigFeatures(getRealm());
    }

    private void verifyUser() throws Exception {
        new ModelVerifier<>(User.class, getRealm(), 1, new ModelVerifier.Verifier<User>() {
            @Override
            public void verifyModel(User user, int index) throws Exception {
                assertEquals(100002, user.getPartyId());
                assertEquals("asdfs#$sdwewe12134fgdWEHT--", user.getPartnerRefreshToken());
                assertEquals("1e2e8620-8ba4-3842-9de7-d7ed0a761c20", user.getAccessToken());
                assertEquals("Frikkie", user.getGivenName());
                assertEquals("Poggenpoel", user.getFamilyName());
                assertEquals("Petrus", user.getMiddleNames());
                assertEquals("1", user.getGender());
                assertEquals("ENG", user.getLanguage());
                assertEquals("2016-11-21", user.getDateOfBirth());
                assertEquals("Janneman", user.getPreferredName());
                assertEquals("1", user.getTitle());
                assertEquals("Jr.", user.getSuffix());
                assertEquals("100001", user.getVitalityMembershipId());
            }
        });
    }

    private void verifyInsurerConfiguration() throws Exception {
        new ModelVerifier<>(InsurerConfiguration.class, getRealm(), 1, new ModelVerifier.Verifier<InsurerConfiguration>() {
            @Override
            public void verifyModel(InsurerConfiguration model, int index) throws Exception {
                assertEquals(2, model.getTenantId());
            }
        });
    }

    private void verifyReferences() throws Exception {
        new ReferenceModelVerifier(Reference.class, getRealm(), 2, new ModelVerifier.Verifier<Reference>() {
            @Override
            public void verifyModel(Reference reference, int index) throws Exception {
                LoginServiceResponse.Reference response = getResponse(LoginServiceResponse.class, "login/Login_successful.json").partyDetails.references.get(index);
                assertEquals(reference.getEffectiveFrom(), response.effectiveFrom);
                assertEquals(reference.getEffectiveTo(), response.effectiveTo);
                assertEquals(reference.getIssuedBy(), response.issuedBy.toString());
                assertEquals(reference.getType(), response.type);
                assertEquals(reference.getValue(), response.value);
            }
        });
    }

    private void verifyGeneralPreferences() {
        ModelVerifier<GeneralPreference> modelVerifier = new ModelVerifier<>(GeneralPreference.class, getRealm());
        modelVerifier.assertOneModel();

        GeneralPreference generalPreference = modelVerifier.getModel();
        assertEquals("78564", generalPreference.getType());
        assertEquals("42", generalPreference.getValue());
        assertEquals("1648-05-15", generalPreference.getEffectiveFrom());
        assertEquals("3145-05-06", generalPreference.getEffectiveTo());
    }

    private void verifyMeasurementSystemPreference() {
        ModelVerifier<MeasurementSystemPreference> modelVerifier = new ModelVerifier<>(MeasurementSystemPreference.class, getRealm());
        modelVerifier.assertOneModel();

        MeasurementSystemPreference measurementSystemPreference = modelVerifier.getModel();
        assertEquals("12", measurementSystemPreference.getType());
        assertEquals("Stop changing everything constantly", measurementSystemPreference.getName());
    }

    private void verifyTelephones() throws Exception {
        new ModelVerifier<>(Telephone.class, getRealm(), 2, new ModelVerifier.Verifier<Telephone>() {
            @Override
            public void verifyModel(Telephone telephone, int index) throws Exception {
                LoginServiceResponse.Telephone response = getResponse(LoginServiceResponse.class, "login/Login_successful.json").partyDetails.telephones.get(index);
                assertEquals(response.effectiveFrom, telephone.getEffectiveFrom());
                assertEquals(response.effectiveTo, telephone.getEffectiveTo());
                assertEquals(response.extension, telephone.getExtension());
                assertEquals(response.contactNumber, telephone.getContactNumber());

                // TODO: verify contact roles
            }
        });
    }

    private void verifyTimeZonePreference() throws Exception {
        new ModelVerifier<>(TimeZonePreference.class, getRealm(), 1, new ModelVerifier.Verifier<TimeZonePreference>() {
            @Override
            public void verifyModel(TimeZonePreference model, int index) {
                assertEquals("en_ZA", model.getCode());
                assertEquals("123", model.getDaylightSavings());
                assertEquals("Africa/Johannesburg", model.getValue());
            }
        });
    }

    private void verifyWebAddresses() throws Exception {
        new ModelVerifier<>(WebAddress.class, getRealm(), 3, new ModelVerifier.Verifier<WebAddress>() {
            @Override
            public void verifyModel(WebAddress model, int index) throws Exception {
                verifyWebAddress(model, getResponse(LoginServiceResponse.class, "login/Login_successful.json").partyDetails.webAddresses.get(index));
            }
        });
    }

    private void verifyWebAddress(WebAddress model, LoginServiceResponse.WebAddress response) {
        assertEquals(response.effectiveFrom, model.getEffectiveFrom());
        assertEquals(response.effectiveTo, model.getEffectiveTo());
        assertEquals(response.uRL, model.getURL());
    }

    private void verifyGeographicalAreaPreference() throws Exception {
        new ModelVerifier<>(GeographicalAreaPreference.class, getRealm(), 1, new ModelVerifier.Verifier<GeographicalAreaPreference>() {
            @Override
            public void verifyModel(GeographicalAreaPreference model, int index) throws Exception {
                assertEquals("2016-11-21", model.getEffectiveFrom());
                assertEquals("2016-11-21", model.getEffectiveTo());
                assertEquals("906554", model.getType());
                assertEquals("Mars", model.getValue());
            }
        });
    }

    private void verifyLanguagePreference() throws Exception {
        new ModelVerifier<>(LanguagePreference.class, getRealm(), 1, new ModelVerifier.Verifier<LanguagePreference>() {
            @Override
            public void verifyModel(LanguagePreference model, int index) throws Exception {
                assertEquals("en_ZA", model.getISOCode());
                assertEquals("English/South_Africa", model.getValue());
            }
        });
    }

    private void verifyEmailAddresses() throws Exception {
        new ModelVerifier<>(EmailAddress.class, getRealm(), 2, new ModelVerifier.Verifier<EmailAddress>() {
            @Override
            public void verifyModel(EmailAddress model, int index) throws Exception {
                verifyEmailAddress(model, getResponse(LoginServiceResponse.class, "login/Login_successful.json").partyDetails.emailAddresses.get(index));
            }
        });
    }

    private void verifyEmailAddress(EmailAddress model, LoginServiceResponse.EmailAddress response) {
        assertEquals(response.effectiveFrom, model.getEffectiveFrom());
        assertEquals(response.effectiveTo, model.getEffectiveTo());
        assertEquals(response.value, model.getValue());
    }

    private void verifyPhysicalAddresses() throws Exception {
        new ModelVerifier<>(StreetAddress.class, getRealm(), 1, new ModelVerifier.Verifier<StreetAddress>() {
            @Override
            public void verifyModel(StreetAddress model, int index) throws Exception {
                assertEquals("2016-11-21", model.getEffectiveFrom());
                assertEquals("2016-11-21", model.getEffectiveTo());
                assertEquals("South Africa", model.getCountry());
                assertEquals(true, model.isPOBox());
                assertEquals("N/A", model.getComplex());
                assertEquals("181", model.getPostalCode());
                assertEquals("559 Felsiet Street", model.getStreetAddress1());
                assertEquals("0", model.getUnitNumber());
                assertEquals("Elardus Park", model.getStreetAddress2());
                assertEquals("string", model.getStreetAddress3());
                assertEquals("Pretoria", model.getPlace());
            }
        });
    }

    private void verifyUserInstructions() throws Exception {
        new ModelVerifier<>(UserInstruction.class, getRealm(), 1, new ModelVerifier.Verifier<UserInstruction>() {
            @Override
            public void verifyModel(UserInstruction model, int index) throws Exception {
                assertEquals("123", model.getId());
                assertEquals(String.valueOf(UserInstructions.Types.LOGIN_T_AND_C), model.getType());
                assertEquals("2016-11-30", model.getEffectiveFrom());
            }
        });
    }

    private void verifyAppConfigVersion() throws Exception {
        AppConfigRepositoryTest.verifyAppConfigVersion(getRealm());
    }

    private void assertLoginSucceedsWithoutProductFeatures(String path) throws Exception {
        assertTrue(repository.persistLoginResponse(getResponse(LoginServiceResponse.class, path), new Username(USERNAME)));
        assertModelIsNotCreated(ProductFeature.class);
    }

    private void assertNothingIsPersisted(String path) throws IOException {
        boolean parsingResult = repository.persistLoginResponse(getResponse(LoginServiceResponse.class, path), new Username(USERNAME));

        assertFalse(parsingResult);
        assertTrue(getRealm().isEmpty());
    }

    private void assertUserModelIsNotCreated() {
        assertModelIsNotCreated(User.class);
    }

    private static class ReferenceModelVerifier extends ModelVerifier<Reference> {

        ReferenceModelVerifier(Class<Reference> modelClass, Realm realm, int numberOfModels, Verifier<Reference> verifier) throws Exception {
            super(modelClass, realm, numberOfModels, verifier);
        }

        @NonNull
        @Override
        protected RealmResults<Reference> getModels() {
            return super.getModels().sort("issuedBy");
        }
    }

    private static class ProductFeatureModelVerifier extends ModelVerifier<ProductFeature> {
        ProductFeatureModelVerifier(Class<ProductFeature> modelClass, Realm realm, int numberOfModels, Verifier<ProductFeature> verifier) throws Exception {
            super(modelClass, realm, numberOfModels, verifier);
        }

        @NonNull
        @Override
        protected RealmResults<ProductFeature> getModels() {
            return super.getModels().sort("type");
        }
    }
}
