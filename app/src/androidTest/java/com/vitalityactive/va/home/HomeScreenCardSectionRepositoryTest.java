package com.vitalityactive.va.home;

import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.constants.CardStatusType;
import com.vitalityactive.va.home.repository.HomeScreenCardSectionRepository;
import com.vitalityactive.va.home.repository.HomeScreenCardSectionRepositoryImpl;
import com.vitalityactive.va.home.service.HomeScreenCardStatusResponse;
import com.vitalityactive.va.persistence.models.HomeCard;
import com.vitalityactive.va.testutilities.ModelVerifier;
import com.vitalityactive.va.testutilities.annotations.RepositoryTests;
import com.vitalityactive.va.utilities.date.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@RepositoryTests
public class HomeScreenCardSectionRepositoryTest extends RepositoryTestBase {
    private HomeScreenCardSectionRepository repository;

    public void setUp() throws IOException {
        super.setUp();
        repository = new HomeScreenCardSectionRepositoryImpl(dataStore);
    }

    @Test
    public void home_screen_response_is_persisted_correctly() throws Exception {
        repository.persistCardSectionResponse(getResponse("Homescreen_successful.json"));

        new ModelVerifier<>(HomeCard.class, getRealm(), 1, new ModelVerifier.Verifier<HomeCard>() {
            @Override
            public void verifyModel(HomeCard homeCard, int index) throws Exception {
                assertEquals(HomeSectionType.GET_REWARDED.getTypeKey(), homeCard.getSection());
                assertEquals(12345, homeCard.getAmountCompleted());
                assertEquals(HomeCardType.NON_SMOKERS_DECLARATION.getTypeKey(), homeCard.getType());
                assertEquals(14, homeCard.getPriority());
                assertEquals("350", homeCard.getPotentialPoints());
                assertEquals("2582", homeCard.getEarnedPoints());
                assertEquals(CardStatusType._DONE, homeCard.getStatus());
                assertEquals("2017-10-07", new LocalDate(homeCard.getGoalStartDate()).toString());
                assertEquals("2017-10-13", new LocalDate(homeCard.getGoalEndDate()).toString());
            }
        });
    }

    @Test
    public void home_screen_persists_overwrites_old_items() throws IOException {
        // after one model is in the repository
        repository.persistCardSectionResponse(getResponse("Homescreen_successful.json"));
        assertEquals(1, repository.getHomeCards().size());

        // when updated with no models, then there are no items in the repository (old items are removed/replaced)
        repository.persistCardSectionResponse(getResponse("Homescreen_successful_no_sections.json"));
        assertEquals(0, repository.getHomeCards().size());
    }

    private HomeScreenCardStatusResponse getResponse(String path) throws IOException {
        return getResponse(HomeScreenCardStatusResponse.class, path);
    }
}
