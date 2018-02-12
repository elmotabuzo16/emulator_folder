package com.vitalityactive.va.pointsmonitor;

import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.dto.PointsEntryCategoryDTO;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.utilities.TimeUtilities;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class PointsMonitorPresenterImpl extends BasePresenter<PointsMonitorPresenter.UserInterface> implements PointsMonitorPresenter, EventListener<PointsHistoryRequestCompletedEvent> {

    private static final int MONTHS = 36;
    private Scheduler scheduler;
    private PointsMonitorInteractor interactor;
    private EventDispatcher eventDispatcher;
    private TimeUtilities timeUtilities;

    private int selectedCategoryTypeKey = 999;
    private PointsMonitorContent content;

    public PointsMonitorPresenterImpl(Scheduler scheduler, PointsMonitorInteractor interactor, EventDispatcher eventDispatcher, TimeUtilities timeUtilities, PointsMonitorContent content) {
        this.scheduler = scheduler;
        this.interactor = interactor;
        this.eventDispatcher = eventDispatcher;
        this.timeUtilities = timeUtilities;
        this.content = content;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        userInterface.showMonths(createMonths());
    }

    private List<PointsHistoryMonth> createMonths() {
        String[] monthNames = new DateFormatSymbols().getShortMonths();
        List<PointsHistoryMonth> months = new ArrayList<>();
        for (int i = getFirstMonthIndex(); i < getMonthsOffsetByCurrentMonth(); ++i) {
            int normalisedMonthIndex = getNormalisedMonthIndex(i);
            int year = getYearForRawMonthIndex(i);
            months.add(new PointsHistoryMonth(monthNames[normalisedMonthIndex], String.valueOf(year)));
        }
        Collections.reverse(months);
        return months;
    }

    @Override
    public void onUserInterfaceAppeared() {
        super.onUserInterfaceAppeared();
        eventDispatcher.addEventListener(PointsHistoryRequestCompletedEvent.class, this);

        userInterface.showLoadingIndicator();
        if (!interactor.isFetchingPointsHistory()) {
            interactor.fetchPointsHistory();
        }
    }

    @Override
    public void onUserSwipesToRefresh() {
        interactor.refreshPointsHistory();
    }

    @Override
    public void onUserSelectsCategory(int categoryTypeKey) {
        if (selectedCategoryTypeKey == categoryTypeKey) {
            return;
        }
        selectedCategoryTypeKey = categoryTypeKey;
        eventDispatcher.dispatchEvent(new PointsMonitorCategoriesSelectedEvent());
    }

    @Override
    public boolean cachedDataExists() {
        return interactor.hasPointsEntries();
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        super.onUserInterfaceDisappeared(isFinishing);
        eventDispatcher.removeEventListener(PointsHistoryRequestCompletedEvent.class, this);
    }

    @Override
    public void onEvent(PointsHistoryRequestCompletedEvent event) {
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                userInterface.hideLoadingIndicator();
                userInterface.onPointsSuccessfullyLoaded();
                eventDispatcher.dispatchEvent(new PointsMonitorRefreshCompletedEvent());
            }
        });
    }

    private int getFirstMonthIndex() {
        return timeUtilities.getCurrentMonth();
    }

    private int getMonthsOffsetByCurrentMonth() {
        return MONTHS + timeUtilities.getCurrentMonth();
    }

    private int getNormalisedMonthIndex(int monthIndex) {
        return monthIndex % 12;
    }

    private int getYearForRawMonthIndex(int rawMonthIndex) {
        int reversedYearOffset = getMaximumYearOffset() - getYearOffset(rawMonthIndex);
        return timeUtilities.getCurrentYear() - reversedYearOffset;
    }

    private int getMaximumYearOffset() {
        return getYearOffset(getLastMonthIndex());
    }

    private int getYearOffset(int rawMonthIndex) {
        return rawMonthIndex/12;
    }

    private int getLastMonthIndex() {
        return getMonthsOffsetByCurrentMonth() - 1;
    }

    @Override
    public PointsEntryCategoryDTO getSelectedCategory() {
        return new PointsEntryCategoryDTO(selectedCategoryTypeKey, getCategoryTitle());
    }

    private String getCategoryTitle() {
        return content.getPointsEntryCategoryTitle(selectedCategoryTypeKey);
    }
}
