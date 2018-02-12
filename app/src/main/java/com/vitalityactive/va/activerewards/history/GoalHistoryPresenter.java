package com.vitalityactive.va.activerewards.history;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.activerewards.dto.GoalTrackerOutDto;

import java.util.List;
import java.util.Map;

public interface GoalHistoryPresenter extends Presenter<GoalHistoryPresenter.UserInterface> {
    void loadNextPage();
    Map<String, List<GoalTrackerOutDto>> getActivityHistory();
    boolean isPageLoading();
    boolean isLastPage();

    interface UserInterface {
        void showLoadingIndicator();
        void hideLoadingIndicator();
        void showPagingLoadingIndicator();
        void hidePagingLoadingIndicator();
        void showConnectionErrorMessage(boolean fullscreen);
        void showGenericErrorMessage(boolean fullscreen);
        void showNoMoreActivityView();
        void showEmptyView();
        void updateRecyclerView();
    }
}
