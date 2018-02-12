package com.vitalityactive.va.activerewards.history;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.activerewards.dto.GoalTrackerActiveDto;
import com.vitalityactive.va.activerewards.dto.GoalTrackerOutDto;
import com.vitalityactive.va.activerewards.landing.repository.GoalsAndProgressRepository;
import com.vitalityactive.va.activerewards.landing.service.EffectiveDate;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.model.goalandprogress.GetGoalProgressAndDetailsResponse;
import com.vitalityactive.va.activerewards.CalendarUtils;
import com.vitalityactive.va.utilities.GoalTrackerEffectiveToComparator;
import com.vitalityactive.va.utilities.date.NonUserFacingDateFormatter;
import com.vitalityactive.va.utilities.date.LocalDate;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class GoalHistoryPresenterImpl implements GoalHistoryPresenter {
    public final static int FEED_SIZE = 3; // in month
    private final static String TAG = "AR_History";
    private final static boolean SAVE_TO_DB = true;
    private final GoalHistoryInteractor interactor;
    private final EventDispatcher eventDispatcher;
    private final GoalHistoryRepository goalHistoryRepository;
    private final GoalsAndProgressRepository goalsAndProgressRepository;
    private final MainThreadScheduler scheduler;
    private UserInterface userInterface;
    private EventListener<GoalsAndProgressLoadedHistoryEvent> activityFeedEventListener;
    private String lastShownDate; // last day in displayed range. Should be updated together with adding feed to activityHistory
    // Used to generate range for next request
    private Map<String, List<GoalTrackerOutDto>> activityHistory;
    private boolean isLoading;

    private final DateFormattingUtilities dateFormattingUtilities;

    public GoalHistoryPresenterImpl(@NonNull GoalHistoryInteractor interactor,
                                    @NonNull EventDispatcher eventDispatcher,
                                    @NonNull MainThreadScheduler scheduler,
                                    @NonNull GoalHistoryRepository goalHistoryRepository,
                                    @NonNull GoalsAndProgressRepository goalsAndProgressRepository,
                                    @NonNull DateFormattingUtilities dateFormattingUtilities) {
        this.interactor = interactor;
        this.eventDispatcher = eventDispatcher;
        this.scheduler = scheduler;
        this.goalHistoryRepository = goalHistoryRepository;
        this.goalsAndProgressRepository = goalsAndProgressRepository;
        this.dateFormattingUtilities = dateFormattingUtilities;

//        createEventListeners();
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        createEventListeners();
        activityHistory = new LinkedHashMap<>();
        loadNextPage();
    }

    @Override
    public void onUserInterfaceAppeared() {
        addEventListeners();
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        interactor.stopRequest();
        removeEventListeners();
    }

    @Override
    public void onUserInterfaceDestroyed() {

    }

    private void createEventListeners() {
        activityFeedEventListener = new EventListener<GoalsAndProgressLoadedHistoryEvent>() {
            @Override
            public void onEvent(final GoalsAndProgressLoadedHistoryEvent event) {
                scheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        hideProgress();
                        hidePagingProgress();
                        Log.d(TAG, "onEvent " + event.isSuccessful());
                        if (event.isSuccessful()) {
                            isLoading = false;
                            handleSuccessfulResponse(event);

                            if (isResponseEmpty(event.getResponseBody())) {
                                if (activityHistory.isEmpty()) {
                                    userInterface.showEmptyView();
                                } else {
                                    userInterface.showNoMoreActivityView();
                                }
                            }
                        } else if (activityHistory.isEmpty()) {
                            // any non-successful response while existing data is empty -> show EmptyView
                            userInterface.showEmptyView();
                        } else if (event.getRequestResult().equals(RequestResult.GENERIC_ERROR)) {
                            userInterface.showGenericErrorMessage(true);
                        } else if (event.getRequestResult().equals(RequestResult.CONNECTION_ERROR)) {
                            userInterface.showConnectionErrorMessage(true);
                        }
                    }
                });
            }
        };
    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public void loadNextPage() {
        try {
            EffectiveDate requestedRange = CalendarUtils.getRangeForRequest(lastShownDate == null ? CalendarUtils.getTomorrow() : lastShownDate);
            loadFeedFromServer(requestedRange, SAVE_TO_DB, activityHistory.isEmpty());
        } catch (ParseException e) {
            Log.e(TAG, "Exception in parsing the date: " + e.getMessage());
            userInterface.showGenericErrorMessage(false);
        }
    }

    private synchronized void loadFeedFromServer(EffectiveDate range,
                                    boolean saveResultToDb,
                                    boolean showProgress) {
        if(!isLoading) {
            isLoading = true;
            interactor.loadGoalProgressAndDetails(range.effectiveFrom, range.effectiveTo, saveResultToDb);
            if (showProgress) {
                showProgress();
            }
        }
    }

    @Override
    public Map<String, List<GoalTrackerOutDto>> getActivityHistory() {
        return activityHistory;
    }

    public boolean isPageLoading() {
        return isLoading;
    }

    public boolean isLastPage() {
        if(lastShownDate == null){
            return false;
        } else {
            try {
                return NonUserFacingDateFormatter.javaUtilDateFromYearMonthDayString(lastShownDate).getTime() <= getLastPageDate().getTime();
            } catch (ParseException e) {
                Log.e("AR_HISTORY", "wrong date format: " + e.toString());
                return true;
            }
        }
    }

    private Date getLastPageDate() {
        final Date twoYearsBorder = CalendarUtils.getLastSupportedDate();
        GoalTrackerActiveDto goalTrackerActiveDto = goalsAndProgressRepository.getGoalTrackerActive();
        if (goalTrackerActiveDto != null) {
            String validFrom = goalTrackerActiveDto.getValidFrom();
            try {
                Date dateValidFrom = NonUserFacingDateFormatter.javaUtilDateFromYearMonthDayString(validFrom);
                return new Date(Math.max(twoYearsBorder.getTime(), dateValidFrom.getTime()));
            } catch (ParseException e) {
                Log.e("AR_HISTORY", "wrong date format: " + e.toString());
                return twoYearsBorder;
            }
        } else {
            return twoYearsBorder;
        }
    }

    private void addEventListeners() {
        eventDispatcher.addEventListener(GoalsAndProgressLoadedHistoryEvent.class, activityFeedEventListener);
    }

    private void removeEventListeners() {
        eventDispatcher.removeEventListener(GoalsAndProgressLoadedHistoryEvent.class, activityFeedEventListener);
    }

    private boolean isResponseEmpty(GetGoalProgressAndDetailsResponse response) {
        return (response == null ||
                response.getGoalProgressAndDetailsResponse == null ||
                response.getGoalProgressAndDetailsResponse.goalTrackerOuts == null ||
                response.getGoalProgressAndDetailsResponse.goalTrackerOuts.isEmpty());
    }

    private void handleSuccessfulResponse(@NonNull final GoalsAndProgressLoadedHistoryEvent event) {
        List<GoalTrackerOutDto> newFeed = new ArrayList<>();
        try {
            EffectiveDate requestedRange = CalendarUtils.getFirstDayOfMonth(lastShownDate == null ? CalendarUtils.getTomorrow() : lastShownDate);

            if(event.getResponseBody() != null && event.getResponseBody().getGoalProgressAndDetailsResponse != null
                    && event.getResponseBody().getGoalProgressAndDetailsResponse.goalTrackerOuts != null) {
                List<GetGoalProgressAndDetailsResponse.GoalTrackerOut> goalTrackerOuts = event.getResponseBody().getGoalProgressAndDetailsResponse.goalTrackerOuts;
                if (goalTrackerOuts != null && !goalTrackerOuts.isEmpty()) {
                    Collections.sort(goalTrackerOuts, new GoalTrackerEffectiveToComparator());
                    System.out.println(goalTrackerOuts);
                    for (GetGoalProgressAndDetailsResponse.GoalTrackerOut entry : goalTrackerOuts) {
                        if (inCurrentPeriod(entry, requestedRange)) {
                            newFeed.add(new GoalTrackerOutDto(entry));
                        } else {
                            requestedRange = saveFeedAndBeginNewInterval(requestedRange.effectiveTo, newFeed, new GoalTrackerOutDto(entry));
                        }
                    }
                }

                saveFeedAndBeginNewInterval(requestedRange.effectiveTo, newFeed, null);

                if (!TextUtils.isEmpty(event.getEffectiveFrom())) {
                    lastShownDate = event.getEffectiveFrom();
                }
            }
            userInterface.updateRecyclerView();
        } catch (ParseException e) {
            Log.e(TAG, "Exception in parsing the date: " + e.getMessage());
            // NOP
        }

    }

    private boolean inCurrentPeriod(GetGoalProgressAndDetailsResponse.GoalTrackerOut entry,
                                    EffectiveDate range) throws ParseException {
        return NonUserFacingDateFormatter.javaUtilDateFromYearMonthDayString(entry.effectiveTo).after(
                NonUserFacingDateFormatter.javaUtilDateFromYearMonthDayString(range.effectiveFrom));
    }

    private EffectiveDate saveFeedAndBeginNewInterval(String prevDate,
                                                      List<GoalTrackerOutDto> feed,
                                                      GoalTrackerOutDto newEntry) throws ParseException {
        EffectiveDate newRange = CalendarUtils.getFirstDayOfMonth(prevDate == null ? CalendarUtils.getTomorrow() : prevDate);
        if (!feed.isEmpty()) {
            String month = dateFormattingUtilities.formatMonthYear(new LocalDate(newRange.effectiveTo));
            activityHistory.put(month, new ArrayList<>(new LinkedHashSet<>(feed)));
            feed.clear();
        }

        if(newEntry == null){
            return CalendarUtils.getFirstDayOfMonth(prevDate);
        } else {
            feed.add(newEntry);
            return CalendarUtils.getFirstDayOfMonth(newEntry.getEffectiveTo());
        }
    }

    private void showProgress() {
        userInterface.showLoadingIndicator();
    }

    private void hideProgress() {
        userInterface.hideLoadingIndicator();
    }

    private void hidePagingProgress() {
        userInterface.hidePagingLoadingIndicator();
    }
}
