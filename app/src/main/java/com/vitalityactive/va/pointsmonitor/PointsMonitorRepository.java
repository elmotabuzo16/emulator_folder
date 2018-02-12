package com.vitalityactive.va.pointsmonitor;

import android.support.annotation.NonNull;

import com.vitalityactive.va.constants.PointsEntryCategory;
import com.vitalityactive.va.constants.ProductFeatureLinkType;
import com.vitalityactive.va.dto.PointsEntryCategoryDTO;
import com.vitalityactive.va.dto.PointsEntryDTO;
import com.vitalityactive.va.networking.model.PointsHistoryServiceResponse;
import com.vitalityactive.va.networking.parsing.Persister;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.persistence.models.FeatureLink;
import com.vitalityactive.va.persistence.models.PointsEntry;
import com.vitalityactive.va.persistence.models.VitalityMembershipPeriod;
import com.vitalityactive.va.utilities.date.Date;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.RealmQuery;

@Singleton
public class PointsMonitorRepository implements PointsMonitorAvailablePointsCategoriesProvider, PointsEntryRepository {

    private final Persister persister;
    private PointsMonitorContent content;
    private DataStore dataStore;

    @Inject
    PointsMonitorRepository(DataStore dataStore, PointsMonitorContent content) {
        this.dataStore = dataStore;
        this.content = content;
        persister = new Persister(dataStore);
    }

    boolean hasPointsEntries() {
        return dataStore.hasModelInstance(PointsEntry.class);
    }

    @NonNull
    List<PointsEntryDTO> getMonthPointsEntries(final Date firstDayOfMonth, final Date lastDayOfMonth, final PointsEntryCategoryDTO selectedCategory) {

        return dataStore.getModels(PointsEntry.class, new DataStore.QueryExecutor<PointsEntry, RealmQuery<PointsEntry>>() {
            @Override
            public List<PointsEntry> executeQueries(RealmQuery<PointsEntry> initialQuery) {
                initialQuery = initialQuery.between("effectiveDate", firstDayOfMonth.getMillisecondsSinceEpoch(), lastDayOfMonth.getMillisecondsSinceEpoch());
                if (selectedCategory.isAll()) {
                    return initialQuery.findAll();
                }
                if (selectedCategory.isOther()) {
                    List<Integer> availableCategoryTypeKeys = getAvailableCategoryTypeKeys();
                    if (availableCategoryTypeKeys.isEmpty()) {
                        return initialQuery.findAll();
                    }
                    return initialQuery.not().in("category", availableCategoryTypeKeys.toArray(new Integer[availableCategoryTypeKeys.size()])).findAll();
                }
                return initialQuery.equalTo("category", selectedCategory.getTypeKey()).findAll();
            }
        }, new DataStore.ModelListMapper<PointsEntry, PointsEntryDTO>() {
            @Override
            public List<PointsEntryDTO> mapModels(List<PointsEntry> models) {
                List<PointsEntryDTO> mappedModels = new ArrayList<>();
                for (PointsEntry pointsEntry : models) {
                    mappedModels.add(new PointsEntryDTO(pointsEntry));
                }
                return mappedModels;
            }
        });
    }

    @Override
    public PointsEntryDTO getPointsEntry(String id) {
        return dataStore.getModelInstance(PointsEntry.class, new DataStore.ModelMapper<PointsEntry, PointsEntryDTO>() {
            @Override
            public PointsEntryDTO mapModel(PointsEntry model) {
                return new PointsEntryDTO(model);
            }
        }, "id", id);
    }

    private List<Integer> getAvailableCategoryTypeKeys() {
        ArrayList<Integer> typeKeys = new ArrayList<>();
        for (PointsEntryCategoryDTO category : getPointsEntryCategories()) {
            typeKeys.add(category.getTypeKey());
        }
        return typeKeys;
    }

    boolean persistPointsHistoryResponse(PointsHistoryServiceResponse response) {
        removeOldPointsHistory();
        persistVitalityMembershipPeriods(response);
        persistPointsEntries(response);
        return true;
    }

    private void removeOldPointsHistory() {
        dataStore.removeAll(VitalityMembershipPeriod.class);
        dataStore.removeAll(PointsEntry.class);
    }

    private boolean persistVitalityMembershipPeriods(PointsHistoryServiceResponse response) {
        return persister.addModels(response.pointsAccounts, new Persister.InstanceCreator<Model, PointsHistoryServiceResponse.PointsAccount>() {
            @Override
            public Model create(PointsHistoryServiceResponse.PointsAccount model) {
                return new VitalityMembershipPeriod(model);
            }
        });
    }

    private boolean persistPointsEntries(PointsHistoryServiceResponse response) {
        if (response.pointsAccounts == null) {
            return false;
        }
        for (PointsHistoryServiceResponse.PointsAccount pointsAccount : response.pointsAccounts) {
            persister.addModels(pointsAccount.pointsEntries, new Persister.InstanceCreator<Model, PointsHistoryServiceResponse.PointsEntry>() {
                @Override
                public Model create(PointsHistoryServiceResponse.PointsEntry model) {
                    return new PointsEntry(model);
                }
            });
        }
        return true;
    }

    @Override
    public List<PointsEntryCategoryDTO> getPointsEntryCategories() {
        List<Integer> categoryKeys = dataStore.getModels(FeatureLink.class, new DataStore.QueryExecutor<FeatureLink, RealmQuery<FeatureLink>>() {
            @Override
            public List<FeatureLink> executeQueries(RealmQuery<FeatureLink> initialQuery) {
                return initialQuery.equalTo("typeKey", ProductFeatureLinkType._POINTSCATEGORY).findAll();
            }
        }, new DataStore.ModelListMapper<FeatureLink, Integer>() {
            @Override
            public List<Integer> mapModels(List<FeatureLink> models) {
                List<Integer> keys = new ArrayList<>();
                for (FeatureLink featureLink : models) {
                    keys.add(featureLink.getLinkedKey());
                }
                return keys;
            }
        });

        List<PointsEntryCategoryDTO> categories = new ArrayList<>();

        for (int key : categoryKeys) {
            categories.add(new PointsEntryCategoryDTO(key, getCategoryTitle(key)));
        }

        sortPointsEntryCategories(categories);

        return categories;
    }

    private void sortPointsEntryCategories(List<PointsEntryCategoryDTO> categories) {
        Collections.sort(categories, new Comparator<PointsEntryCategoryDTO>() {
            @Override
            public int compare(PointsEntryCategoryDTO o1, PointsEntryCategoryDTO o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
        ArrayList<PointsEntryCategoryDTO> knowYourHealthCategories = new ArrayList<>();
        ArrayList<PointsEntryCategoryDTO> improveYourHealthCategories = new ArrayList<>();
        ArrayList<PointsEntryCategoryDTO> randomCategories = new ArrayList<>();

        for (PointsEntryCategoryDTO category : categories) {
            switch (category.getTypeKey()) {
                case PointsEntryCategory._ASSESSMENT:
                case PointsEntryCategory._HEALTHYFOOD:
                case PointsEntryCategory._SCREENING:
                    knowYourHealthCategories.add(category);
                    break;
                case PointsEntryCategory._FITNESS:
                    improveYourHealthCategories.add(category);
                    break;
                default:
                    randomCategories.add(category);
            }
        }

        categories.clear();
        categories.addAll(knowYourHealthCategories);
        categories.addAll(improveYourHealthCategories);
        categories.addAll(randomCategories);
    }

    @NonNull
    private String getCategoryTitle(int typeKey) {
        return content.getPointsEntryCategoryTitle(typeKey);
    }
}
