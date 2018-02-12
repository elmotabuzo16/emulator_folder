package com.vitalityactive.va.persistence.models;

import android.support.annotation.NonNull;

import com.vitalityactive.va.home.service.HomeScreenCardStatusResponse;

import io.realm.RealmList;
import io.realm.RealmObject;

public class HomeCardItem extends RealmObject {

    private int type;
    private int status;
    private String validFrom;
    private String validTo;
    private RealmList<HomeCardMetadata> cardItemMetadatas = new RealmList<>();

    public HomeCardItem(){
    }

    public HomeCardItem(HomeScreenCardStatusResponse.CardItem cardItem) {
        type = cardItem.typeKey;
        status = cardItem.statusTypeKey;
        validFrom = cardItem.validFrom;
        validTo = cardItem.validTo;
        for (HomeScreenCardStatusResponse.Metadata metadata : cardItem.cardItemMetadatas) {
            cardItemMetadatas.add(new HomeCardMetadata(metadata));
        }
    }

    public int getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    @NonNull
    public RealmList<HomeCardMetadata> getCardMetadatas() {
        return cardItemMetadatas;
    }

}
