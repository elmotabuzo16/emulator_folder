package com.vitalityactive.va.appconfig;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.vitalityactive.va.R;
import com.vitalityactive.va.dto.AppConfigFeatureParameterDTO;
import com.vitalityactive.va.networking.model.Application;
import com.vitalityactive.va.networking.parsing.Persister;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.persistence.models.AppConfigFeature;
import com.vitalityactive.va.persistence.models.AppConfigFeatureParameter;
import com.vitalityactive.va.persistence.models.AppConfigVersion;
import com.vitalityactive.va.persistence.models.ApplicationData;
import com.vitalityactive.va.utilities.TextUtilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmQuery;

public class AppConfigRepository {

    private static final String DEFAULT_EMPTY_VERSION_NUMBER = "0.0";
    public static String MISSING_FILE = "MISSING_FILE";
    private DataStore dataStore;
    private Context context;

    public AppConfigRepository(DataStore dataStore, Context context) {
        this.dataStore = dataStore;
        this.context = context;
    }

    public boolean persistAppConfigVersion(Application.ConfigurationVersion configurationVersion) {
        removeOldAppConfigVersion();
        return persistNewAppConfigVersion(configurationVersion);
    }

    private boolean persistNewAppConfigVersion(Application.ConfigurationVersion configurationVersion) {
        AppConfigVersion model = new AppConfigVersion(configurationVersion.effectiveFrom, configurationVersion.effectiveTo, configurationVersion.releaseVersion);
        return dataStore.add(model);
    }

    private void removeOldAppConfigVersion() {
        dataStore.removeAll(AppConfigVersion.class);
    }

    public boolean persistAppConfigFeatures(List<Application.ApplicationFeature> applicationFeatures) {
        removeOldAppConfigFeatures();
        return persistNewAppConfigFeatures(applicationFeatures);
    }

    private boolean persistNewAppConfigFeatures(List<Application.ApplicationFeature> applicationFeatures) {
        return new Persister(dataStore).addModels(applicationFeatures, new Persister.InstanceCreator<Model, Application.ApplicationFeature>() {
            @Override
            public Model create(Application.ApplicationFeature model) {
                return new AppConfigFeature(model);
            }
        });
    }

    private void removeOldAppConfigFeatures() {
        dataStore.removeAll(AppConfigFeature.class);
        dataStore.removeAll(AppConfigFeatureParameter.class);
    }

    @NonNull
    public String getLiferayGroupId() {
        return dataStore.getFieldValue(AppConfigFeatureParameter.class, "name", "liferayGroupId", getStringFieldAccessor());
    }

    @NonNull
    public String getGradientColor1() {
        return dataStore.getFieldValue(AppConfigFeatureParameter.class, "name", "gradientColor1", getStringFieldAccessor());
    }

    @NonNull
    public String getGradientColor2() {
        return dataStore.getFieldValue(AppConfigFeatureParameter.class, "name", "gradientColor2", getStringFieldAccessor());
    }

    @NonNull
    public String getDefaultLanguageCode() {
        return dataStore.getFieldValue(AppConfigFeatureParameter.class, "name", "defaultLanguage", getStringFieldAccessor());
    }

    @NonNull
    private String getGlobalTintColour() {
        String colour = dataStore.getFieldValue(AppConfigFeatureParameter.class, "name", "globalTintColor", getStringFieldAccessor());
        if (TextUtilities.isNullOrWhitespace(colour)) {
            colour = Integer.toHexString(ContextCompat.getColor(context, R.color.colorPrimary));
        }
        return colour;
    }

    public String getGlobalTintColorHex() {
        return "#" + getGlobalTintColour();
    }

    @NonNull
    private String getGlobalTintDarkerColour() {
      return  Integer.toHexString(manipulateColor(Color.parseColor(getGlobalTintColorHex()), 0.85f));
    }

    private int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r,255),
                Math.min(g,255),
                Math.min(b,255));
    }


    public String getGlobalTintDarkerColorHex() {
        return "#" + getGlobalTintDarkerColour();
    }


    @NonNull
    public String getReleaseVersion() {
        return dataStore.getFieldValue(AppConfigVersion.class, new DataStore.FieldAccessor<AppConfigVersion, String>() {
            @NonNull
            @Override
            public String getField(@Nullable AppConfigVersion model) {
                return model == null ? DEFAULT_EMPTY_VERSION_NUMBER : model.getReleaseVersion();
            }
        });
    }

    @NonNull
    public String getApplicationTypeCode() {
        return dataStore.getFieldValue(ApplicationData.class, new DataStore.FieldAccessor<ApplicationData, String>() {
            @NonNull
            @Override
            public String getField(@Nullable ApplicationData model) {
                return model.getTypeCode();
            }
        });
    }

    @NonNull
    private DataStore.FieldAccessor<AppConfigFeatureParameter, String> getStringFieldAccessor() {
        return new DataStore.FieldAccessor<AppConfigFeatureParameter, String>() {
            @NonNull
            @Override
            public String getField(@Nullable AppConfigFeatureParameter model) {
                return model == null ? "" : model.getValue();
            }
        };
    }

    public List<String> getResourceFileNames() {
        List<AppConfigFeatureParameterDTO> parameters = dataStore.getModels(AppConfigFeatureParameter.class, new DataStore.QueryExecutor<AppConfigFeatureParameter, RealmQuery<AppConfigFeatureParameter>>() {
            @Override
            public List<AppConfigFeatureParameter> executeQueries(RealmQuery<AppConfigFeatureParameter> initialQuery) {
                return initialQuery.in("featureType", getAllResourceFileFeatureTypeKeys()).findAll();
            }
        }, new DataStore.ModelListMapper<AppConfigFeatureParameter, AppConfigFeatureParameterDTO>() {
            @Override
            public List<AppConfigFeatureParameterDTO> mapModels(List<AppConfigFeatureParameter> models) {
                List<AppConfigFeatureParameterDTO> mappedModels = new ArrayList<>();
                for (AppConfigFeatureParameter appConfigFeatureParameter : models) {
                    mappedModels.add(new AppConfigFeatureParameterDTO(appConfigFeatureParameter));
                }
                return mappedModels;
            }
        });

        List<String> fileNames = new ArrayList<>();
        for (AppConfigFeatureParameterDTO dto : parameters) {
            fileNames.add(dto.value);
        }

        return fileNames;
    }

    @NonNull
    private String[] getAllResourceFileFeatureTypeKeys() {
        return new String[] {
                ResourceFileFeatureType.NON_SMOKERS_DECLARATION.getTypeKey(),
                ResourceFileFeatureType.VHC.getTypeKey(),
                ResourceFileFeatureType.VHR.getTypeKey(),
                ResourceFileFeatureType.VNA.getTypeKey(),
                ResourceFileFeatureType.SRC.getTypeKey()
        };
    }

    public String getResourceFileName(final ResourceFileFeatureType resourceFileFeatureType, final String languageCode) {
        return dataStore.getFieldValue(AppConfigFeatureParameter.class, new DataStore.SingleModelQueryExecutor<AppConfigFeatureParameter, RealmQuery<AppConfigFeatureParameter>>() {
            @Override
            public AppConfigFeatureParameter executeQueries(RealmQuery<AppConfigFeatureParameter> initialQuery) {
                return initialQuery.equalTo("featureType", resourceFileFeatureType.getTypeKey()).equalTo("name", languageCode).findFirst();
            }
        }, new DataStore.FieldAccessor<AppConfigFeatureParameter, String>() {
            @NonNull
            @Override
            public String getField(@Nullable AppConfigFeatureParameter model) {
                return model == null ? resourceFileFeatureType.getFallbackFileName() : model.getValue();
            }
        });
    }

    public String getGeneralTermsAndConditionsContentId() {
        return getLiferayContentId(AppConfigFeatureType.GENERAL_TERMS_AND_CONDITIONS);
    }

    public String getNonSmokersDeclarationContentId() {
        return getLiferayContentId(AppConfigFeatureType.NON_SMOKERS_DECLARATION);
    }

    public String getNonSmokersPrivacyPolicyContentId() {
        return getLiferayContentId(AppConfigFeatureType.NON_SMOKERS_PRIVACY_POLICY);
    }

    public String getVHCPrivacyPolicyContentId() {
        return getLiferayContentId(AppConfigFeatureType.VHC_PRIVACY_POLICY);
    }

    public String getVHRPrivacyPolicyContentId() {
        return getLiferayContentId(AppConfigFeatureType.VHR_PRIVACY_POLICY);
    }

    public String getVNAPrivacyPolicyContentId() {
        return getLiferayContentId(AppConfigFeatureType.VNA_PRIVACY_POLICY);
    }

    public String getSNVPrivacyPolicyContentId() {
        return getLiferayContentId(AppConfigFeatureType.SNV_PRIVACY_POLICY);
    }

    public String getWellnessDevicesPrivacyPolicyContentId() {
        return getLiferayContentId(AppConfigFeatureType.WD_PRIVACY_POLICY);
    }

    public String getPreferencesPrivacyPolicyContentId() {
        return getLiferayContentId(AppConfigFeatureType.PREFERENCES_PRIVACY_POLICY);
    }

    public String getUKEPartnerJourneyTermsAndConditionsContentId() {
        return getLiferayContentId(AppConfigFeatureType.UKE_PARTNER_JOURNEY_TERMS_AND_CONDITIONS);
    }

    public String getVNADisclaimerContentId() {
        return getLiferayContentId(AppConfigFeatureType.VNA_DISCLAIMER);
    }

    public String getVHRDisclaimerContentId() {
        return getLiferayContentId(AppConfigFeatureType.VHR_DISCLAIMER);
    }

    public String getActiveRewardsMedicallyFitContentId() {
        return getLiferayContentId(AppConfigFeatureType.AR_MEDICALLY_FIT_AGREEMENT);
    }

    public String getActiveRewardsDataPrivacyContentId() {
        return "ar-rewards-data-privacy";
    }

    public String getStarbucksPrivacyPolicyContentId() {
        return getLiferayContentId(AppConfigFeatureType.STARBUCKS_PRIVACY_POLICY);
    }

    public String getShareVitalityStatusContentId() {
        return getLiferayContentId(AppConfigFeatureType.UKE_SHARE_VITALITY_STATUS);
    }

    private String getLiferayContentId(final AppConfigFeatureType appConfigFeatureType) {
        return getAppConfigFeatureParameterValue(appConfigFeatureType, "lifeRayContentId");
    }

    public String getWellnessDevicesActivityMappingFileName() {
        return getAppConfigFeatureParameterValue(AppConfigFeatureType.WDA_ACTIVITY_MAPPING, "filename");
    }

    public String getMWBPrivacyPolicyContentId() {
        return getLiferayContentId(AppConfigFeatureType.VHR_PRIVACY_POLICY);
    }

    private String getAppConfigFeatureParameterValue(final AppConfigFeatureType appConfigFeatureType, final String featureParameterName) {
        return dataStore.getFieldValue(AppConfigFeatureParameter.class, new DataStore.SingleModelQueryExecutor<AppConfigFeatureParameter, RealmQuery<AppConfigFeatureParameter>>() {
            @Override
            public AppConfigFeatureParameter executeQueries(RealmQuery<AppConfigFeatureParameter> initialQuery) {
                return initialQuery.equalTo("featureType", appConfigFeatureType.getTypeKey()).equalTo("name", featureParameterName).findFirst();
            }
        }, getStringFieldAccessor());
    }

    public boolean persistAppConfig(Application application) {
        if (application == null) {
            return verifyAppConfigInsurerFields();
        }
        if (areRequiredFieldsMissing(application)) {
            return false;
        }
        if (noApplicationFeatures(application)) {
            return verifyAppConfigInsurerFields();
        }

        return persistAppConfigFeatures(application.configurationVersion.applicationFeatures)
                && persistAppConfigVersion(application.configurationVersion)
                && persistApplicationData(application)
                && verifyAppConfigInsurerFields();
    }

    private boolean persistApplicationData(Application application){
        ApplicationData model = new ApplicationData(application.typeCode, application.name, application.typeKey);
        return dataStore.add(model);
    }

    private boolean noApplicationFeatures(Application application) {
        return application.configurationVersion.applicationFeatures == null || application.configurationVersion.applicationFeatures.isEmpty();
    }

    private boolean areRequiredFieldsMissing(Application application) {
        return application.configurationVersion == null || application.typeKey == null;
    }

    private boolean verifyAppConfigInsurerFields() {
        return !getLiferayGroupId().equals("")
                && !getGradientColor1().equals("")
                && !getGradientColor2().equals("")
                && !getDefaultLanguageCode().equals("")
                && !getGlobalTintColorHex().equals("");
    }

    public List<String> getFileNamesOfResourceFilesThatAreMissing() {
        List<String> resourceFileNames = getResourceFileNames();
        List<String> fileNamesOfResourceFilesThatAreMissing = new ArrayList<>();
        for (String fileName : resourceFileNames) {
            File file = getFile(fileName);
            if (!file.exists()) {
                fileNamesOfResourceFilesThatAreMissing.add(fileName);
            }
        }
        return fileNamesOfResourceFilesThatAreMissing;
    }

    @NonNull
    public File getFile(String fileName) {
        return new File(context.getFilesDir() + File.separator + fileName);
    }

    public String getHealthCareBenefitFileName() {
        return getAppConfigFeatureParameterValue(AppConfigFeatureType.VHC_HEALTH_CARE_BENEFIT, "filename");
    }

    public String getActiveRewardsBenefitGuideFileName() {
        String filename = getAppConfigFeatureParameterValue(AppConfigFeatureType.AR_BENEFIT_GUIDE, "filename");
        return TextUtilities.isNullOrWhitespace(filename) ? MISSING_FILE : filename;
    }

    private enum AppConfigFeatureType {
        GENERAL_TERMS_AND_CONDITIONS("2"),
        NON_SMOKERS_DECLARATION("4"),
        NON_SMOKERS_PRIVACY_POLICY("5"),
        VHC_PRIVACY_POLICY("8"),
        VHR_PRIVACY_POLICY("11"),
        PREFERENCES_PRIVACY_POLICY("14"),
        WD_PRIVACY_POLICY("15"),
        VNA_DISCLAIMER("59"),
        WDA_ACTIVITY_MAPPING("16"),
        VHC_HEALTH_CARE_BENEFIT("17"),
        UKE_PARTNER_JOURNEY_TERMS_AND_CONDITIONS("9999999"),      // todo
        VHR_DISCLAIMER("18"),
        AR_MEDICALLY_FIT_AGREEMENT("19"),
        AR_BENEFIT_GUIDE("21"),
        SNV_PRIVACY_POLICY("23"),
        STARBUCKS_PRIVACY_POLICY("23"),
        UKE_SHARE_VITALITY_STATUS("25"),
        VNA_PRIVACY_POLICY("11"); //TODO: jay: change to correct VNA FeatureTypes


        private final String typeKey;

        AppConfigFeatureType(String typeKey) {
            this.typeKey = typeKey;
        }

        public String getTypeKey() {
            return typeKey;
        }
    }

    public enum ResourceFileFeatureType {
        NON_SMOKERS_DECLARATION("6", "non_smokers_declaration.json"),
        VHC("9", "vhc.json"),
        VHR("12", "vhr.json"),
        VNA("1000000001", "vna.json"),
        SRC("10001", "screening.json")
        ;

        private final String typeKey;

        private final String fallbackFileName;

        ResourceFileFeatureType(String typeKey, String fallbackFileName) {
            this.typeKey = typeKey;
            this.fallbackFileName = fallbackFileName;
        }

        public String getTypeKey() {
            return typeKey;
        }

        public String getFallbackFileName() {
            return fallbackFileName;
        }

    }
}
