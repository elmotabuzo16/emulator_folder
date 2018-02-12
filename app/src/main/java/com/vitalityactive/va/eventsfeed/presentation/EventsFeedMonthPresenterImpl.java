package com.vitalityactive.va.eventsfeed.presentation;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.vitalityactive.va.eventsfeed.EventsFeedDay;
import com.vitalityactive.va.eventsfeed.EventsFeedRepository;
import com.vitalityactive.va.eventsfeed.EventsFeedSelectedCategoriesProvider;
import com.vitalityactive.va.eventsfeed.data.dto.EventDTO;
import com.vitalityactive.va.eventsfeed.data.dto.EventsFeedCategoryDTO;
import com.vitalityactive.va.eventsfeed.domain.EventsFeedInteractor;
import com.vitalityactive.va.eventsfeed.domain.EventsFeedInteractorImpl;
import com.vitalityactive.va.utilities.date.Date;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by jayellos on 11/11/17.
 */

public class EventsFeedMonthPresenterImpl implements EventsFeedMonthPresenter{

    private final EventsFeedRepository repository;
    private EventsFeedSelectedCategoriesProvider selectedCategoriesProvider;
    private final EventsFeedInteractor interactor;

    @Inject
    public EventsFeedMonthPresenterImpl(EventsFeedRepository repository,
                                        EventsFeedSelectedCategoriesProvider selectedCategoriesProvider,
                                        EventsFeedInteractorImpl interactor) {
        this.repository = repository;
        this.selectedCategoriesProvider = selectedCategoriesProvider;
        this.interactor = interactor;
    }

    public List<EventDTO> getEventsFeedEntriesDTO(Date firstDayOfMonth, Date lastDayOfMonth){
        return repository.getEventsFeedEntries(firstDayOfMonth, lastDayOfMonth, selectedCategoriesProvider.getSelectedCategories());
    }

    @NonNull
    public List<EventsFeedDay> getEventsFeed(Date firstDayOfMonth, Date lastDayOfMonth) {
        List<EventDTO> events = getEventsFeedEntriesDTO(firstDayOfMonth, lastDayOfMonth);

        return filterEventsFeedByDay(events);
    }

    private List<EventsFeedDay> filterEventsFeedByDay(List<EventDTO> eventsFeedEntries) {

        @SuppressLint("UseSparseArrays") HashMap<Integer, List<EventDTO>> filtered = new HashMap<>();


        for (EventDTO eventsFeedEntry : eventsFeedEntries) {

            //if day not exist, create key of the day
            if (!filtered.containsKey(eventsFeedEntry.getEventDateTime().getDayOfMonth())) {
                filtered.put(eventsFeedEntry.getEventDateTime().getDayOfMonth(), new ArrayList<EventDTO>());
            }
            //then add value to the key

            boolean exists = false;
            for (EventDTO entry : filtered.get(eventsFeedEntry.getEventDateTime().getDayOfMonth())) {
                if(entry.getCategoryKey() == eventsFeedEntry.getCategoryKey()){
                    exists = true;
                    break;
                }
            }

            if(!exists) {
                filtered.get(eventsFeedEntry.getEventDateTime().getDayOfMonth()).add(eventsFeedEntry);
            }

//            filtered.get(eventsFeedEntry.getEventDateTime().getDayOfMonth()).add(eventsFeedEntry);
        }

        //     return 1 if b should be before a
        //     return -1 if a should be before b
        //     return 0 otherwise
        List<EventsFeedDay> days = new ArrayList<>();
        for (List<EventDTO> dayEventsFeedEntries : filtered.values()) {
            Collections.sort(dayEventsFeedEntries, new Comparator<EventDTO>() {
                @Override
                public int compare(EventDTO pointsEntry1, EventDTO pointsEntry2) {
                    return -pointsEntry1.getEventDateTime().compareTo(pointsEntry2.getEventDateTime());
                }
            });
            days.add(new EventsFeedDay(dayEventsFeedEntries.get(0).getEventDateTime(), dayEventsFeedEntries));
        }


        Collections.sort(days, new Comparator<EventsFeedDay>() {
            @Override
            public int compare(EventsFeedDay o1, EventsFeedDay o2) {
                return -o1.getDate().compareTo(o2.getDate());
            }
        });
        return days;
    }



    public EventsFeedCategoryDTO getSelectedCategory() {
        return selectedCategoriesProvider.getSelectedCategory();
    }

    public boolean isCurrentlyRefreshing() {
        return interactor.isFetching();
    }

}
