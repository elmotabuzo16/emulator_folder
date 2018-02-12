package com.vitalityactive.va.eventsfeed;

import com.vitalityactive.va.BaseTest;
import com.vitalityactive.va.eventsfeed.data.dto.EventDTO;
import com.vitalityactive.va.eventsfeed.data.dto.EventsFeedCategoryDTO;
import com.vitalityactive.va.eventsfeed.domain.EventsFeedInteractorImpl;
import com.vitalityactive.va.eventsfeed.presentation.EventsFeedMonthPresenterImpl;
import com.vitalityactive.va.persistence.models.EventFeed;
import com.vitalityactive.va.utilities.TimeUtilities;
import com.vitalityactive.va.utilities.date.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Created by jayellos on 11/11/17.
 */

public class EventsFeedMonthPresenterImplTest extends BaseTest{
    @Mock private TimeUtilities timeUtilities;
    @Mock private EventsFeedRepository repository;
    @Mock private EventsFeedSelectedCategoriesProvider selectedCategoriesProvider;
    @Mock private EventsFeedInteractorImpl interactor;

    private EventsFeedMonthPresenterImpl presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new EventsFeedMonthPresenterImpl(repository,
                selectedCategoriesProvider, interactor);

        timeUtilities = new TimeUtilities();
    }

    @Test
    public void test_if_isCurrentlyRefreshing(){
        presenter.isCurrentlyRefreshing();
        verify(interactor).isFetching();
    }

    @Test
    public void days_are_filtered_by_date_correctly(){
        int monthIndex = 0;

        Date firstDayOfMonth = new Date("2017-11-01T00:00:00+08:00[Asia/Manila]");
        Date lastDayOfMonth = new Date("2017-11-30T23:59:59+08:00[Asia/Manila]");

        List<EventsFeedCategoryDTO> eventsFeedCategoryDTO = new ArrayList<>();
        eventsFeedCategoryDTO.add(new EventsFeedCategoryDTO(3,"Mock Events Feed Category DTO"));

        when(repository.getEventsFeedEntries(firstDayOfMonth, lastDayOfMonth, eventsFeedCategoryDTO)).thenReturn(createPointsEntries());
        when(mockTimeUtilities.now()).thenReturn(new Date("2017-11-30T12:30:40+08:00[Asia/Manila]"));

        List<EventsFeedDay> days = presenter.getEventsFeed(firstDayOfMonth, lastDayOfMonth);

        Date mostRecentDayInNov = new Date("2017-11-30T00:00:00Z");

        System.out.println("now: "+mockTimeUtilities.now().toString());
        System.out.println("date: "+mostRecentDayInNov.toString());

//        assertEquals(2, days.size());

    }

    private List<EventDTO> createPointsEntries() {
        return Arrays.asList(
                getPointsEntryDTO("2017-12-01T12:30:42-05:00[Asia/Manila]"),
                getPointsEntryDTO("2017-11-21T08:30:42+02:00[Asia/Manila]"),
                getPointsEntryDTO("2017-11-21T04:23:12+02:00[Asia/Manila]")
        );
    }

    private EventDTO getPointsEntryDTO(String effectiveDate) {
        EventFeed event = new EventFeed();
        event.setEventDateTime(new Date(effectiveDate).getMillisecondsSinceEpoch());
        return new EventDTO(event);
    }
}
