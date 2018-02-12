package com.vitalityactive.va.partnerjourney.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vitalityactive.va.dto.PartnerItemDTO;
import com.vitalityactive.va.partnerjourney.models.PartnerGroup;
import com.vitalityactive.va.partnerjourney.models.PartnerItem;
import com.vitalityactive.va.partnerjourney.models.PartnerItemDetails;
import com.vitalityactive.va.persistence.DataStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PartnerRepository {
    private final DataStore dataStore;

    public PartnerRepository(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public boolean persist(List<PartnerGroup> groups) {
        return dataStore.replaceAll(PartnerGroup.class, groups);
    }

    public boolean persist(PartnerItemDetails details) {
        ArrayList<PartnerItemDetails> items = new ArrayList<>(Collections.singleton(details));
        return dataStore.addOrUpdate(items);
    }

    public String getName(long id) {
        return dataStore.getFieldValue(PartnerItem.class, "id", id, new DataStore.FieldAccessor<PartnerItem, String>() {
            @NonNull
            @Override
            public String getField(@Nullable PartnerItem model) {
                if (model == null || model.title == null)
                    return "";
                return model.title;
            }
        });
    }

    public PartnerItemDetails getDetails(long id) {
        return dataStore.getModelInstance(PartnerItemDetails.class, new DataStore.SelfModelMapper<PartnerItemDetails>() {
            @Override
            public PartnerItemDetails mapModel(PartnerItemDetails model) {
                return PartnerItemDetails.copy(model);
            }
        }, "id", id);
    }

    public PartnerItemDTO getPartnerItem(long partnerId) {
        return dataStore.getModelInstance(PartnerItem.class, new DataStore.ModelMapper<PartnerItem, PartnerItemDTO>() {
            @Override
            public PartnerItemDTO mapModel(PartnerItem model) {
                return new PartnerItemDTO(model);
            }
        }, "id", partnerId);
    }

    public List<PartnerItemDTO> getPartnerItems(int categoryTypeKey) {
        return dataStore.getModelInstance(PartnerGroup.class, new DataStore.ModelMapper<PartnerGroup, List<PartnerItemDTO>>() {
            @Override
            public List<PartnerItemDTO> mapModel(PartnerGroup partnerGroup) {
                List<PartnerItemDTO> partnerItemDTOS = new ArrayList<>();
                for (PartnerItem partnerItem : partnerGroup.items) {
                    partnerItemDTOS.add(new PartnerItemDTO(partnerItem));
                }
                return partnerItemDTOS;
            }
        }, "type", categoryTypeKey);
    }
}
