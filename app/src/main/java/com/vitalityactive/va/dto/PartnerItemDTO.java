package com.vitalityactive.va.dto;

import com.vitalityactive.va.partnerjourney.models.PartnerItem;

public class PartnerItemDTO {

    public final long id;
    public final String title;
    public final String details;
    public final String logoFileName;
    public final String longDescription;

    public PartnerItemDTO(PartnerItem partnerItem) {
        this.id = partnerItem.id;
        this.title = partnerItem.title;
        this.details = partnerItem.details;
        this.logoFileName = partnerItem.logoFileName;
        this.longDescription = partnerItem.longDescription;
    }
}
