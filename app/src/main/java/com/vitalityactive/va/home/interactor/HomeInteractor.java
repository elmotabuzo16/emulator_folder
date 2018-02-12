package com.vitalityactive.va.home.interactor;

import android.support.annotation.IntDef;

import com.vitalityactive.va.dto.HomeCardDTO;
import com.vitalityactive.va.dto.RewardHomeCardDTO;
import com.vitalityactive.va.home.HomeCardType;

import java.lang.annotation.Retention;
import java.util.List;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public interface HomeInteractor {
    int UNKNOWN = 0;
    int NOT_STARTED = 1;
    int STARTED = 2;

    @NavigationMode
    int getVhrStatus();

    boolean isRequestInProgress();

    void fetchHomeCards();

    void checkEventStatusByPartyId(int eventTypeKey);

    void clearVhrStatus();

    List<HomeCardDTO> getHomeCards();

    List<RewardHomeCardDTO> getRewardHomeCards(HomeCardType homeCardType);

    @Retention(SOURCE)
    @IntDef({UNKNOWN, NOT_STARTED, STARTED})
    @interface NavigationMode {
    }
}
