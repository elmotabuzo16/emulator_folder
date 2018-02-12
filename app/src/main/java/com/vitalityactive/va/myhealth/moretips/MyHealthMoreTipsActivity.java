package com.vitalityactive.va.myhealth.moretips;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.NavigationCoordinator;
import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.myhealth.content.FeedbackTip;
import com.vitalityactive.va.myhealth.content.MyHealthContent;
import com.vitalityactive.va.myhealth.vitalityageprofile.MyHealthTipsMoreResultsPresenter;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MyHealthMoreTipsActivity extends BasePresentedActivity<MyHealthMoreTipsPresenter.UserInterface, MyHealthMoreTipsPresenter> implements MyHealthMoreTipsPresenter.UserInterface, GenericRecyclerViewAdapter.OnItemClickListener<FeedbackTip> {

    public static final String ATTRIBUTE_TYPE_KEY = "ATTRIBUTE_TYPE_KEY";
    public static final String SECTION_TYPE_KEY = "SECTION_TYPE_KEY";
    @Inject
    protected NavigationCoordinator navigationCoordinator;
    protected RecyclerView recyclerView;
    @Inject
    AppConfigRepository appConfigRepository;
    int feedbackTypeKey;
    @Inject
    MyHealthMoreTipsPresenter presenter;
    int mode;
    @Inject
    MyHealthTipsMoreResultsPresenter myHealthTipsMoreResultsPresenter;
    ContainersRecyclerViewAdapter containersRecyclerViewAdapter;
    List<FeedbackTip> feedbackTips;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.activity_myhealth_more_tips);
        injectDependencies(getDependencyInjector());
        setActionBarTitleAndDisplayHomeAsUp(getString(R.string.my_health_detail_main_title_more_tips_1067));
        setActionBarColor();
        setStatusBarColor();
        setUpRecyclerView();
        presenter.setAttributeTypeKey(getIntent().getIntExtra(ATTRIBUTE_TYPE_KEY, -1));
        presenter.setSectionTypeKey(getIntent().getIntExtra(SECTION_TYPE_KEY, -1));
    }

    private void setActionBarColor() {
        try {
            super.setActionBarColor(globalTintColor());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setStatusBarColor() {
        try {
            super.setStatusBarColor(globalTintColor());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected MyHealthMoreTipsPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected MyHealthMoreTipsPresenter getPresenter() {
        return presenter;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setUpRecyclerView() {
        recyclerView = findViewById(R.id.main_recyclerview);
        containersRecyclerViewAdapter = new ContainersRecyclerViewAdapter(createAdapters());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(containersRecyclerViewAdapter);
        ViewUtilities.scrollToTop(recyclerView);
    }

    private ArrayList<GenericRecyclerViewAdapter> createAdapters() {
        ArrayList<GenericRecyclerViewAdapter> viewAdapters = new ArrayList<>();
        if (feedbackTips != null && feedbackTips.size() > MyHealthContent.MAX_TIPS_TO_SHOW) {
            for (FeedbackTip feedbackTip : feedbackTips.subList(MyHealthContent.MAX_TIPS_TO_SHOW, feedbackTips.size())) {
                viewAdapters.add(new GenericRecyclerViewAdapter<>(this, feedbackTip, R.layout.myhealth_feedback_attribute_tips_item, new MyHealthMoreTipsViewHolder.Factory(this)));
            }
        }
        return viewAdapters;
    }

    public ArrayList<GenericRecyclerViewAdapter> customAdapters() {
        ArrayList<GenericRecyclerViewAdapter> adapters = new ArrayList<>();
        adapters.add(createVitalityAgeMyHealthUpdatedAdapter());
        return adapters;
    }

    protected @ColorInt
    int globalTintColor() {
        return Color.parseColor(appConfigRepository.getGlobalTintColorHex());
    }

    protected int getFeedbackTypeKey() {
        return feedbackTypeKey;
    }

    protected String getActionBarTitle() {
        return getString(R.string.my_health_vitality_age_screen_title_613);
    }

    private GenericRecyclerViewAdapter createVitalityAgeMyHealthUpdatedAdapter() {
        List<FeedbackTip> vitalityAgeDetails = new ArrayList<>();
        vitalityAgeDetails.add(new FeedbackTip());
        return new GenericRecyclerViewAdapter<>(this,
                vitalityAgeDetails,
                R.layout.vitalityage_myhealth_card,
                new MyHealthMoreTipsViewHolder.Factory(this));
    }

    @Override
    public void onClicked(int position, FeedbackTip feedbackTip) {
        if (feedbackTip.getFeedbackTipTypeCode() != null) {
            navigationCoordinator.navigateToFeedbackTipDetails(this, feedbackTip.getFeedbackTipTypeKey(), feedbackTip.getFeedbackTipTypeCode());
        }
    }

    @Override
    public void loadFeedbackTips(List<FeedbackTip> feedbackTips) {
        this.feedbackTips = feedbackTips;
        refreshAdapters();
    }

}
