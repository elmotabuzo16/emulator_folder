package com.vitalityactive.va.pointsmonitor;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.vitalityactive.va.dto.PointsEntryCategoryDTO;
import com.vitalityactive.va.dto.PointsEntryDTO;
import com.vitalityactive.va.utilities.TimeUtilities;
import com.vitalityactive.va.utilities.date.Date;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class PointsMonitorMonthPresenter {

    private final TimeUtilities timeUtilities;
    private final PointsMonitorRepository repository;
    private PointsMonitorSelectedCategoriesProvider selectedCategoriesProvider;
    private final PointsMonitorInteractor interactor;

    @Inject
    PointsMonitorMonthPresenter(TimeUtilities timeUtilities,
                                PointsMonitorRepository repository,
                                PointsMonitorSelectedCategoriesProvider selectedCategoriesProvider,
                                PointsMonitorInteractorImpl interactor) {
        this.timeUtilities = timeUtilities;
        this.repository = repository;
        this.selectedCategoriesProvider = selectedCategoriesProvider;
        this.interactor = interactor;
    }

    @NonNull
    public List<PointsHistoryDay> getPointsEntries(int monthIndex) {
        Date firstDayOfMonth = timeUtilities.now().minusMonths(monthIndex).toFirstDayOfMonth();
        Date lastDayOfMonth = timeUtilities.now().minusMonths(monthIndex).toLastDayOfMonth();
        List<PointsEntryDTO> pointsEntries = repository.getMonthPointsEntries(firstDayOfMonth, lastDayOfMonth, selectedCategoriesProvider.getSelectedCategory());
        return filterPointsEntriesByDay(pointsEntries);
    }

    private List<PointsHistoryDay> filterPointsEntriesByDay(List<PointsEntryDTO> pointsEntries) {
        @SuppressLint("UseSparseArrays") HashMap<Integer, List<PointsEntryDTO>> filtered = new HashMap<>();
        for (PointsEntryDTO pointsEntry : pointsEntries) {
            if (!filtered.containsKey(pointsEntry.getEffectiveDate().getDayOfMonth())) {
                filtered.put(pointsEntry.getEffectiveDate().getDayOfMonth(), new ArrayList<PointsEntryDTO>());
            }
            filtered.get(pointsEntry.getEffectiveDate().getDayOfMonth()).add(pointsEntry);
        }
        List<PointsHistoryDay> days = new ArrayList<>();
        for (List<PointsEntryDTO> dayPointsEntries : filtered.values()) {
            Collections.sort(dayPointsEntries, new Comparator<PointsEntryDTO>() {
                @Override
                public int compare(PointsEntryDTO pointsEntry1, PointsEntryDTO pointsEntry2) {
                    return -pointsEntry1.getEffectiveDate().compareTo(pointsEntry2.getEffectiveDate());
                }
            });
            days.add(new PointsHistoryDay(dayPointsEntries.get(0).getEffectiveDate(), dayPointsEntries));
        }
        Collections.sort(days, new Comparator<PointsHistoryDay>() {
            @Override
            public int compare(PointsHistoryDay o1, PointsHistoryDay o2) {
                return -o1.getDate().compareTo(o2.getDate());
            }
        });
        return days;
    }

    public PointsEntryCategoryDTO getSelectedCategory() {
        return selectedCategoriesProvider.getSelectedCategory();
    }

    public boolean isCurrentlyRefreshing() {
        return interactor.isFetchingPointsHistory();
    }
}
