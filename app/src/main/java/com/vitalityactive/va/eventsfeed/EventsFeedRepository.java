package com.vitalityactive.va.eventsfeed;

import android.support.annotation.NonNull;

import com.vitalityactive.va.constants.ProductFeatureLinkType;
import com.vitalityactive.va.eventsfeed.data.dto.EventDTO;
import com.vitalityactive.va.eventsfeed.data.dto.EventsFeedCategoryDTO;
import com.vitalityactive.va.eventsfeed.data.net.response.EventResponse;
import com.vitalityactive.va.eventsfeed.data.net.response.EventsFeedResult;
import com.vitalityactive.va.networking.parsing.Persister;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.models.EventFeed;
import com.vitalityactive.va.persistence.models.FeatureLink;
import com.vitalityactive.va.utilities.date.Date;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.RealmQuery;

@Singleton
public class EventsFeedRepository implements EventsFeedAvailableEventsCategoriesProvider{

    private final Persister persister;
    private EventsFeedContent content;
    private DataStore dataStore;

    @Inject
    EventsFeedRepository(DataStore dataStore, EventsFeedContent content) {
        this.dataStore = dataStore;
        this.content = content;
        persister = new Persister(dataStore);
    }

    //EventsFeedAvailableEventsCategoriesProvider
    @Override
    public List<EventsFeedCategoryDTO> getEventsFeedEntryCategories() {

        List<EventsFeedCategoryDTO> categories = dataStore.getModels(FeatureLink.class, new DataStore.QueryExecutor<FeatureLink, RealmQuery<FeatureLink>>() {
            @Override
            public List<FeatureLink> executeQueries(RealmQuery<FeatureLink> initialQuery) {
                return initialQuery.equalTo("typeKey", ProductFeatureLinkType._EVENTCATEGORY).findAll();
            }
        }, new DataStore.ModelListMapper<FeatureLink, EventsFeedCategoryDTO>() {
            @Override
            public List<EventsFeedCategoryDTO> mapModels(List<FeatureLink> models) {
                List<EventsFeedCategoryDTO> categories = new ArrayList<>();

                for (FeatureLink featureLink : models) {

                    if(featureLink.getProductFeature().getFeatureType() == 20) {
                        categories.add(new EventsFeedCategoryDTO(featureLink.getLinkedKey(), featureLink.getProductFeature().getTypeName()));
//                        categories.add(new EventsFeedCategoryDTO(featureLink.getLinkedKey(), featureLink.getProductFeature().getTypeCode()));
//                        categories.add(new EventsFeedCategoryDTO(featureLink.getLinkedKey(), getCategoryTitle(featureLink.getLinkedKey())));
                    }
                }
                return categories;
            }
        });

        //Notes: remove the sorting ~jay
//        sortEventsFeedCategories(categories);

        return categories;
    }

    public boolean hasEntries() {
        return dataStore.hasModelInstance(EventFeed.class);
    }


    @NonNull
    public List<EventDTO> getEventsFeedEntries(final Date firstDayOfMonth, final Date lastDayOfMonth, final List<EventsFeedCategoryDTO> selectedCategories) {

        return dataStore.getModels(EventFeed.class, new DataStore.QueryExecutor<EventFeed, RealmQuery<EventFeed>>() {
            @Override
            public List<EventFeed> executeQueries(RealmQuery<EventFeed> initialQuery) {
                initialQuery = initialQuery.between("eventDateTime", firstDayOfMonth.getMillisecondsSinceEpoch(), lastDayOfMonth.getMillisecondsSinceEpoch());

                if (selectedCategories.size() < 1 || isAll(selectedCategories)) {
                    return initialQuery.findAll();
                }
                if (isOther(selectedCategories)) {
                    List<Integer> availableCategoryTypeKeys = getAvailableCategoryTypeKeys();
                    if (availableCategoryTypeKeys.isEmpty()) {
                        return initialQuery.findAll();
                    }
                    return initialQuery.not().in("categoryKey", availableCategoryTypeKeys.toArray(new Integer[availableCategoryTypeKeys.size()])).findAll();
                }
                return initialQuery.in("categoryKey", listToArray(selectedCategories)).findAll();

            }
        }, new DataStore.ModelListMapper<EventFeed, EventDTO>() {
            @Override
            public List<EventDTO> mapModels(List<EventFeed> models) {
                List<EventDTO> mappedModels = new ArrayList<>();
                for (EventFeed event : models) {
                    mappedModels.add(new EventDTO(event));
                }
                return mappedModels;
            }
        });
    }

    private Integer[] listToArray(List<EventsFeedCategoryDTO> selectedCategories){

        Integer[] list = new Integer[selectedCategories.size()];
        for (int i=0; i<selectedCategories.size(); i++){
            EventsFeedCategoryDTO dto = selectedCategories.get(i);

            list[i] = dto.getTypeKey();
        }

        return list;
    }

    private boolean isAll(List<EventsFeedCategoryDTO> selectedCategories){

        for (EventsFeedCategoryDTO categoryDTO: selectedCategories){
            if(categoryDTO.isAll()){
                return true;
            }
        }

        return false;
    }

    private boolean isOther(List<EventsFeedCategoryDTO> selectedCategories){

        for (EventsFeedCategoryDTO categoryDTO: selectedCategories){
            if(categoryDTO.isOther()){
                return true;
            }
        }

        return false;
    }

    private List<Integer> getAvailableCategoryTypeKeys() {
        ArrayList<Integer> typeKeys = new ArrayList<>();
        for (EventsFeedCategoryDTO category : getEventsFeedEntryCategories()) {
            typeKeys.add(category.getTypeKey());
        }
        return typeKeys;
    }

    public boolean persistResponse(EventsFeedResult response) {
        removeEventsFeed();
        persistEvents(response);
        return true;
    }

    private void removeEventsFeed() {
        dataStore.removeAll(EventFeed.class);
    }

    private boolean persistEvents(EventsFeedResult response) {
        if(response.getEventResponses() == null){
            return false;
        }

        for (EventResponse eventResponse : response.getEventResponses()) {
            persister.addModel(new EventFeed(eventResponse));
        }

        return true;
    }

    @NonNull
    private String getCategoryTitle(int typeKey) {
        return content.getEventsFeedEntryCategoryTitle(typeKey);
    }
}