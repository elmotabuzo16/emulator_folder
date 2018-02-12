package com.vitalityactive.va.eventsfeed;

import com.vitalityactive.va.eventsfeed.data.dto.EventsFeedCategoryDTO;

import java.util.List;

public interface EventsFeedAvailableEventsCategoriesProvider {

    List<EventsFeedCategoryDTO> getEventsFeedEntryCategories();
}
