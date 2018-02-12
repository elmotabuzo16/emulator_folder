package com.vitalityactive.va.eventsfeed.presentation;

import android.content.Context;
import android.support.annotation.NonNull;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.eventsfeed.EventsFeedSelectedCategoriesProvider;
import com.vitalityactive.va.eventsfeed.data.EventsFeedMonth;
import com.vitalityactive.va.eventsfeed.data.dto.EventsFeedCategoryDTO;

import java.util.List;

public interface EventsFeedPresenter extends Presenter<EventsFeedPresenter.UserInterface>, EventsFeedSelectedCategoriesProvider {

    void onUserInterfaceAppeared();

    void onUserSwipesToRefresh();

    void onUserSelectsCategory(List<EventsFeedCategoryDTO> selectedCats);

    boolean cachedDataExists();

    void setCurrentDateSelected(int selectedMonthIndex, String selectedYear);

    interface UserInterface extends Presenter.UserInterface {

        void showMonths(@NonNull List<EventsFeedMonth> months);

        void onPointsSuccessfullyLoaded();

        Context getActivityContext();
    }
}
