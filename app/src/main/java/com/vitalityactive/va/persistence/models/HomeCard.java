package com.vitalityactive.va.persistence.models;

import android.support.annotation.NonNull;

import com.vitalityactive.va.constants.CardItemStatusType;
import com.vitalityactive.va.constants.CardMetadataType;
import com.vitalityactive.va.constants.CardStatusType;
import com.vitalityactive.va.home.HomeCardType;
import com.vitalityactive.va.home.service.HomeScreenCardStatusResponse;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.utilities.date.LocalDate;

import java.util.ListIterator;

import io.realm.RealmList;
import io.realm.RealmObject;

public class HomeCard extends RealmObject implements Model {
    @NonNull
    private String goalEndDate = LocalDate.now().toString();
    @NonNull
    private String goalStartDate = LocalDate.now().toString();
    @NonNull
    private String validTo = LocalDate.now().toString();
    private String type;
    private long amountCompleted;
    private String section;
    private int priority;
    private String potentialPoints;
    private String vitalityStatus;
    private int status;
    private String total;
    private String earnedPoints;
    private RealmList<HomeCardMetadata> cardMetadatas = new RealmList<>();
    private RealmList<HomeCardItem> cardItems = new RealmList<>();

    public HomeCard() {

    }

    public HomeCard(HomeScreenCardStatusResponse.Card card, String section) {
        amountCompleted = card.amountCompleted;
        type = card.typeKey;
        priority = card.priority;
        status = card.statusTypeKey;
        total = card.total;
        validTo = card.validTo;
        this.section = section;
        for (HomeScreenCardStatusResponse.Metadata metadata : card.cardMetadatas) {
            cardMetadatas.add(new HomeCardMetadata(metadata));
            switch (metadata.typeKey) {
                case CardMetadataType._POTENTIALPOINTS:
                    potentialPoints = metadata.value;
                    break;
                case CardMetadataType._EARNEDPOINTS:
                    earnedPoints = metadata.value;
                    break;
                case CardMetadataType._GOALSTARTDATE:
                    if (metadata.value != null) {
                        goalStartDate = metadata.value;
                    }
                    break;
                case CardMetadataType._GOALENDDATE:
                    if (metadata.value != null) {
                        goalEndDate = metadata.value;
                    }
                    break;
            }
        }
        for (HomeScreenCardStatusResponse.CardItem cardItem : card.cardItems) {
            cardItems.add(new HomeCardItem(cardItem));
        }
    }

    public static HomeCard create(HomeScreenCardStatusResponse.Card card, String section) {
        if (HomeCardType.REWARDS.getTypeKey().equals(card.typeKey)) {
            ListIterator<HomeScreenCardStatusResponse.CardItem> listIterator = card.cardItems.listIterator();
            while(listIterator.hasNext()) {
                HomeScreenCardStatusResponse.CardItem cardItem = listIterator.next();
                if (new LocalDate(cardItem.validTo).isBefore(LocalDate.now()) ||
                        cardItem.statusTypeKey != CardItemStatusType._AVAILABLE) {
                    listIterator.remove();
                }
            }
        }
        return new HomeCard(card, section);
    }

    public long getAmountCompleted() {
        return amountCompleted;
    }

    public String getSection() {
        return section;
    }

    public String getType() {
        return type;
    }

    public int getPriority() {
        return priority;
    }

    public String getPotentialPoints() {
        return potentialPoints;
    }

    public int getStatus() {
        return status;
    }

    public String getVitalityStatus() {
        return vitalityStatus;
    }

    public String getEarnedPoints() {
        return earnedPoints;
    }

    public String getTotal() {
        return total;
    }

    @NonNull
    public String getGoalEndDate() {
        return goalEndDate;
    }

    @NonNull
    public String getGoalStartDate() {
        return goalStartDate;
    }

    @NonNull
    public String getValidTo() {
        return validTo;
    }

    @NonNull
    public RealmList<HomeCardMetadata> getCardMetadatas() {
        return cardMetadatas;
    }

    @NonNull
    public RealmList<HomeCardItem> getCardItems() {
        return cardItems;
    }

}
