package com.vitalityactive.va.myhealth.learnmore;


import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.myhealth.content.MyHealthLearnMoreContent;
import com.vitalityactive.va.myhealth.content.MyHealthOnboardingContent;
import com.vitalityactive.va.myhealth.content.MyHealthRecyclerItemDecorator;
import com.vitalityactive.va.myhealth.entity.VitalityAge;
import com.vitalityactive.va.myhealth.shared.VitalityAgeConstants;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.learnmore.LearnMoreContainerViewHolder;
import com.vitalityactive.va.uicomponents.learnmore.LearnMoreItem;
import com.vitalityactive.va.uicomponents.learnmore.LearnMoreViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class VitalityAgeLearnMoreActivity extends BaseActivity {

    public static final String VITALITY_AGE_ACTUAL_TYPE = "VITALITY_AGE_ACTUAL_TYPE";
    public static final String VITALITY_AGE_EFFECTIVE_TYPE = "VITALITY_AGE_EFFECTIVE_TYPE";
    public static final String VITALITY_AGE_VALUE = "VITALITY_AGE_VALUE";
    public static final String VITALITY_AGE_VARIANCE = "VITALITY_AGE_VARIANCE";
    protected RecyclerView recyclerView;
    @Inject
    MyHealthOnboardingContent content;
    @Inject
    VitalityAgeLearnMorePresenter vitalityAgeLearnMorePresenter;
    @Inject
    AppConfigRepository appConfigRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vitality_age_learnmore);
        setUpActionBarWithTitle(getActionBarTitle()).setDisplayHomeAsUpEnabled(true);
        injectDependency();
        setUpRecyclerView();
        setActionBarColor();
        setStatusBarColor();
        setBackgroundColor();
    }

    private ArrayList<GenericRecyclerViewAdapter> createChildAdapters() {
        ArrayList<GenericRecyclerViewAdapter> adapters = new ArrayList<>();
        GenericRecyclerViewAdapter customViewAdapter = createCustomViewAdapter();
        adapters.add(createLearnMoreAdapter());
        adapters.add(customViewAdapter);
        return adapters;
    }

    private GenericRecyclerViewAdapter createLearnMoreAdapter() {
        return new LearnMoreViewAdapter<>(this,
                createListData(),
                new LearnMoreContainerViewHolder.Factory(),
                getHeaderTitle(),
                getHeaderSubTitle(),
                R.layout.view_learn_more_container);
    }

    protected void setUpRecyclerView() {
        recyclerView = findViewById(R.id.main_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new ContainersRecyclerViewAdapter(createChildAdapters()));
        recyclerView.addItemDecoration(
                new MyHealthRecyclerItemDecorator.Builder()
                        .context(this)
                        .orientation(LinearLayoutManager.VERTICAL)
                        .build());
        ViewUtilities.scrollToTop(recyclerView);
    }

    private void setBackgroundColor() {
        findViewById(R.id.main_recyclerview).setBackgroundColor(ContextCompat.getColor(this, R.color.active_rewards_divider_background));
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

    private @ColorInt
    int globalTintColor() {
        return Color.parseColor(appConfigRepository.getGlobalTintColorHex());
    }


    private int getActualFeedBackTypeKey() {
        return getIntent().getIntExtra(VITALITY_AGE_ACTUAL_TYPE, VitalityAgeConstants.VA_UNKNOWN);
    }

    private int getEffectiveFeedBackTypeKey() {
        return getIntent().getIntExtra(VITALITY_AGE_EFFECTIVE_TYPE, VitalityAgeConstants.VA_UNKNOWN);
    }

    private String getValue() {
        return getIntent().getStringExtra(VITALITY_AGE_VALUE);
    }

    private String getVariance() {
        return getIntent().getStringExtra(VITALITY_AGE_VARIANCE);
    }

    protected int getActionBarTitle() {
        return R.string.learn_more_button_title_104;
    }

    protected String getHeaderTitle() {
        return getString(MyHealthLearnMoreContent.LearnMoreHeader.getTitleResourceId(getEffectiveFeedBackTypeKey()));
    }

    protected String getHeaderSubTitle() {
        return MyHealthLearnMoreContent.getLearnMoreHeader(this, getVitalityAge());
    }

    protected List<List<LearnMoreItem>> createListData() {
        List<LearnMoreItem> learnMoreItems = new ArrayList<>();
        content.initialize(getVitalityAge());
        learnMoreItems.addAll(Arrays.asList(
                new LearnMoreItem(content.getLearnMoreSection1Title(), content.getLearnMoreSection1Content(), content.getLearnMoreSection1Icon(), null, 0, globalTintColor(), true),
                new LearnMoreItem(content.getLearnMoreSection2Title(), content.getLearnMoreSection2Content(), content.getLearnMoreSection2Icon(), null, 0, globalTintColor(), true)
        ));
        if (getEffectiveFeedBackTypeKey() == VitalityAgeConstants.VA_UNKNOWN || getEffectiveFeedBackTypeKey() == VitalityAgeConstants.VA_NOT_ENOUGH_DATA || getEffectiveFeedBackTypeKey() == VitalityAgeConstants.VA_OUTDATED) {
            learnMoreItems.add(new LearnMoreItem(content.getLearnMoreSection3Title(), content.getLearnMoreSection3Content(), content.getLearnMoreSection3Icon(), null, 0, globalTintColor(), true));
        }
        return Collections.singletonList(learnMoreItems);
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


    private DisclaimerItem createDisclaimerItem() {
        return new DisclaimerItem(getString(R.string.generic_disclaimer_button_title_265), R.drawable.benefit_guide);
    }

    public void injectDependency() {
        getDependencyInjector().inject(this);
    }

    protected GenericRecyclerViewAdapter createCustomViewAdapter() {
        return new GenericRecyclerViewAdapter<>(VitalityAgeLearnMoreActivity.this,
                createDisclaimerItem(),
                R.layout.vitality_age_disclaimer_item,
                new DisclaimerViewHolder.Factory(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        navigationCoordinator.navigateAfterVitalityAgeLearnMoreDisclaimerButtonClicked(VitalityAgeLearnMoreActivity.this);
                    }
                }, globalTintColor())
        );
    }

    private VitalityAge getVitalityAge() {
        return new VitalityAge.Builder()
                .variance(getVariance())
                .age(getValue())
                .effectiveType(getEffectiveFeedBackTypeKey())
                .actualType(getActualFeedBackTypeKey())
                .variance(getVariance())
                .build();
    }

}
