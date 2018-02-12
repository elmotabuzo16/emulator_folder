package com.vitalityactive.va.pointsmonitor;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.test.InstrumentationRegistry;

import com.vitalityactive.va.R;
import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.constants.PointsEntryCategory;
import com.vitalityactive.va.dto.PointsEntryCategoryDTO;
import com.vitalityactive.va.login.LoginRepositoryImpl;
import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.register.entity.Username;
import com.vitalityactive.va.testutilities.annotations.RepositoryTests;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RepositoryTests
public class PointsMonitorRepositoryTest extends RepositoryTestBase {
    private PointsMonitorRepository repository;

    @Before
    public void setUp() throws IOException {
        super.setUp();
        repository = new PointsMonitorRepository(dataStore, new PointsMonitorContentImpl(InstrumentationRegistry.getTargetContext()));
    }

    @Test
    public void get_points_entry_categories() throws IOException {
        Username username = new Username("frikkiePoggenPoel@gmail.com");
        new LoginRepositoryImpl(dataStore, new AppConfigRepository(dataStore, InstrumentationRegistry.getTargetContext()))
                .persistLoginResponse(getResponse(LoginServiceResponse.class, "login/Login_points_categories_feature_links.json"), username);

        List<PointsEntryCategoryDTO> categories = getCategoriesSorted();

        assertEquals(3, categories.size());
        assertEquals(PointsEntryCategory._FITNESS, categories.get(0).getTypeKey());
        assertEquals(PointsEntryCategory._ASSESSMENT, categories.get(1).getTypeKey());
        assertEquals(PointsEntryCategory._SCREENING, categories.get(2).getTypeKey());
        assertEquals(getString(R.string.PM_category_filter_fitness_title_519), categories.get(0).getTitle());
        assertEquals(getString(R.string.PM_category_filter_assessment_title_516), categories.get(1).getTitle());
        assertEquals(getString(R.string.PM_category_filter_screening_title_518), categories.get(2).getTitle());
    }

    private String getString(@StringRes int resourceId) {
        return InstrumentationRegistry.getTargetContext().getString(resourceId);
    }

    @NonNull
    private List<PointsEntryCategoryDTO> getCategoriesSorted() {
        List<PointsEntryCategoryDTO> categories = repository.getPointsEntryCategories();
        Collections.sort(categories, new Comparator<PointsEntryCategoryDTO>() {
            @Override
            public int compare(PointsEntryCategoryDTO o1, PointsEntryCategoryDTO o2) {
                return Integer.compare(o1.getTypeKey(), o2.getTypeKey());
            }
        });
        return categories;
    }
}
