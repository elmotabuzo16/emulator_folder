package com.vitalityactive.va.eventsfeed.domain;

import android.content.Context;

import com.vitalityactive.va.eventsfeed.data.net.request.EventsFeedRequest;

public interface EventsFeedInteractor {

    boolean hasEntries();

    boolean isFetching();

    void fetchEventsFeed(EventsFeedRequest requestBody);

    void refresh(EventsFeedRequest requestBody);

    void setContext(Context context);

}
