package com.vitalityactive.va.login;

import android.support.annotation.Nullable;

import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.networking.parsing.Persister;
import com.vitalityactive.va.onboarding.LoginRepository;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.Model;
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
import com.vitalityactive.va.persistence.models.VitalityMembership;
import com.vitalityactive.va.persistence.models.WebAddress;
import com.vitalityactive.va.register.entity.Username;

import java.util.List;

public class LoginRepositoryImpl implements LoginRepository {
    private final DataStore dataStore;
    private AppConfigRepository appConfigRepository;
    private final Persister persister;

    public LoginRepositoryImpl(DataStore dataStore, AppConfigRepository appConfigRepository) {
        this.dataStore = dataStore;
        this.appConfigRepository = appConfigRepository;
        persister = new Persister(dataStore);
    }

    @Override
    public boolean persistLoginResponse(LoginServiceResponse response, Username username) {
        if (response.partyDetails == null) {
            return false;
        }

        try {
            if (!parseAndPersistUser(response, username.getText().toString())
                    || !parseAndPersistInsurerConfiguration(response)
                    || !parseAndPersistAppConfig(response)) {
                clearDataStore();
                return false;
            }
        } catch (Exception ignored) {
            clearDataStore();
            return false;
        }

        try {
            addModel(getMeasurementSystemPreference(response));
            addModel(getTimeZonePreference(response));
            addModel(getGeographicalAreaPreference(response));
            addModel(getLanguagePreference(response));
            addModel(getCurrentMembershipPeriod(response));
            parseAndPersistReferences(response.partyDetails.references);
            parseAndPersistGeneralPreferences(response.partyDetails.generalPreferences);
            parseAndPersistTelephones(response.partyDetails.telephones);
            parseAndPersistWebAddresses(response.partyDetails.webAddresses);
            parseAndPersistEmailAddresses(response.partyDetails.emailAddresses);
            parseAndPersistPhysicalAddresses(response.partyDetails.physicalAddresses);
            parseAndPersistUserInstructions(response.userInstructions);
            parseAndPersistVitalityMembership(response.vitalityMembership);
        } catch (Exception ignored) {

        }

        return true;
    }

    private void clearDataStore() {
        // todo: should only the main data store be cleared? if not, use DataStoreClearer
        dataStore.clear();
    }

    private boolean parseAndPersistAppConfig(LoginServiceResponse response) {
        return appConfigRepository.persistAppConfig(response.application);
    }

    private boolean parseAndPersistProductFeatures(LoginServiceResponse response) {
        return !firstValueExists(response.vitalityMembership.get(0).membershipProducts)
                || !firstValueExists(response.vitalityMembership.get(0).membershipProducts.get(0).productFeatures)
                || parseAndPersistModels(response.vitalityMembership.get(0).membershipProducts.get(0).productFeatures, new Persister.InstanceCreator<Model, LoginServiceResponse.ProductFeature>() {
            @Override
            public Model create(LoginServiceResponse.ProductFeature model) {
                return ProductFeature.create(model);
            }
        });
    }

    private <T> boolean firstValueExists(List<T> list) {
        return list != null && !list.isEmpty() && list.get(0) != null;
    }

    @Nullable
    private LanguagePreference getLanguagePreference(LoginServiceResponse response) {
        return response.partyDetails.languagePreference == null ? null : new LanguagePreference(response.partyDetails.languagePreference);
    }

    @Nullable
    private GeographicalAreaPreference getGeographicalAreaPreference(LoginServiceResponse response) {
        return response.partyDetails.geographicalAreaPreference == null ? null : new GeographicalAreaPreference(response.partyDetails.geographicalAreaPreference);
    }

    @Nullable
    private TimeZonePreference getTimeZonePreference(LoginServiceResponse response) {
        return response.partyDetails.timeZonePreference == null ? null : new TimeZonePreference(response.partyDetails.timeZonePreference);
    }

    @Nullable
    private MeasurementSystemPreference getMeasurementSystemPreference(LoginServiceResponse response) {
        return response.partyDetails.measurementSystemPreference == null ? null : new MeasurementSystemPreference(response.partyDetails.measurementSystemPreference);
    }

    @Nullable
    private CurrentVitalityMembershipPeriod getCurrentMembershipPeriod(LoginServiceResponse response) {
        return response.vitalityMembership.get(0).currentVitalityMembershipPeriod == null
                ? null
                : new CurrentVitalityMembershipPeriod(response.vitalityMembership.get(0).currentVitalityMembershipPeriod);
    }

    private <T extends Model> void addModel(T model) {
        if (model != null) {
            dataStore.add(model);
        }
    }

    private boolean parseAndPersistUser(LoginServiceResponse response, String username) {
        User model = new User(response.partyDetails, response.vitalityMembership.get(0).id, username);
        return dataStore.add(model);
    }

    private boolean parseAndPersistInsurerConfiguration(LoginServiceResponse response) {
        InsurerConfiguration model = new InsurerConfiguration(response.partyDetails.tenantId);
        return dataStore.add(model);
    }

    private boolean parseAndPersistUserInstructions(List<LoginServiceResponse.UserInstruction> userInstructions) {
        return parseAndPersistModels(userInstructions, new Persister.InstanceCreator<Model, LoginServiceResponse.UserInstruction>() {
            @Override
            public Model create(LoginServiceResponse.UserInstruction model) {
                return new UserInstruction(model);
            }
        });
    }

    private boolean parseAndPersistPhysicalAddresses(List<LoginServiceResponse.PhysicalAddress> physicalAddresses) {
        return parseAndPersistModels(physicalAddresses, new Persister.InstanceCreator<Model, LoginServiceResponse.PhysicalAddress>() {
            @Override
            public Model create(LoginServiceResponse.PhysicalAddress model) {
                return new StreetAddress(model);
            }
        });
    }

    private boolean parseAndPersistEmailAddresses(List<LoginServiceResponse.EmailAddress> emailAddresses) {
        return parseAndPersistModels(emailAddresses, new Persister.InstanceCreator<Model, LoginServiceResponse.EmailAddress>() {
            @Override
            public Model create(LoginServiceResponse.EmailAddress model) {
                return new EmailAddress(model);
            }
        });
    }

    private boolean parseAndPersistWebAddresses(List<LoginServiceResponse.WebAddress> webAddresses) {
        return parseAndPersistModels(webAddresses, new Persister.InstanceCreator<Model, LoginServiceResponse.WebAddress>() {
            @Override
            public Model create(LoginServiceResponse.WebAddress model) {
                return new WebAddress(model);
            }
        });
    }

    private boolean parseAndPersistReferences(List<LoginServiceResponse.Reference> references) {
        return parseAndPersistModels(references, new Persister.InstanceCreator<Model, LoginServiceResponse.Reference>() {
            @Override
            public Model create(LoginServiceResponse.Reference model) {
                return new Reference(model);
            }
        });
    }

    private boolean parseAndPersistGeneralPreferences(List<LoginServiceResponse.GeneralPreference> generalPreferences) {
        return parseAndPersistModels(generalPreferences, new Persister.InstanceCreator<Model, LoginServiceResponse.GeneralPreference>() {
            @Override
            public Model create(LoginServiceResponse.GeneralPreference model) {
                return new GeneralPreference(model);
            }
        });
    }

    private boolean parseAndPersistTelephones(List<LoginServiceResponse.Telephone> telephones) {
        return parseAndPersistModels(telephones, new Persister.InstanceCreator<Model, LoginServiceResponse.Telephone>() {
            @Override
            public Model create(LoginServiceResponse.Telephone model) {
                return new Telephone(model);
            }
        });
    }

    private boolean parseAndPersistVitalityMembership(final List<LoginServiceResponse.VitalityMembership> vitalityMembership) {
        return parseAndPersistModels(vitalityMembership, new Persister.InstanceCreator<Model, LoginServiceResponse.VitalityMembership>() {
            @Override
            public Model create(LoginServiceResponse.VitalityMembership model) {
                return new VitalityMembership(model);
            }
        });
    }

    private <T extends Model, U> boolean parseAndPersistModels(List<U> models, Persister.InstanceCreator<T, U> instanceCreator) {
        return persister.addModels(models, instanceCreator);
    }
}
