package com.vitalityactive.va.pointsmonitor;

import com.vitalityactive.va.BaseTest;
import com.vitalityactive.va.constants.PointsEntryCategory;
import com.vitalityactive.va.dto.PointsEntryCategoryDTO;
import com.vitalityactive.va.dto.PointsEntryDTO;
import com.vitalityactive.va.persistence.models.PointsEntry;
import com.vitalityactive.va.utilities.date.Date;

import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class PointsMonitorMonthPresenterTest extends BaseTest {

    @Mock
    PointsMonitorRepository mockPointsMonitorRepository;

    @Mock
    PointsMonitorSelectedCategoriesProvider mockSelectedCategoriesProvider;

    @Mock
    PointsMonitorInteractorImpl interactor;

    private PointsEntryCategoryDTO selectedCategory = new PointsEntryCategoryDTO(PointsEntryCategory._ASSESSMENT, "asdf");

    @Override
    public void setUp() throws Exception {
        super.setUp();
        initialiseAndroidThreeTen();
        when(mockSelectedCategoriesProvider.getSelectedCategory()).thenReturn(selectedCategory);
    }

    @Test
    public void days_are_filtered_correctly() {
        Date firstDayOfMonth = new Date("2016-12-01T00:00:00+02:00[Africa/Johannesburg]");
        Date lastDayOfMonth = new Date("2016-12-31T23:59:59+02:00[Africa/Johannesburg]");
        when(mockPointsMonitorRepository.getMonthPointsEntries(firstDayOfMonth, lastDayOfMonth, selectedCategory)).thenReturn(createPointsEntries());

        when(mockTimeUtilities.now()).thenReturn(new Date(NOW));

        PointsMonitorMonthPresenter presenter = new PointsMonitorMonthPresenter(mockTimeUtilities, mockPointsMonitorRepository, mockSelectedCategoriesProvider, interactor);

        List<PointsHistoryDay> days = presenter.getPointsEntries(0);

        Date mostRecentDayInJune = new Date("2017-06-21T00:00:00Z");
        int indexMostRecentDayInJune = 0;
        assertEquals(2, days.size());
        assertEquals(2, days.get(indexMostRecentDayInJune).getPointsEntries().size());
        assertEquals(mostRecentDayInJune.getDayOfMonth(), days.get(indexMostRecentDayInJune).getDate().getDayOfMonth());
        assertEquals(mostRecentDayInJune.getMonth(), days.get(indexMostRecentDayInJune).getDate().getMonth());
        assertEquals(mostRecentDayInJune.getYear(), days.get(indexMostRecentDayInJune).getDate().getYear());

        // Entries on same day sorted most recent to least recent
        assertEquals(new Date("2017-06-21T08:30:42+02:00[Africa/Johannesburg]"), days.get(indexMostRecentDayInJune).getPointsEntries().get(0).getEffectiveDate());
        assertEquals(new Date("2017-06-21T04:23:12+02:00[Africa/Johannesburg]"), days.get(indexMostRecentDayInJune).getPointsEntries().get(1).getEffectiveDate());

        Date leastRecentDayInJune = new Date("2017-06-01T00:00:00Z");
        int indexLeastRecentDayInJune = 1;
        assertEquals(leastRecentDayInJune.getDayOfMonth(), days.get(indexLeastRecentDayInJune).getDate().getDayOfMonth());
        assertEquals(leastRecentDayInJune.getMonth(), days.get(indexLeastRecentDayInJune).getDate().getMonth());
        assertEquals(leastRecentDayInJune.getYear(), days.get(indexLeastRecentDayInJune).getDate().getYear());
    }

    private List<PointsEntryDTO> createPointsEntries() {
        return Arrays.asList(
                getPointsEntryDTO("2017-06-01T12:30:42-05:00[America/New_York]"),
                getPointsEntryDTO("2017-06-21T08:30:42+02:00[Africa/Johannesburg]"),
                getPointsEntryDTO("2017-06-21T04:23:12+02:00[Africa/Johannesburg]")
        );
    }

    private PointsEntryDTO getPointsEntryDTO(String effectiveDate) {
        PointsEntry pointsEntry = new PointsEntry();
        pointsEntry.setEffectiveDate(new Date(effectiveDate).getMillisecondsSinceEpoch());
        return new PointsEntryDTO(pointsEntry);
    }
}
