package com.vitalityactive.va.eventsfeed.presentation;

import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.dto.CurrentVitalityMembershipPeriodDTO;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.eventsfeed.EventsFeedCategoriesSelectedEvent;
import com.vitalityactive.va.eventsfeed.EventsFeedContent;
import com.vitalityactive.va.eventsfeed.EventsFeedRefreshCompletedEvent;
import com.vitalityactive.va.eventsfeed.EventsFeedRequestCompletedEvent;
import com.vitalityactive.va.eventsfeed.data.EventsFeedMonth;
import com.vitalityactive.va.eventsfeed.data.dto.EventsFeedCategoryDTO;
import com.vitalityactive.va.eventsfeed.data.net.request.EffectivePeriod;
import com.vitalityactive.va.eventsfeed.data.net.request.EventTypeFilter;
import com.vitalityactive.va.eventsfeed.data.net.request.EventsFeedRequest;
import com.vitalityactive.va.eventsfeed.domain.EventsFeedInteractor;
import com.vitalityactive.va.networking.RequestFailedEvent;
import com.vitalityactive.va.utilities.TimeUtilities;
import com.vitalityactive.va.utilities.date.Date;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventsFeedPresenterImpl extends BasePresenter<EventsFeedPresenter.UserInterface> implements EventsFeedPresenter,
        EventListener<EventsFeedRequestCompletedEvent>{

    private static final int MONTHS = 36;
    private Scheduler scheduler;
    private EventsFeedInteractor interactor;
    private EventDispatcher eventDispatcher;
    private TimeUtilities timeUtilities;

    private int selectedCategoryTypeKey = 999;
    private EventsFeedContent content;

    private String selectedDateFrom;
    private String selectedDateTo;

//    private List<Integer> selectedCategoryTypeKeys = new ArrayList<>();
    private List<EventsFeedCategoryDTO> selectedCategories = new ArrayList<>();
    private PartyInformationRepository partyInformationRepository;


    public EventsFeedPresenterImpl(Scheduler scheduler, EventsFeedInteractor interactor, EventDispatcher eventDispatcher, TimeUtilities timeUtilities, EventsFeedContent content, PartyInformationRepository partyInformationRepository) {
        this.scheduler = scheduler;
        this.interactor = interactor;
        this.eventDispatcher = eventDispatcher;
        this.timeUtilities = timeUtilities;
        this.content = content;
        this.partyInformationRepository = partyInformationRepository;

        eventDispatcher.addEventListener(RequestFailedEvent.class, new EventListener<RequestFailedEvent>() {
            @Override
            public void onEvent(RequestFailedEvent event) {

            }
        });
    }

    public void setCurrentDateSelected(int selectedMonthIndex, String selectedYear){

        int selectedMonth = (getFirstMonthIndex() - (selectedMonthIndex%12)) % 12;
        if(selectedMonth < 1){
            selectedMonth = 12;
        }

        this.selectedDateFrom = String.format("%s-%02d-%02d", selectedYear, selectedMonth, 1);
        Date dateTo = new Date(selectedDateFrom+"T00:00:00+08:00[Asia/Manila]");
        dateTo = dateTo.toLastDayOfMonth();

        this.selectedDateTo = String.format("%s-%02d-%02d", selectedYear, selectedMonth, dateTo.getDayOfMonth());
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        userInterface.showMonths(createMonths());
    }

    private List<EventsFeedMonth> createMonths() {
        // TODO: replace these with localized month names generated with date formatter (hopefully)
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        List<EventsFeedMonth> months = new ArrayList<>();

        for (int i = getFirstMonthIndex(); i < getMonthsOffsetByCurrentMonth(); ++i) {
            int normalisedMonthIndex = getNormalisedMonthIndex(i);
            int year = getYearForRawMonthIndex(i);
            months.add(new EventsFeedMonth(monthNames[normalisedMonthIndex], String.valueOf(year)));
        }
        Collections.reverse(months);
        return months;
    }

    @Override
    public void onUserInterfaceAppeared() {
        super.onUserInterfaceAppeared();
        interactor.setContext(userInterface.getActivityContext());
        eventDispatcher.addEventListener(EventsFeedRequestCompletedEvent.class, this);

        userInterface.showLoadingIndicator();
        if (!interactor.isFetching()) {
            interactor.fetchEventsFeed(getEventsFeedRequest());
        }
    }

    private EventsFeedRequest getEventsFeedRequest(){
        EffectivePeriod effectivePeriod = new EffectivePeriod();

        CurrentVitalityMembershipPeriodDTO vitalityMembershipPeriod = partyInformationRepository.getCurrentVitalityMembershipPeriod();

        if(vitalityMembershipPeriod != null) {

            Date effectiveFrom = new Date(vitalityMembershipPeriod.getEffectiveFrom()+"T00:00:00+08:00[Asia/Manila]").minusMonths(12);//.minusMonths(12);
            Date effectiveTo = new Date(vitalityMembershipPeriod.getEffectiveTo()+"T00:00:00+08:00[Asia/Manila]");

            String effectiveFromStr = String.format("%d-%02d-%02d", effectiveFrom.getYear(), effectiveFrom.getMonth(), effectiveFrom.getDayOfMonth());
            String effectiveToStr = String.format("%d-%02d-%02d", effectiveTo.getYear(), effectiveTo.getMonth(), effectiveTo.getDayOfMonth());


            effectivePeriod.setEffectiveFrom(effectiveFromStr); //currentVitalityMembershipPeriod.effectiveFrom - 12 months
            effectivePeriod.setEffectiveTo(effectiveToStr);
        }

        //set the filter
        EventTypeFilter eventTypeFilter = new EventTypeFilter();
        eventTypeFilter.setTypeKey(3);


        EventsFeedRequest request = new EventsFeedRequest(effectivePeriod, new EventTypeFilter[]{eventTypeFilter}, null);
//      EventsFeedRequest request = new EventsFeedRequest(effectivePeriod, null, null);

        return request;
    }

    @Override
    public void onUserSwipesToRefresh() {
        interactor.refresh(getEventsFeedRequest());
    }

    @Override
    public void onUserSelectsCategory(List<EventsFeedCategoryDTO> selectedCats) {
        if(selectedCats.size() > 0) {
//            selectedCategoryTypeKey = selectedCats.get(0);
//            selectedCategoryTypeKeys = selectedCats;
            selectedCategoryTypeKey = selectedCats.get(0).getTypeKey();
            selectedCategories = selectedCats;
            eventDispatcher.dispatchEvent(new EventsFeedCategoriesSelectedEvent());
        }
    }

    @Override
    public boolean cachedDataExists() {
        return interactor.hasEntries();
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        super.onUserInterfaceDisappeared(isFinishing);
        eventDispatcher.removeEventListener(EventsFeedRequestCompletedEvent.class, this);
    }

    @Override
    public void onEvent(EventsFeedRequestCompletedEvent event) {
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                userInterface.hideLoadingIndicator();
                userInterface.onPointsSuccessfullyLoaded();
                eventDispatcher.dispatchEvent(new EventsFeedRefreshCompletedEvent());
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
    public EventsFeedCategoryDTO getSelectedCategory() {

        if(selectedCategories != null && selectedCategories.size()>0){
            return selectedCategories.get(0);
        }
        return new EventsFeedCategoryDTO(selectedCategoryTypeKey, getCategoryTitle(selectedCategoryTypeKey));

//        return new EventsFeedCategoryDTO(selectedCategoryTypeKey, getCategoryTitle(selectedCategoryTypeKey));
//        return new EventsFeedCategoryDTO(selectedCategoryTypeKey, EventsFeedTitleProvider.getCategoryTitle(selectedCategoryTypeKey));
    }

    @Override
    public List<EventsFeedCategoryDTO> getSelectedCategories() {
        return selectedCategories==null?new ArrayList<EventsFeedCategoryDTO>():selectedCategories;
    }

    private String getCategoryTitle(int key) {
        return content.getEventsFeedEntryCategoryTitle(key);
    }

    //    @Override
//    public List<EventsFeedCategoryDTO> getSelectedCategoriesAsDTO() {
//
//        List<EventsFeedCategoryDTO> selectedEventsFeedCategoryDTOList = new ArrayList<>();
//        for (Integer selectedCatKey: getSelectedCategories()){
//            selectedEventsFeedCategoryDTOList.add(new EventsFeedCategoryDTO(selectedCatKey, getCategoryTitle(selectedCatKey)));
//        }
//
//        return selectedEventsFeedCategoryDTOList;
//    }
}
