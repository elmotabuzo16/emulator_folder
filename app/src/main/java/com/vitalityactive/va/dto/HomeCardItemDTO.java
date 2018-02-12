package com.vitalityactive.va.dto;

import com.vitalityactive.va.home.HomeCardItemType;
import com.vitalityactive.va.persistence.models.HomeCardItem;
import com.vitalityactive.va.persistence.models.HomeCardMetadata;

import java.util.ArrayList;
import java.util.List;

public class HomeCardItemDTO {

    public HomeCardItemType type = HomeCardItemType.UNKNOWN;
    public HomeCardItemType.StatusType status = HomeCardItemType.StatusType.UNKNOWN;
    public String validTo;
    public List<HomeCardItemMetadataDTO> cardItemMetadatas = new ArrayList<>();

    public HomeCardItemDTO(HomeCardItem cardItem) {
        type = HomeCardItemType.fromTypeKey(cardItem.getType());
        status = HomeCardItemType.StatusType.fromTypeKey(cardItem.getStatus());
        validTo = cardItem.getValidTo();
        for (HomeCardMetadata cardMetadata : cardItem.getCardMetadatas()) {
            cardItemMetadatas.add(new HomeCardItemMetadataDTO(cardMetadata));
        }
    }

    public boolean isLinked() {
        return status == HomeCardItemType.StatusType.LINKED;
    }

    public boolean isAvailable() {
        return status == HomeCardItemType.StatusType.AVAILABLE;
    }

    public boolean isExpired() {
        return status == HomeCardItemType.StatusType.EXPIRED;
    }

    public boolean isRedeemed() {
        return status == HomeCardItemType.StatusType.REDEEMED;
    }

    public boolean isDone() {
        return status == HomeCardItemType.StatusType.DONE;
    }

}
