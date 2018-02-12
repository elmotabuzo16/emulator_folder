package com.vitalityactive.va.dto;

import com.vitalityactive.va.home.HomeCardItemType;
import com.vitalityactive.va.persistence.models.HomeCardMetadata;

public class HomeCardItemMetadataDTO {
    public HomeCardItemType.MetadataType type = HomeCardItemType.MetadataType.UNKNOWN;
    public String value;

    public HomeCardItemMetadataDTO(HomeCardMetadata cardMetadata) {
        type = HomeCardItemType.MetadataType.fromTypeKey(cardMetadata.getTypeKey());
        value = cardMetadata.getValue();
    }
}
