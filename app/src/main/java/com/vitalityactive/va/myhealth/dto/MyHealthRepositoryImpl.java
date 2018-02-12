package com.vitalityactive.va.myhealth.dto;

import android.util.Log;

import com.vitalityactive.va.networking.model.HealthAttributeFeedbackResponse;
import com.vitalityactive.va.networking.model.HealthAttributeInformationResponse;
import com.vitalityactive.va.networking.parsing.Persister;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.persistence.models.myhealth.HealthInformationSection;
import com.vitalityactive.va.persistence.models.myhealth.VitalityAgeHealthAttribute;
import com.vitalityactive.va.persistence.models.vhc.HealthAttributeFeedback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.realm.RealmQuery;
import io.realm.Sort;

import static com.vitalityactive.va.myhealth.shared.VitalityAgeConstants.VITALITY_AGE_SECTION;
import static com.vitalityactive.va.myhealth.shared.VitalityAgeConstants.VITALITY_AGE_TYPE_KEY;

public class MyHealthRepositoryImpl implements MyHealthRepository {

    private final Persister persister;
    private final DataStore dataStore;

    public MyHealthRepositoryImpl(DataStore dataStore) {
        this.persister = new Persister(dataStore);
        this.dataStore = dataStore;
    }

    @Override
    public void persistMostRecentHealthAttributeFeedback(HealthAttributeFeedbackResponse healthAttributeFeedbackResponse) {
        removeAll(VitalityAgeHealthAttribute.class);
        try {
            List<HealthAttributeFeedbackResponse.HealthAttribute> healthAttributes = healthAttributeFeedbackResponse.healthAttributes;
            Collections.sort(healthAttributes, new HealthAttributeComparator());
            VitalityAgeHealthAttribute vitalityAgeHealthAttribute = new VitalityAgeHealthAttribute(healthAttributes.get(0));
            vitalityAgeHealthAttribute.addHealthAttributeFeedbacks(addHealthAttributeFeedbacks(healthAttributes.get(0).healthAttributeFeedbacks));
            persister.addModel(vitalityAgeHealthAttribute);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public VitalityAgeHealthAttributeDTO getVitalityAgeHealthAttributeFeedback() {
        HealthInformationSectionDTO healthInformationSectionByTypeKey = getHealthInformationSectionByTypeKey(VITALITY_AGE_SECTION);
        if (healthInformationSectionByTypeKey != null
                && healthInformationSectionByTypeKey.getAttributeDTOS() != null
                && !healthInformationSectionByTypeKey.getAttributeDTOS().isEmpty()) {
            for (AttributeDTO attr : healthInformationSectionByTypeKey.getAttributeDTOS()) {
                if (new Long(attr.getAttributeTypeKey()).equals(VITALITY_AGE_TYPE_KEY)) {
                    return new VitalityAgeHealthAttributeDTO(attr);
                }
            }
        }
        return null;
    }

    @Override
    public List<HealthInformationSectionDTO> getHealthInformationSections() {
        final List<HealthInformationSectionDTO> sectionDTOs = new ArrayList<HealthInformationSectionDTO>();
        return dataStore.getModels(HealthInformationSection.class, new DataStore.ModelListMapper<HealthInformationSection, HealthInformationSectionDTO>() {
            @Override
            public List<HealthInformationSectionDTO> mapModels(List<HealthInformationSection> sectionList) {
                for (HealthInformationSection section : sectionList) {
                    sectionDTOs.add(new HealthInformationSectionDTO(section));
                }
                return sectionDTOs;
            }
        });
    }

    @Override
    public HealthInformationSectionDTO getHealthInformationSectionByTypeKey(final long typeKey) {
        if (hasFetchedHealthInformation()) {
            final List<HealthInformationSectionDTO> sectionDTOs = new ArrayList<>();
            List<HealthInformationSectionDTO> objects = dataStore.getModels(HealthInformationSection.class, new DataStore.QueryExecutor<HealthInformationSection, RealmQuery<HealthInformationSection>>() {
                @Override
                public List<HealthInformationSection> executeQueries(RealmQuery<HealthInformationSection> initialQuery) {
                    return getSectionByTypeKeyQuery(initialQuery, typeKey).findAllSorted("sortOrder", Sort.ASCENDING);
                }
            }, new DataStore.ModelListMapper<HealthInformationSection, HealthInformationSectionDTO>() {
                @Override
                public List<HealthInformationSectionDTO> mapModels(List<HealthInformationSection> sectionList) {
                    for (HealthInformationSection section : sectionList) {
                        sectionDTOs.add(new HealthInformationSectionDTO(section));
                    }
                    return sectionDTOs;
                }
            });
            return objects != null && objects.size() > 0 ? objects.get(0) : null;
        } else {
            return null;
        }
    }

    public boolean hasFetchedHealthInformation() {
        try {
            return dataStore.hasModelInstance(HealthInformationSection.class);
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public boolean sectionHasSubsection(long sectionTypeKey) {
        List<HealthInformationSectionDTO> sections = getHealthInformationSectionByParentTypeKey(Long.valueOf(sectionTypeKey).intValue());
        if (sections != null && sections.size() > 0) {
            for (HealthInformationSectionDTO section : sections) {
                if (section.getAttributeDTOS() != null && section.getAttributeDTOS().size() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<HealthInformationSectionDTO> getHealthInformationSectionByParentTypeKey(final int parentTypeKey) {
        if (hasFetchedHealthInformation()) {
            final List<HealthInformationSectionDTO> sectionDTOs = new ArrayList<>();
            return dataStore.getModels(HealthInformationSection.class, new DataStore.QueryExecutor<HealthInformationSection, RealmQuery<HealthInformationSection>>() {
                @Override
                public List<HealthInformationSection> executeQueries(RealmQuery<HealthInformationSection> initialQuery) {
                    return getSectionByParentTypeKeyQuery(initialQuery, parentTypeKey).findAllSorted("sortOrder", Sort.ASCENDING);
                }
            }, new DataStore.ModelListMapper<HealthInformationSection, HealthInformationSectionDTO>() {
                @Override
                public List<HealthInformationSectionDTO> mapModels(List<HealthInformationSection> sectionList) {
                    for (HealthInformationSection section : sectionList) {
                        sectionDTOs.add(new HealthInformationSectionDTO(section));
                    }
                    return sectionDTOs;
                }
            });
        } else {
            return null;
        }
    }

    private RealmQuery<HealthInformationSection> getSectionByTypeKeyQuery(RealmQuery<HealthInformationSection> initialQuery, long typeKey) {
        return initialQuery.equalTo("typeKey", typeKey);
    }

    private RealmQuery<HealthInformationSection> getSectionByParentTypeKeyQuery(RealmQuery<HealthInformationSection> initialQuery, int parentTypeKey) {
        return initialQuery.equalTo("parentTypeKey", parentTypeKey);
    }

    public List<HealthInformationSection> getHealthInformationsSections() {
        return dataStore.getModels(HealthInformationSection.class, new DataStore.ModelListMapper<HealthInformationSection, HealthInformationSection>() {
            @Override
            public List<HealthInformationSection> mapModels(List<HealthInformationSection> models) {
                return models;
            }
        });
    }


    @Override
    public void persistHealthAttributeTipResponse(HealthAttributeInformationResponse healthAttributeInformationResponse) {
        removeAll(HealthInformationSection.class);
        List<HealthInformationSection> sections = loopSections(new ArrayList<HealthInformationSection>(), healthAttributeInformationResponse.sections, 0);
        if (sections != null) {
            for (HealthInformationSection section : sections) {
                persister.addModel(section);
            }
        }
    }

    private List<HealthInformationSection> loopSections(List<HealthInformationSection> realmSectionList, List<HealthAttributeInformationResponse.Section> sections, int parentTypeKey) {
        if (sections != null) {
            for (HealthAttributeInformationResponse.Section section : sections) {
                if (section.typeKey != null && section.typeKey != parentTypeKey) {
                    realmSectionList.add(new HealthInformationSection(section, parentTypeKey));
                    Log.d("DB_SEction", String.format("%s %d", section.typeKey, parentTypeKey));
                }
                if (section.sections != null && section.sections.size() > 0) {
                    loopSections(realmSectionList, section.sections, section != null ? section.typeKey : 0);
                }
            }
        }
        return realmSectionList;
    }

    private List<HealthAttributeFeedback> addHealthAttributeFeedbacks(List<HealthAttributeFeedbackResponse.HealthAttributeFeedback> healthAttributeFeedbacks) {
        List<HealthAttributeFeedback> healthAttributeFeedbackList = new ArrayList<>();
        for (HealthAttributeFeedbackResponse.HealthAttributeFeedback healthAttributeFeedback : healthAttributeFeedbacks) {
            healthAttributeFeedbackList.add(new HealthAttributeFeedback(healthAttributeFeedback));
        }
        return healthAttributeFeedbackList;
    }


    private <T extends Model> void removeAll(Class<T> aClass) {
        dataStore.removeAll(aClass);
    }

    @Override
    public AttributeDTO getHealthAttributeBySectionTypeKeyAndAttributeTypeKey(int sectionTypeKey, int attributeTypeKey) {
        HealthInformationSectionDTO section = getHealthInformationSectionByTypeKey(sectionTypeKey);
        if (section != null) {
            if (section.getAttributeDTOS() != null && section.getAttributeDTOS().size() > 0) {
                for (AttributeDTO attributeDto : section.getAttributeDTOS()) {
                    if (attributeDto.getAttributeTypeKey() == attributeTypeKey) {
                        return attributeDto;
                    }
                }
            }
        }
        return null;
    }

    private class HealthAttributeComparator implements Comparator<HealthAttributeFeedbackResponse.HealthAttribute> {

        @Override
        public int compare(HealthAttributeFeedbackResponse.HealthAttribute healthAttribute1, HealthAttributeFeedbackResponse.HealthAttribute healthAttribute2) {
            try {
                return healthAttribute2.sourceEventId.compareTo(healthAttribute1.sourceEventId);
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
    }
}
