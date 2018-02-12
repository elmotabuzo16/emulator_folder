package com.vitalityactive.va.myhealth.healthattributes;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.myhealth.content.FeedbackTip;
import com.vitalityactive.va.myhealth.content.FeedbackTipItem;
import com.vitalityactive.va.myhealth.content.HealthAttributeRecommendationItem;
import com.vitalityactive.va.myhealth.content.MyHealthContent;
import com.vitalityactive.va.myhealth.content.MyHealthRecyclerItemDecorator;
import com.vitalityactive.va.myhealth.shared.MyHealthUtils;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MyHealthAttributeDetailActivity extends BasePresentedActivity<MyHealthAttributeDetailPresenter.UserInterface, MyHealthAttributeDetailPresenter> implements MyHealthAttributeDetailPresenter.UserInterface, View.OnClickListener, GenericRecyclerViewAdapter.OnItemClickListener<FeedbackTip> {


    public static final String ATTRIBUTE_TYPE_KEY = "ATTRIBUTE_TYPE_KEY";
    public static final String SECTION_TYPE_KEY = "SECTION_TYPE_KEY";
    @Inject
    AppConfigRepository appConfigRepository;
    @Inject
    MyHealthAttributeDetailPresenter presenter;
    RecyclerView recyclerView;
    ContainersRecyclerViewAdapter containersRecyclerViewAdapter;
    HealthAttributeRecommendationItem healthAttributeRecommendationItem;
    FeedbackTipItem feedbackTipItem;

    @Override
    public void create(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_health_attribute_details);
        injectDependencies(getDependencyInjector());
        setActionBarColor();
        setStatusBarColor();
        setUpRecyclerView();
        presenter.setHealthAttributeTypeKey(getIntent().getIntExtra(ATTRIBUTE_TYPE_KEY, -1));
        presenter.setSectionTypeKey(getIntent().getIntExtra(SECTION_TYPE_KEY, -1));
    }

    protected void setActionBarColor() {
        try {
            super.setActionBarColor(globalTintColor());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setStatusBarColor() {
        try {
            super.setStatusBarColor(globalTintColor());
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private @ColorInt
    int globalTintColor() {
        return Color.parseColor(appConfigRepository.getGlobalTintColorHex());
    }

    private ArrayList<GenericRecyclerViewAdapter> createAdapters() {
        ArrayList<GenericRecyclerViewAdapter> viewAdapters = new ArrayList<>();
        GenericRecyclerViewAdapter recommendationAdapter = createFeedbackRecommendationAdapter();
        if (recommendationAdapter != null) {
            viewAdapters.add(recommendationAdapter);
        }
        GenericRecyclerViewAdapter feedbackTipsAdapter = createFeedbackTipsAdapter();
        if (feedbackTipsAdapter != null) {
            viewAdapters.add(feedbackTipsAdapter);
        }
        return viewAdapters;
    }


    private GenericRecyclerViewAdapter createFeedbackRecommendationAdapter() {
        if (getHealthAttributeRecommendationItem() != null) {
            return new GenericRecyclerViewAdapter<>(this, getHealthAttributeRecommendationItem(), R.layout.myhealth_feedback_attribute_recommendation, new MyHealthAttributeRecommendationViewHolder.Factory());
        }
        return null;
    }

    private GenericRecyclerViewAdapter createFeedbackTipsAdapter() {
        if (getFeedbackTipItem() != null && hasDisplayableContent(getFeedbackTipItem().getFeedbackTips())) {
            return new GenericRecyclerViewAdapter<>(this, getFeedbackTipItem(), R.layout.myhealth_feedback_attribute_tips, new HealthAttributeFeedbackTipMainViewholder.Factory(this, this, globalTintColor()));
        }
        return null;
    }

    private boolean hasDisplayableContent(List<FeedbackTip> feedbackTips) {
        return feedbackTips != null && feedbackTips.size() > 0 && MyHealthUtils.tipsHaveContent(feedbackTips);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected MyHealthAttributeDetailPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected MyHealthAttributeDetailPresenter getPresenter() {
        return presenter;
    }


    @Override
    public void showHealthAttributeRecommendationAndFeedbackTip(HealthAttributeRecommendationItem healthAttributeRecommendationItem, List<FeedbackTip> feedbackTips) {
        this.healthAttributeRecommendationItem = healthAttributeRecommendationItem;
        this.feedbackTipItem = new FeedbackTipItem.Builder()
                .setFeedbackTips(feedbackTips)
                .setSectionTitle(getSectionTitle())
                .build();
        refreshAdapters();
    }


    private String getSectionTitle() {
        try {
            return getString(MyHealthContent.FeedbackAttributeTitle.getContentId(getIntent().getIntExtra(SECTION_TYPE_KEY, -1)));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    protected void refreshAdapters() {
        final ArrayList<GenericRecyclerViewAdapter> adapters = createAdapters();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (adapters != null && adapters.size() > 0) {
                    recyclerView.setAdapter(new ContainersRecyclerViewAdapter(adapters));
                    recyclerView.invalidate();
                }
            }
        });
    }

    @Override
    public void showTitle(String title) {
        if (title != null) {
            setActionBarTitleAndDisplayHomeAsUp(title);
        }
    }

    public HealthAttributeRecommendationItem getHealthAttributeRecommendationItem() {
        return healthAttributeRecommendationItem;
    }

    public FeedbackTipItem getFeedbackTipItem() {
        return feedbackTipItem;
    }

    @Override
    public void onClick(View v) {
        navigationCoordinator.navigateToMoreTips(this, getIntent().getIntExtra(SECTION_TYPE_KEY, -1), getIntent().getIntExtra(ATTRIBUTE_TYPE_KEY, -1));
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


    @Override
    public void onClicked(int position, FeedbackTip feedbackTip) {
        if (feedbackTip != null && feedbackTip.getFeedbackTipTypeCode() != null) {
            navigationCoordinator.navigateToFeedbackTipDetails(this, feedbackTip.getFeedbackTipTypeKey(), feedbackTip.getFeedbackTipTypeCode());
        }
    }
}
