package com.vitalityactive.va.dto;

import com.vitalityactive.va.home.HomeCardType;
import com.vitalityactive.va.home.HomeSectionType;
import com.vitalityactive.va.persistence.models.HomeCard;
import com.vitalityactive.va.persistence.models.HomeCardItem;
import com.vitalityactive.va.persistence.models.HomeCardMetadata;

import java.util.ArrayList;
import java.util.List;

import static com.vitalityactive.va.utilities.TextUtilities.getIntegerFromString;

public class HomeCardDTO {
    public HomeCardType type = HomeCardType.UNKNOWN;
    public long amountCompleted;
    public HomeSectionType section = HomeSectionType.UNKNOWN;
    public int priority;
    public int potentialPoints;
    public HomeCardType.StatusType status = HomeCardType.StatusType.UNKNOWN;
    public int total;
    public int vitalityStatus;
    public int earnedPoints;
    public String goalEndDate;
    public String goalStartDate;
    public String validTo;
    public List<HomeCardMetadataDTO> cardMetadatas = new ArrayList<>();
    public List<HomeCardItemDTO> cardItems = new ArrayList<>();

    public HomeCardDTO() {

    }

    public HomeCardDTO(HomeCard card) {
        type = HomeCardType.fromTypeKey(card.getType());
        amountCompleted = card.getAmountCompleted();
        section = HomeSectionType.fromValue(card.getSection());
        priority = card.getPriority();
        final String potentialPoints = card.getPotentialPoints();
        this.potentialPoints = getIntegerFromString(potentialPoints);
        status = HomeCardType.StatusType.fromTypeKey(card.getStatus());
        total = getIntegerFromString(card.getTotal());
        vitalityStatus = getIntegerFromString(card.getVitalityStatus());
        earnedPoints = getIntegerFromString(card.getEarnedPoints());
        goalStartDate = card.getGoalStartDate();
        goalEndDate = card.getGoalEndDate();
        validTo = card.getValidTo();
        for (HomeCardMetadata cardMetadata : card.getCardMetadatas()) {
            cardMetadatas.add(new HomeCardMetadataDTO(cardMetadata));
        }
        for (HomeCardItem cardItem : card.getCardItems()) {
            cardItems.add(new HomeCardItemDTO(cardItem));
        }
    }

    public boolean isDone() {
        return status == HomeCardType.StatusType.DONE;
    }

    public boolean hasNotStarted() {
        return status == HomeCardType.StatusType.NOT_STARTED;
    }

    public boolean isInProgress() {
        return status == HomeCardType.StatusType.IN_PROGRESS;
    }

    public boolean isAchieved() {
        return status == HomeCardType.StatusType.ACHIEVED;
    }

    public boolean isActivated() {
        return status == HomeCardType.StatusType.ACTIVATED;
    }

}
