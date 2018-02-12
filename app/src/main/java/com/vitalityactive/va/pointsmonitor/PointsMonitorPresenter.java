package com.vitalityactive.va.pointsmonitor;

import android.support.annotation.NonNull;

import com.vitalityactive.va.Presenter;

import java.util.List;

public interface PointsMonitorPresenter extends Presenter<PointsMonitorPresenter.UserInterface>, PointsMonitorSelectedCategoriesProvider {

    void onUserInterfaceAppeared();

    void onUserSwipesToRefresh();

    void onUserSelectsCategory(int identifier);

    boolean cachedDataExists();

    interface UserInterface extends Presenter.UserInterface {

        void showMonths(@NonNull List<PointsHistoryMonth> months);

        void onPointsSuccessfullyLoaded();
    }
}
