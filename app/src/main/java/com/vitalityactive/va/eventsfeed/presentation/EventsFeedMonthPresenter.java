package com.vitalityactive.va.eventsfeed.presentation;

import android.support.annotation.NonNull;

import com.vitalityactive.va.eventsfeed.EventsFeedDay;
import com.vitalityactive.va.eventsfeed.data.dto.EventsFeedCategoryDTO;
import com.vitalityactive.va.utilities.date.Date;

import java.util.List;

public interface EventsFeedMonthPresenter {

    @NonNull
    List<EventsFeedDay> getEventsFeed(Date firstDayOfMonth, Date lastDayOfMonth);
    EventsFeedCategoryDTO getSelectedCategory();
    boolean isCurrentlyRefreshing();

}
