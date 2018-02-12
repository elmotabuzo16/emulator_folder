package com.vitalityactive.va.vitalitystatus.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vitalityactive.va.home.events.ProductFeaturePointsResponse;
import com.vitalityactive.va.networking.parsing.Persister;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.vitalitystatus.earningpoints.PointsInformationDTO;
import com.vitalityactive.va.vitalitystatus.earningpoints.StatusPointsItem;
import com.vitalityactive.va.wellnessdevices.dto.PointsDetailsDto;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmQuery;

public class ProductPointsRepositoryImpl implements ProductPointsRepository {
    private final DataStore dataStore;

    public ProductPointsRepositoryImpl(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    private Boolean parseProductFeaturePointsResponse(ProductFeaturePointsResponse response) {
        try {
            return parseModels(response, new Persister.InstanceCreator<Model, ProductFeaturePointsResponse>() {
                @Override
                public Model create(ProductFeaturePointsResponse model) {
                    return ProductFeatureCategoryAndPointsInformationsModel.create(model);
                }
            });
        } catch (RuntimeException ignored) {
            return false;
        }
    }

    private <T extends Model, U> boolean parseModels(U model, Persister.InstanceCreator<T, U> instanceCreator) {
        if (model == null) {
            return false;
        }

        T instance = instanceCreator.create(model);
        return instance != null && dataStore.add(instance);
    }

    private List<PointsInformationDTO> getProductFeatureAndPointsInformations(final int key) {
        ProductFeatureCategoryAndPointsInformationModel productFeatureCategoryAndPointsInformationModel = getProductFeatureCategoryAndPointsInformationModel(key);

        List<PointsInformationDTO> pointsInformationDTOs = new ArrayList<>();

        for (ProductFeatureAndPointsInformationModel model : productFeatureCategoryAndPointsInformationModel.getProductFeatureAndPointsInformations()) {
            pointsInformationDTOs.add(new PointsInformationDTO(model.getName(),
                    model.getPotentialPoints(),
                    false,
                    model.getKey(),
                    model.getPointsEarningFlag()));
        }

        return pointsInformationDTOs;
    }

    private ProductFeatureCategoryAndPointsInformationModel getProductFeatureCategoryAndPointsInformationModel(final int key) {
        return dataStore.getModelInstance(ProductFeatureCategoryAndPointsInformationModel.class, new DataStore.ModelMapper<ProductFeatureCategoryAndPointsInformationModel, ProductFeatureCategoryAndPointsInformationModel>() {
            @Override
            public ProductFeatureCategoryAndPointsInformationModel mapModel(ProductFeatureCategoryAndPointsInformationModel model) {
                return model;
            }
        }, "key", key);
    }

    @NonNull
    private PointsInformationDTO getPointsInformation(ProductFeatureCategoryAndPointsInformationModel model) {
        int key = model.getKey();

        return new PointsInformationDTO(model.getName(),
                getPointsTarget(model),
                pointsInformationHasChildren(key),
                isPointsLimitReached(model),
                model.getKey(),
                model.getPointsEarningFlag());
    }

    private int getPointsTarget(ProductFeatureCategoryAndPointsInformationModel model) {
        if (model.getPointsCategoryLimit() != null && model.getPointsCategoryLimit() > 0) {
            return model.getPointsCategoryLimit();
        }

        return model.getPotentialPoints();
    }

    private boolean isPointsLimitReached(ProductFeatureCategoryAndPointsInformationModel model) {
        return (model.getPointsEarned() >= getPointsTarget(model));

    }

    private boolean pointsInformationHasChildren(int key) {
        return getProductFeatureAndPointsInformations(key).size() > 0;
    }

    @Override
    public List<PointsInformationDTO> getFeatureList(final int key) {
        return dataStore.getModels(ProductFeatureCategoryAndPointsInformationModel.class, new DataStore.QueryExecutor<ProductFeatureCategoryAndPointsInformationModel, RealmQuery<ProductFeatureCategoryAndPointsInformationModel>>() {
            @Override
            public List<ProductFeatureCategoryAndPointsInformationModel> executeQueries(RealmQuery<ProductFeatureCategoryAndPointsInformationModel> initialQuery) {
                return initialQuery.equalTo("key", key).findAll();
            }
        }, new DataStore.ModelListMapper<ProductFeatureCategoryAndPointsInformationModel, PointsInformationDTO>() {
            @Override
            public List<PointsInformationDTO> mapModels(List<ProductFeatureCategoryAndPointsInformationModel> models) {
                ArrayList<PointsInformationDTO> pointsInformationDTOs = new ArrayList<>();

                if (models.size() > 0) {
                    for (int i = 0; i < models.size(); i++) {
                        RealmList<ProductFeatureAndPointsInformationModel> productFeatureAndPointsInformations = models.get(i).getProductFeatureAndPointsInformations();
                        if (productFeatureAndPointsInformations != null) {
                            for (ProductFeatureAndPointsInformationModel model : productFeatureAndPointsInformations) {
                                pointsInformationDTOs.add(new PointsInformationDTO(model.getName(),
                                        model.getPotentialPoints(),
                                        model.getSubfeatures().size() > 0,
                                        model.getKey(),
                                        model.getPointsEarningFlag()));
                            }
                        }
                    }
                }

                return pointsInformationDTOs;
            }
        });
    }

    @Override
    public List<PointsInformationDTO> getSubFeatureList(int key) {
        return dataStore.getModelInstance(ProductFeatureAndPointsInformationModel.class, new DataStore.ModelMapper<ProductFeatureAndPointsInformationModel, List<PointsInformationDTO>>() {
            @Override
            public List<PointsInformationDTO> mapModel(ProductFeatureAndPointsInformationModel models) {
                ArrayList<PointsInformationDTO> pointsInformationDTOs = new ArrayList<>();

                if (models != null && models.getSubfeatures().size() > 0) {
                    for (ProductSubfeatureModel model : models.getSubfeatures()) {
                        pointsInformationDTOs.add(new PointsInformationDTO(model.getName(),
                                model.getPotentialPoints(),
                                false,
                                model.getKey(),
                                model.getPointsEarningFlag()));
                    }
                }
                return pointsInformationDTOs;
            }
        }, "key", key);
    }

    @Override
    public List<PointsInformationDTO> getAllPointsCategories() {
        return dataStore.getModels(ProductFeatureCategoryAndPointsInformationModel.class,
                new DataStore.ModelListMapper<ProductFeatureCategoryAndPointsInformationModel, PointsInformationDTO>() {
                    @Override
                    public List<PointsInformationDTO> mapModels(List<ProductFeatureCategoryAndPointsInformationModel> models) {
                        List<PointsInformationDTO> pointsInformationDTOs = new ArrayList<>();

                        for (ProductFeatureCategoryAndPointsInformationModel model : models) {
                            pointsInformationDTOs.add(getPointsInformation(model));
                        }

                        return pointsInformationDTOs;
                    }
                });
    }

    @Override
    public boolean hasCachedPointsInformation() {
        return getAllPointsCategories().size() > 0;
    }

    @Override
    public boolean persistProductFeaturePointsResponse(ProductFeaturePointsResponse response) {
        dataStore.removeAll(ProductFeatureCategoryAndPointsInformationsModel.class);
        dataStore.removeAll(ProductFeatureCategoryAndPointsInformationModel.class);
        dataStore.removeAll(ProductFeatureAndPointsInformationModel.class);
        dataStore.removeAll(ProductSubfeaturePotentialPointsModel.class);
        dataStore.removeAll(ProductSubfeaturePointsEntryModel.class);
        dataStore.removeAll(ProductSubfeatureConditionsModel.class);
        dataStore.removeAll(ProductSubfeatureEventTypeModel.class);
        dataStore.removeAll(ProductSubfeatureModel.class);
        dataStore.removeAll(PointsEntryModel.class);
        dataStore.removeAll(EventTypeModel.class);

        return response.productFeatureCategoryAndPointsInformations != null && parseProductFeaturePointsResponse(response);
    }

    @Override
    public List<StatusPointsItem> getSubFeaturePointsEntries(final int key) {
        return dataStore.getModels(ProductSubfeatureModel.class, new DataStore.QueryExecutor<ProductSubfeatureModel, RealmQuery<ProductSubfeatureModel>>() {
            @Override
            public List<ProductSubfeatureModel> executeQueries(RealmQuery<ProductSubfeatureModel> initialQuery) {
                return initialQuery.equalTo("key", key).findAll();
            }
        }, new DataStore.ModelListMapper<ProductSubfeatureModel, StatusPointsItem>() {
            @Override
            public List<StatusPointsItem> mapModels(List<ProductSubfeatureModel> models) {
                ArrayList<StatusPointsItem> statusPointsItems = new ArrayList<>();

                if (models != null && models.size() > 0) {
                    for (ProductSubfeatureModel model : models) {
                        if (model.getPointsEntries() != null && model.getPointsEntries().size() > 0) {
                            for (ProductSubfeaturePointsEntryModel pointsEntryModel : model.getPointsEntries()) {
                                statusPointsItems.add(new StatusPointsItem(pointsEntryModel.getPotentialPoints(), pointsEntryModel.getTypeKey()));
                            }
                        }
                    }
                }

                return statusPointsItems;
            }
        });
    }

    @Override
    public List<StatusPointsItem> getFeaturePointsEntries(final int key) {
        return dataStore.getModels(ProductFeatureAndPointsInformationModel.class, new DataStore.QueryExecutor<ProductFeatureAndPointsInformationModel, RealmQuery<ProductFeatureAndPointsInformationModel>>() {
            @Override
            public List<ProductFeatureAndPointsInformationModel> executeQueries(RealmQuery<ProductFeatureAndPointsInformationModel> initialQuery) {
                return initialQuery.equalTo("key", key).findAll();
            }
        }, new DataStore.ModelListMapper<ProductFeatureAndPointsInformationModel, StatusPointsItem>() {
            @Override
            public List<StatusPointsItem> mapModels(List<ProductFeatureAndPointsInformationModel> models) {
                ArrayList<StatusPointsItem> statusPointsItems = new ArrayList<>();

                if (models != null && models.size() > 0) {
                    for (ProductFeatureAndPointsInformationModel model : models) {
                        if (model.getPointsEntries() != null && model.getPointsEntries().size() > 0) {
                            for (PointsEntryModel pointsEntryModel : model.getPointsEntries()) {
                                statusPointsItems.add(new StatusPointsItem(pointsEntryModel.getPotentialPoints(), pointsEntryModel.getTypeKey(), getPointsDetails(key)));
                            }
                        }
                    }
                }

                return statusPointsItems;
            }
        });
    }

    @Override
    public String getFeatureName(int key) {
        return dataStore.getFieldValue(ProductFeatureCategoryAndPointsInformationModel.class,
                "key",
                key,
                new DataStore.FieldAccessor<ProductFeatureCategoryAndPointsInformationModel, String>() {
                    @NonNull
                    @Override
                    public String getField(@Nullable ProductFeatureCategoryAndPointsInformationModel model) {
                        return model == null ? "" : model.getName();
                    }
                });
    }

    @Override
    public String getSubFeatureName(int key) {
        return dataStore.getFieldValue(ProductFeatureAndPointsInformationModel.class,
                "key",
                key,
                new DataStore.FieldAccessor<ProductFeatureAndPointsInformationModel, String>() {
                    @NonNull
                    @Override
                    public String getField(@Nullable ProductFeatureAndPointsInformationModel model) {
                        return model == null ? "" : model.getName();
                    }
                });
    }

    private List<PointsDetailsDto> getPointsDetails(final int key) {
        return dataStore.getModels(ProductFeatureAndPointsInformationModel.class, new DataStore.QueryExecutor<ProductFeatureAndPointsInformationModel, RealmQuery<ProductFeatureAndPointsInformationModel>>() {
            @Override
            public List<ProductFeatureAndPointsInformationModel> executeQueries(RealmQuery<ProductFeatureAndPointsInformationModel> initialQuery) {
                return initialQuery.equalTo("key", key).findAll();
            }
        }, new DataStore.ModelListMapper<ProductFeatureAndPointsInformationModel, PointsDetailsDto>() {
            @Override
            public List<PointsDetailsDto> mapModels(List<ProductFeatureAndPointsInformationModel> models) {
                ArrayList<PointsDetailsDto> potentialPoints = new ArrayList<>();

                for (ProductFeatureAndPointsInformationModel model : models) {
                    RealmList<PointsEntryModel> pointsEntries = model.getPointsEntries();
                    if (pointsEntries != null) {
                        for (PointsEntryModel pointsEntry : pointsEntries) {
                            RealmList<PotentialPointsModel> productSubfeaturePotentialPointsModels = pointsEntry.getPotentialPointsModels();
                            if (productSubfeaturePotentialPointsModels != null) {
                                for (PotentialPointsModel potentialPointsModel : productSubfeaturePotentialPointsModels) {
                                    potentialPoints.add(new PointsDetailsDto(potentialPointsModel));
                                }
                            }
                        }
                    }
                }

                return potentialPoints;
            }
        });
    }
}
