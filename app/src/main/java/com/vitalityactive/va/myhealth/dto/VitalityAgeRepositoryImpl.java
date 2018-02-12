package com.vitalityactive.va.myhealth.dto;


import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.myhealth.entity.VitalityAge;
import com.vitalityactive.va.networking.parsing.Persister;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.models.vitalityage.VitalityAgePersistenceModel;

public class VitalityAgeRepositoryImpl implements VitalityAgeRepository {

    private final DataStore dataStore;
    private final Persister persister;
    private final InsurerConfigurationRepository insurerConfigurationRepository;

    public VitalityAgeRepositoryImpl(DataStore dataStore, InsurerConfigurationRepository insurerConfigurationRepository) {
        this.dataStore = dataStore;
        this.persister = new Persister(dataStore);
        this.insurerConfigurationRepository = insurerConfigurationRepository;
    }

    @Override
    public VitalityAge getVitalityAge() {
        return dataStore.getFirstModelInstance(VitalityAgePersistenceModel.class, new DataStore.ModelMapper<VitalityAgePersistenceModel, VitalityAge>() {
            @Override
            public VitalityAge mapModel(VitalityAgePersistenceModel vitalityAgeModel) {
                if (vitalityAgeModel != null) {
                    return new VitalityAge.Builder()
                            .age(vitalityAgeModel.getAge())
                            .feedbackContent(vitalityAgeModel.getFeedbackContent())
                            .feedbackTitle(vitalityAgeModel.getFeedbackTitle())
                            .effectiveType(vitalityAgeModel.getFeedbackType())
                            .build();
                }
                return null;
            }
        });
    }

    @Override
    public void deleteVitalityAge() {
        dataStore.removeAll(VitalityAgePersistenceModel.class);
    }

    @Override
    public void saveVitalityAgeValue(VitalityAge _vitalityAge) {
        VitalityAge vitalityAge = getVitalityAge();
        if (vitalityAge != null) {
            deleteVitalityAge();
        }
        VitalityAgePersistenceModel vitalityAgePersistenceModel = new VitalityAgePersistenceModel();
        vitalityAgePersistenceModel.setAge(_vitalityAge.getAge());
        vitalityAgePersistenceModel.setFeedbackContent(_vitalityAge.getFeedbackContent());
        vitalityAgePersistenceModel.setFeedbackTitle(_vitalityAge.getFeedbackTitle());
        vitalityAgePersistenceModel.setFeedbackType(_vitalityAge.getEffectiveType());
        persister.addModel(vitalityAgePersistenceModel);
    }

}
