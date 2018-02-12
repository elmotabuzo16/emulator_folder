package com.vitalityactive.va.myhealth.vitalityageprofile;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;

import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.myhealth.BaseVitalityAgeActivity;
import com.vitalityactive.va.myhealth.content.FeedbackTipItem;
import com.vitalityactive.va.myhealth.content.MyHealthRecyclerItemDecorator;
import com.vitalityactive.va.myhealth.content.SectionItem;
import com.vitalityactive.va.myhealth.entity.VitalityAge;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class MyHealthVitalityAgeProfileActivity extends BaseVitalityAgeActivity<MyHealthVitalityAgeProfilePresenter.UserInterface, MyHealthVitalityAgeProfilePresenter> implements MyHealthVitalityAgeProfilePresenter.UserInterface, MyHealthFeedbackTipClickListener<SectionItem> {

    public static final int MORE_RESULTS_SECTION = 1;
    public static final int ATTRIBUTE_DETAILS = 2;
    @Inject
    MyHealthVitalityAgeProfilePresenter presenter;
    @Inject
    AppConfigRepository appConfigRepository;


    List<SectionItem> sections;

    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
    }

    @Override
    protected void setUpRecyclerView() {
        super.setUpRecyclerView();
        recyclerView.addItemDecoration(
                new MyHealthRecyclerItemDecorator.Builder()
                        .context(this)
                        .orientation(LinearLayoutManager.VERTICAL)
                        .build());
        ViewUtilities.scrollToTop(recyclerView);
    }

    private List<GenericRecyclerViewAdapter> createVitalityAgeTipsAdapters(List<SectionItem> sectionItems) {
        List<GenericRecyclerViewAdapter> viewAdapters = new ArrayList<>();
        if (sectionItems != null) {
            for (SectionItem sectionItem : sectionItems) {
                viewAdapters.add(new GenericRecyclerViewAdapter<>(this, sectionItem, R.layout.myhealth_vitalityage_tip_card, new MyHealthVitalityAgeTipsViewHolder.Factory(this, globalTintColor())));
            }
        }
        return viewAdapters;
    }

    @Override
    protected ArrayList<GenericRecyclerViewAdapter> customAdapters() {
        ArrayList<GenericRecyclerViewAdapter> adapters = new ArrayList<>();
        adapters.addAll(createVitalityAgeTipsAdapters(getSections()));
        return adapters;
    }

    @Override
    protected @ColorInt
    int globalTintColor() {
        return Color.parseColor(appConfigRepository.getGlobalTintColorHex());
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.my_health_vitality_age_screen_title_613);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected MyHealthVitalityAgeProfilePresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected MyHealthVitalityAgeProfilePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void loadVitalityAgeTips(List<SectionItem> sections) {
        this.sections = sections;

    }

    @Override
    public void showVitalityAgeAndTips(VitalityAge vitalityAge, List<SectionItem> sectionItems) {
        initialize(vitalityAge);
        this.sections = sectionItems;
    }

    public List<SectionItem> getSections() {
        return sections;
    }

    @Override
    public void onClicked(int position, int section, int sectionTypeKey, int attributeTypeKey) {
        switch (section) {
            case MORE_RESULTS_SECTION: {
                navigateTofeedbackTipMoreResults(sectionTypeKey);
                break;
            }
            case ATTRIBUTE_DETAILS: {
                navigateToFeedbackTipAttributeDetails(sectionTypeKey, attributeTypeKey);
                break;
            }
        }
    }

    public void navigateTofeedbackTipMoreResults(int sectionTypeKey) {
        if (presenter.sectionHasSubsections(sectionTypeKey)) {
            navigationCoordinator.navigateToVitalityAgeTipMoreResults(this, sectionTypeKey);
        }
    }

    private void navigateToFeedbackTipAttributeDetails(int sectionTypeKey, int attributeTypeKey) {
        navigationCoordinator.navigateToFeedbackHealthAttributeDetails(this, sectionTypeKey, attributeTypeKey);
    }


    @Override
    public void onClicked(int position, FeedbackTipItem item) {

    }
}
