package com.vitalityactive.va.myhealth.vitalityageprofile;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.myhealth.content.AttributeItem;
import com.vitalityactive.va.myhealth.content.MyHealthRecyclerItemDecorator;
import com.vitalityactive.va.myhealth.content.SectionItem;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;

import javax.inject.Inject;


public class MyHealthTipsMoreResultsActivity extends BasePresentedActivity<MyHealthTipsMoreResultsPresenter.UserInterface, MyHealthTipsMoreResultsPresenter> implements MyHealthTipsMoreResultsPresenter.UserInterface, GenericRecyclerViewAdapter.OnItemClickListener<AttributeItem> {


    public static final String SECTION_TYPE_KEY = "SECTION_TYPE_KEY";
    @Inject
    MyHealthTipsMoreResultsPresenter myHealthTipsMoreResultsPresenter;
    @Inject
    AppConfigRepository appConfigRepository;
    RecyclerView recyclerView;
    ContainersRecyclerViewAdapter containersRecyclerViewAdapter;
    SectionItem sectionItemTips;
    SectionItem sectionItemTipSummary;

    @Override
    public void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_myhealth_tips_more_results);
        injectDependencies(getDependencyInjector());
        setActionBarTitleAndDisplayHomeAsUp(getString(R.string.health_attribute_tips_moreresults_title));
        setActionBarColor(globalTintColor());
        setStatusBarColor(globalTintColor());
        setUpRecyclerView();
        myHealthTipsMoreResultsPresenter.setSectionTypeKey(getIntent().getIntExtra(SECTION_TYPE_KEY, -1));
    }

    protected void setUpRecyclerView() {
        recyclerView = findViewById(R.id.main_recyclerview);
        containersRecyclerViewAdapter = new ContainersRecyclerViewAdapter(createAdapters());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(containersRecyclerViewAdapter);
        recyclerView.addItemDecoration(
                new MyHealthRecyclerItemDecorator.Builder()
                        .context(this)
                        .orientation(LinearLayoutManager.VERTICAL)
                        .build());
        ViewUtilities.scrollToTop(recyclerView);
    }

    private ArrayList<GenericRecyclerViewAdapter> createAdapters() {
        ArrayList<GenericRecyclerViewAdapter> viewAdapters = new ArrayList<>();
        if (sectionItemTips != null && sectionItemTips.getAttributeItems() != null && sectionItemTips.getAttributeItems().size() > 0) {
            viewAdapters.add(new GenericRecyclerViewAdapter<>(this, sectionItemTips
                    , R.layout.myhealth_vitalityage_tip_more_results_top_section, new MyHealthTipsMoreResultsMainViewHolder.Factory(this)));

        }
        if (sectionItemTipSummary != null && sectionItemTipSummary.getAttributeItems() != null && sectionItemTipSummary.getAttributeItems().size() > 0) {
            viewAdapters.add(new GenericRecyclerViewAdapter<>(this, sectionItemTipSummary
                    , R.layout.myhealth_vitalityage_tip_more_results_bottom_section, new MyHealthTipsMoreResultsSummaryMainViewHolder.Factory(this)));
        }
        return viewAdapters;
    }


    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected MyHealthTipsMoreResultsPresenter.UserInterface getUserInterface() {
        return this;
    }

    protected @ColorInt
    int globalTintColor() {
        return Color.parseColor(appConfigRepository.getGlobalTintColorHex());
    }

    @Override
    protected MyHealthTipsMoreResultsPresenter getPresenter() {
        return myHealthTipsMoreResultsPresenter;
    }

    protected void refreshAdapters() {
        final ArrayList<GenericRecyclerViewAdapter> adapters = createAdapters();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (adapters != null) {
                    recyclerView.setAdapter(new ContainersRecyclerViewAdapter(adapters));
                    recyclerView.invalidate();
                }
            }
        });
    }

    @Override
    public void loadFeedbackTips(SectionItem sectionItemTips, SectionItem sectionItemTipSummary) {
        this.sectionItemTips = sectionItemTips;
        this.sectionItemTipSummary = sectionItemTipSummary;
        refreshAdapters();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onClicked(int position, AttributeItem item) {
        navigationCoordinator.navigateToFeedbackHealthAttributeDetails(this, item.getSectionTypeKey(), item.getAttributeTypeKey());
    }
}
