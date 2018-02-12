package com.vitalityactive.va;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;

import com.vitalityactive.va.constants.ProductFeatureLinkType;
import com.vitalityactive.va.constants.ProductFeatureType;
import com.vitalityactive.va.dto.UserInstructionDTO;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.models.CurrentVitalityMembershipPeriod;
import com.vitalityactive.va.persistence.models.FeatureLink;
import com.vitalityactive.va.persistence.models.InsurerConfiguration;
import com.vitalityactive.va.persistence.models.ProductFeature;
import com.vitalityactive.va.persistence.models.UserInstruction;
import com.vitalityactive.va.utilities.date.NonUserFacingDateFormatter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.RealmList;
import io.realm.RealmQuery;

@Singleton
public class InsurerConfigurationRepository {
    private DataStore dataStore;
    @SuppressLint("UseSparseArrays")
    private final Map<Integer, Integer> cacheProductFeatureTypeKeyFromEventTypeKey = new HashMap<>();
    @SuppressLint("UseSparseArrays")
    private final Map<Integer, Integer> cacheProductFeatureTypeKeyFromHealthAttributeTypeKey = new HashMap<>();

    @Inject
    public InsurerConfigurationRepository(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public boolean shouldShowNonSmokersPrivacyPolicy() {
        return hasProductFeature(com.vitalityactive.va.constants.ProductFeature._NONSMOKERSDSCONSENT);
    }

    public boolean shouldShowVHCPrivacyPolicy() {
        return hasProductFeature(com.vitalityactive.va.constants.ProductFeature._VHCDSCONSENT);
    }

    public boolean shouldShowSNVPrivacyPolicy() {
        return hasProductFeature(com.vitalityactive.va.constants.ProductFeature._SVDSCONSENT);
    }

    public boolean shouldShowVHRPrivacyPolicy() {
        return hasProductFeature(com.vitalityactive.va.constants.ProductFeature._VHRDSCONSENT);
    }

    public boolean shouldShowVNAPrivacyPolicy() {
        return hasProductFeature(com.vitalityactive.va.constants.ProductFeature._VNADSCONSENT);
    }

    public boolean shouldShowWDPrivacyPolicy() {
        return hasProductFeature(com.vitalityactive.va.constants.ProductFeature._WDADSCONSENT);
    }

    public boolean shouldShowVHRDisclaimer() {
        return hasProductFeature(com.vitalityactive.va.constants.ProductFeature._VHRDISCLAIMER);
    }

    public boolean shouldShowARMedicallyFitAgreement() {
        return hasProductFeature(com.vitalityactive.va.constants.ProductFeature._ARMEDICALLYFIT);
    }

    public boolean shouldShowRewardsMenuItem() {
        return hasProductFeature(com.vitalityactive.va.constants.ProductFeature._ARPROBABILISTIC) ||
                hasProductFeature(com.vitalityactive.va.constants.ProductFeature._ARCHOICE) ||
                hasProductFeature(com.vitalityactive.va.constants.ProductFeature._ARDEFINED);
    }

    public boolean hasProbabilisticRewards() {
        return hasProductFeature(com.vitalityactive.va.constants.ProductFeature._ARPROBABILISTIC);
    }

    public boolean hasDefinedRewards() {
        return hasProductFeature(com.vitalityactive.va.constants.ProductFeature._ARDEFINED);
    }

    public boolean hasRewardChoice() {
        return hasProductFeature(com.vitalityactive.va.constants.ProductFeature._ARCHOICE);
    }

    public boolean requireVhrBeforeAr() {
        return hasProductFeature(com.vitalityactive.va.constants.ProductFeature._ARVHRREQUIRED);
    }

    private boolean hasProductFeature(int productFeatureTypeKey) {
        return dataStore.hasModelInstance(com.vitalityactive.va.persistence.models.ProductFeature.class, "type", productFeatureTypeKey);
    }

    boolean insurerConfigurationExists() {
        return dataStore.hasModelInstance(InsurerConfiguration.class);
    }

    public boolean hasFeatureTypeBMI() {
        return hasFeatureType(ProductFeatureType._VHCBMI);
    }

    public boolean hasFeatureTypeVHCTemplate() {
        return hasFeatureType(ProductFeatureType._VHCTEMPLATE);
    }

    public boolean hasFeatureTypeWaistCircumference() {
        return hasFeatureType(ProductFeatureType._VHCWAISTCIRCUM);
    }

    private boolean hasFeatureType(int featureType) {
        return dataStore.hasModelInstance(com.vitalityactive.va.persistence.models.ProductFeature.class, "featureType", featureType);
    }

    public boolean hasFeatureTypeGlucose() {
        return hasFeatureType(ProductFeatureType._VHCBLOODGLUCOSE);
    }

    public boolean hasFeatureTypeBloodPressure() {
        return hasFeatureType(ProductFeatureType._VHCBLOODPRESSURE);
    }

    public boolean hasFeatureTypeCholesterol() {
        return hasFeatureType(ProductFeatureType._VHCCHOLESTEROL);
    }

    public boolean hasFeatureTypeHbA1c() {
        return hasFeatureType(ProductFeatureType._VHCHBA1C);
    }

    public boolean hasFeatureTypeUrinaryProtein() {
        return hasFeatureType(ProductFeatureType._VHCURINARYPROTEIN);
    }

    public List<Integer> getConfiguredVHCFeatureTypes() {
        return dataStore.getModels(com.vitalityactive.va.persistence.models.ProductFeature.class,
                getQueryToSelectAllProductFeaturesForVHCFeatureTypes(),
                getModelMapperForProductFeaturesToUniqueListOfVHCFeatureTypes());
    }

    public Date getCurrentMembershipPeriodStart() {
        return dataStore.getFieldValue(CurrentVitalityMembershipPeriod.class,
                new DataStore.FieldAccessor<CurrentVitalityMembershipPeriod, Date>() {
                    @NonNull
                    @Override
                    public Date getField(@Nullable CurrentVitalityMembershipPeriod model) {
                        String effectiveFrom = null;
                        if (model != null) {
                            effectiveFrom = model.getEffectiveFrom();
                        }

                        Date date = new Date();

                        if (effectiveFrom != null) {
                            try {
                                date = NonUserFacingDateFormatter.javaUtilDateFromYearMonthDayString(effectiveFrom);
                            } catch (ParseException e) {
                                Log.e(NonUserFacingDateFormatter.TAG, "wrong date format: " + e.toString());
                            }
                        }

                        return date;
                    }
                });
    }

    public Date getCurrentMembershipPeriodEnd() {
        return dataStore.getFieldValue(CurrentVitalityMembershipPeriod.class,
                new DataStore.FieldAccessor<CurrentVitalityMembershipPeriod, Date>() {
                    @NonNull
                    @Override
                    public Date getField(@Nullable CurrentVitalityMembershipPeriod model) {
                        String effectiveTo = null;
                        if (model != null) {
                            effectiveTo = model.getEffectiveTo();
                        }

                        Date date = new Date();

                        if (effectiveTo != null) {
                            try {
                                date = NonUserFacingDateFormatter.javaUtilDateFromYearMonthDayString(effectiveTo);
                            } catch (ParseException e) {
                                Log.e(NonUserFacingDateFormatter.TAG, "wrong date format: " + e.toString());
                            }
                        }

                        return date;
                    }
                });
    }

    @NonNull
    private DataStore.QueryExecutor<com.vitalityactive.va.persistence.models.ProductFeature, RealmQuery<com.vitalityactive.va.persistence.models.ProductFeature>> getQueryToSelectAllProductFeaturesForVHCFeatureTypes() {
        return new DataStore.QueryExecutor<com.vitalityactive.va.persistence.models.ProductFeature, RealmQuery<com.vitalityactive.va.persistence.models.ProductFeature>>() {
            @Override
            public List<com.vitalityactive.va.persistence.models.ProductFeature> executeQueries(RealmQuery<com.vitalityactive.va.persistence.models.ProductFeature> initialQuery) {
                return initialQuery.in("featureType", getAllVHCEventProductFeatureTypes()).findAll();
            }
        };
    }

    @NonNull
    private Integer[] getAllVHCEventProductFeatureTypes() {
        return new Integer[]{
                ProductFeatureType._VHCBMI,
                ProductFeatureType._VHCWAISTCIRCUM,
                ProductFeatureType._VHCBLOODGLUCOSE,
                ProductFeatureType._VHCBLOODPRESSURE,
                ProductFeatureType._VHCCHOLESTEROL,
                ProductFeatureType._VHCHBA1C,
                ProductFeatureType._VHCURINARYPROTEIN
        };
    }

    @NonNull
    private DataStore.ModelListMapper<com.vitalityactive.va.persistence.models.ProductFeature, Integer> getModelMapperForProductFeaturesToUniqueListOfVHCFeatureTypes() {
        return new DataStore.ModelListMapper<com.vitalityactive.va.persistence.models.ProductFeature, Integer>() {
            @Override
            public List<Integer> mapModels(List<com.vitalityactive.va.persistence.models.ProductFeature> models) {
                ArrayList<Integer> featureTypeKeys = new ArrayList<>();
                for (com.vitalityactive.va.persistence.models.ProductFeature productFeature : models) {
                    final int featureType = productFeature.getFeatureType();
                    if (!featureTypeKeys.contains(featureType)) {
                        featureTypeKeys.add(featureType);
                    }
                }
                return featureTypeKeys;
            }
        };
    }

    public List<Integer> getConfiguredEventTypeKeys() {
        return dataStore.getModels(FeatureLink.class, new DataStore.QueryExecutor<FeatureLink, RealmQuery<FeatureLink>>() {
            @Override
            public List<FeatureLink> executeQueries(RealmQuery<FeatureLink> initialQuery) {
                return getEventTypeQuery(initialQuery).in("productFeature.featureType", getAllVHCEventProductFeatureTypes()).findAll();
            }
        }, new DataStore.ModelListMapper<FeatureLink, Integer>() {
            @Override
            public List<Integer> mapModels(List<FeatureLink> models) {
                List<Integer> eventTypeKeys = new ArrayList<>();
                for (FeatureLink featureLink : models) {
                    eventTypeKeys.add(featureLink.getLinkedKey());
                }
                return eventTypeKeys;
            }
        });
    }

    public int getFeatureTypeFromEventTypeKey(final int eventTypeKey) {
        return dataStore.getModelInstance(FeatureLink.class, new DataStore.SingleModelQueryExecutor<FeatureLink, RealmQuery<FeatureLink>>() {
            @Override
            public FeatureLink executeQueries(RealmQuery<FeatureLink> initialQuery) {
                return getEventTypeQuery(initialQuery).equalTo("linkedKey", eventTypeKey).findFirst();
            }
        }, new DataStore.ModelMapper<FeatureLink, Integer>() {
            @Override
            public Integer mapModel(FeatureLink model) {
                return model == null ? 0 : model.getProductFeature().getFeatureType();
            }
        });
    }

    public int getBMIEventTypeKey() {
        return getEventTypeKey(com.vitalityactive.va.constants.ProductFeature._BMI);
    }

    public int getBloodPressureEventTypeKey() {
        return getEventTypeKey(com.vitalityactive.va.constants.ProductFeature._BLOODPRESSURE);
    }

    public int getDiastolicBloodPressureEventTypeKey() {
        return getEventTypeKey(com.vitalityactive.va.constants.ProductFeature._DIASTOLICBP);
    }

    public int getSystolicBloodPressureEventTypeKey() {
        return getEventTypeKey(com.vitalityactive.va.constants.ProductFeature._SYSTOLICBP);
    }

    private int getEventTypeKey(final int productFeatureTypeKey) {
        return dataStore.getModelInstance(FeatureLink.class, new DataStore.SingleModelQueryExecutor<FeatureLink, RealmQuery<FeatureLink>>() {
            @Override
            public FeatureLink executeQueries(RealmQuery<FeatureLink> initialQuery) {
                return getEventTypeQuery(initialQuery).equalTo("productFeature.type", productFeatureTypeKey).findFirst();
            }
        }, new DataStore.ModelMapper<FeatureLink, Integer>() {
            @Override
            public Integer mapModel(FeatureLink model) {
                return model == null ? 0 : model.getLinkedKey();
            }
        });
    }

    private RealmQuery<FeatureLink> getEventTypeQuery(RealmQuery<FeatureLink> initialQuery) {
        return initialQuery.equalTo("typeKey", ProductFeatureLinkType._EVENTTYPE);
    }

    public Integer getHealthAttributeTypeKey(final int eventTypeKey, final int healthAttributeGroupFeatureType) {
        return dataStore.getModelInstance(ProductFeature.class, new DataStore.SingleModelQueryExecutor<ProductFeature, RealmQuery<ProductFeature>>() {
            @Override
            public ProductFeature executeQueries(RealmQuery<ProductFeature> initialQuery) {
                return initialQuery
                        .equalTo("featureType", healthAttributeGroupFeatureType)
                        .equalTo("featureLinks.linkedKey", eventTypeKey).findFirst();
            }
        }, new DataStore.ModelMapper<ProductFeature, Integer>() {
            @Override
            public Integer mapModel(ProductFeature model) {
                RealmList<FeatureLink> featureLinks = model.getFeatureLinks();
                for (FeatureLink featureLink : featureLinks) {
                    if (featureLink.getTypeKey() == ProductFeatureLinkType._HEALTHATTRIBUTETYPE) {
                        return featureLink.getLinkedKey();
                    }
                }

                return -1;
            }
        });
    }

    public List<Integer> getEventTypeKeys(final int healthAttributeGroupFeatureType) {
        return dataStore.getModels(FeatureLink.class, new DataStore.QueryExecutor<FeatureLink, RealmQuery<FeatureLink>>() {
            @Override
            public List<FeatureLink> executeQueries(RealmQuery<FeatureLink> initialQuery) {
                return getEventTypeQuery(initialQuery).equalTo("productFeature.featureType", healthAttributeGroupFeatureType).findAll();
            }
        }, new DataStore.ModelListMapper<FeatureLink, Integer>() {
            @Override
            public List<Integer> mapModels(List<FeatureLink> models) {
                List<Integer> typeKeys = new ArrayList<>();

                for (FeatureLink featureLink : models) {
                    typeKeys.add(featureLink.getLinkedKey());
                }

                return typeKeys;
            }
        });
    }

    public int getProductFeatureTypeKeyFromEventTypeKey(final int eventTypeKey) {
        if (cacheProductFeatureTypeKeyFromEventTypeKey.isEmpty()) {
            loadProductFeatureTypeKeyFromEventTypeKeyMappings();
        }

        Integer cached = cacheProductFeatureTypeKeyFromEventTypeKey.get(eventTypeKey);
        if (cached == null) {
            return 0;
        }
        return cached;
    }

    private void loadProductFeatureTypeKeyFromEventTypeKeyMappings() {
        List<Pair<Integer, Integer>> models = dataStore.getModels(ProductFeature.class, new DataStore.QueryExecutor<ProductFeature, RealmQuery<ProductFeature>>() {
            @Override
            public List<ProductFeature> executeQueries(RealmQuery<ProductFeature> initialQuery) {
                return initialQuery
                        .equalTo("featureLinks.typeKey", ProductFeatureLinkType._EVENTTYPE)
                        .findAll();
            }
        }, new DataStore.ModelListMapper<ProductFeature, Pair<Integer, Integer>>() {
            @Override
            public List<Pair<Integer, Integer>> mapModels(List<ProductFeature> models) {
                List<Pair<Integer, Integer>> result = new ArrayList<>();
                for (ProductFeature model : models) {
                    Integer productFeatureType = getProductFeatureTypeKey(model);
                    RealmList<FeatureLink> featureLinks = model.getFeatureLinks();
                    for (FeatureLink featureLink : featureLinks) {
                        if (featureLink.getTypeKey() == ProductFeatureLinkType._EVENTTYPE) {
                            result.add(new Pair<>(featureLink.getLinkedKey(), productFeatureType));
                        }
                    }
                }
                return result;
            }
        });

        for (Pair<Integer, Integer> model : models) {
            cacheProductFeatureTypeKeyFromEventTypeKey.put(model.first, model.second);
        }
    }

    @NonNull
    private Integer getProductFeatureTypeKey(ProductFeature model) {
        return model == null ? 0 : model.getType();
    }

    public int getProductFeatureTypeKeyFromHealthAttributeTypeKey(final int healthAttributeTypeKey) {
        if (cacheProductFeatureTypeKeyFromHealthAttributeTypeKey.isEmpty()) {
            loadProductFeatureTypeKeyFromHealthAttributeTypeKeyMappings();
        }

        Integer cached = cacheProductFeatureTypeKeyFromHealthAttributeTypeKey.get(healthAttributeTypeKey);
        if (cached == null) {
            return 0;
        }
        return cached;
    }

    private void loadProductFeatureTypeKeyFromHealthAttributeTypeKeyMappings() {
        List<Pair<Integer, Integer>> models = dataStore.getModels(ProductFeature.class, new DataStore.QueryExecutor<ProductFeature, RealmQuery<ProductFeature>>() {
            @Override
            public List<ProductFeature> executeQueries(RealmQuery<ProductFeature> initialQuery) {
                return initialQuery
                        .equalTo("featureLinks.typeKey", ProductFeatureLinkType._HEALTHATTRIBUTETYPE)
                        .findAll();
            }
        }, new DataStore.ModelListMapper<ProductFeature, Pair<Integer, Integer>>() {
            @Override
            public List<Pair<Integer, Integer>> mapModels(List<ProductFeature> models) {
                List<Pair<Integer, Integer>> result = new ArrayList<>();
                for (ProductFeature model : models) {
                    Integer productFeatureType = getProductFeatureTypeKey(model);
                    RealmList<FeatureLink> featureLinks = model.getFeatureLinks();
                    for (FeatureLink featureLink : featureLinks) {
                        if (featureLink.getTypeKey() == ProductFeatureLinkType._HEALTHATTRIBUTETYPE) {
                            result.add(new Pair<>(featureLink.getLinkedKey(), productFeatureType));
                        }
                    }
                }
                return result;
            }
        });

        for (Pair<Integer, Integer> model : models) {
            cacheProductFeatureTypeKeyFromHealthAttributeTypeKey.put(model.first, model.second);
        }
    }

    public boolean shouldShowRewardPartnerDataSharingConsent(final long rewardKey) {
        Integer instructionType = dataStore.getModelInstance(FeatureLink.class, new DataStore.SingleModelQueryExecutor<FeatureLink, RealmQuery<FeatureLink>>() {
            @Override
            public FeatureLink executeQueries(RealmQuery<FeatureLink> initialQuery) {
                return initialQuery
                        .equalTo("productFeature.featureType", ProductFeatureType._ACTIVEREWARDSPARTNER)
                        .equalTo("typeKey", ProductFeatureLinkType._REWARD)
                        .equalTo("linkedKey", rewardKey)
                        .findFirst();
            }
        }, new DataStore.ModelMapper<FeatureLink, Integer>() {
            @Override
            public Integer mapModel(FeatureLink model) {
                if (model == null) {
                    return null;
                }
                for (FeatureLink featureLink : model.getProductFeature().getFeatureLinks()) {
                    if (featureLink.getTypeKey() == ProductFeatureLinkType._DSINSTRUCTIONTYPE) {
                        return featureLink.getLinkedKey();
                    }
                }
                return null;
            }
        });
        if (instructionType == null) {
            return false;
        }
        String instructionTypeString = String.valueOf(instructionType);
        return dataStore.getModelInstance(UserInstruction.class, new UserInstructionDTO.Mapper(), "type", instructionTypeString) != null;
    }
}
