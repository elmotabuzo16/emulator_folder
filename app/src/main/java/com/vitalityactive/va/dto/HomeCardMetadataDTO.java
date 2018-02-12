package com.vitalityactive.va.dto;

import com.vitalityactive.va.home.HomeCardType;
import com.vitalityactive.va.persistence.models.HomeCardMetadata;

public class HomeCardMetadataDTO {
    public HomeCardType.MetadataType type = HomeCardType.MetadataType.UNKNOWN;
    public String value;

    public HomeCardMetadataDTO(HomeCardMetadata cardMetadata) {
        type = HomeCardType.MetadataType.fromTypeKey(cardMetadata.getTypeKey());
        value = cardMetadata.getValue();
    }
}
