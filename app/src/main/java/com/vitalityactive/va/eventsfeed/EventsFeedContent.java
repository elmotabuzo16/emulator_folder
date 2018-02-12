package com.vitalityactive.va.eventsfeed;

import android.support.annotation.NonNull;

public interface EventsFeedContent {
    @NonNull
    String getEventsFeedEntryCategoryTitle(int categoryTypeKey);
}
