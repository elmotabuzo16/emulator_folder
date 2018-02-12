package com.vitalityactive.va.eventsfeed;

import com.vitalityactive.va.eventsfeed.data.dto.EventsFeedCategoryDTO;

import java.util.List;

public interface EventsFeedSelectedCategoriesProvider {
    EventsFeedCategoryDTO getSelectedCategory();
    List<EventsFeedCategoryDTO> getSelectedCategories();
    
}
