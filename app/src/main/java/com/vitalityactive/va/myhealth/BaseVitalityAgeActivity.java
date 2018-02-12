package com.vitalityactive.va.myhealth;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.R;
import com.vitalityactive.va.myhealth.content.MyHealthContent;
import com.vitalityactive.va.myhealth.content.VitalityAgeData;
import com.vitalityactive.va.myhealth.entity.VitalityAge;
import com.vitalityactive.va.myhealth.shared.VitalityAgeConstants;
import com.vitalityactive.va.myhealth.vitalityageprofile.MyHealthVitalityAgeProfileViewHolder;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseVitalityAgeActivity<UI extends BaseVitalityAgeInterface, P extends Presenter<UI>>
        extends BasePresentedActivity<UI, P> implements View.OnClickListener {

    protected RecyclerView recyclerView;
    ContainersRecyclerViewAdapter containersRecyclerViewAdapter;
    int effectiveFeedbackTypeKey;
    int actualFeedbackTypeKey;
    String vitalityAgeValue;
    String vitalityAgeVariance;
    List<VitalityAgeData> vitalityAgeDetails;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        super.create(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vitality_age);
        setUpRecyclerView();
        injectDependencies(getDependencyInjector());
        setActionBarColor();
        setStatusBarColor();
        setActionBarTitleAndDisplayHomeAsUp(getActionBarTitle());
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

    private GenericRecyclerViewAdapter<VitalityAgeData, MyHealthVitalityAgeProfileViewHolder> createVitalityAgeAdapter(List<VitalityAgeData> vitalityAgeDetails) {
        this.vitalityAgeDetails = vitalityAgeDetails;
        return new GenericRecyclerViewAdapter<>(this, vitalityAgeDetails, R.layout.vitality_age_header, new MyHealthVitalityAgeProfileViewHolder.Factory(this));
    }


    protected void setVitalityAgeContent(final String age, final String feedbackTitle, final String variance, final int feedbackType) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                List<GenericRecyclerViewAdapter> adapters = new ArrayList<>();
                GenericRecyclerViewAdapter vitalityAgeViewAdapter = createVitalityAgeAdapter(Collections.singletonList(MyHealthContent.getVitalityAge(BaseVitalityAgeActivity.this, age, feedbackTitle, feedbackType, variance)));
                if (vitalityAgeViewAdapter != null) {
                    adapters.add(vitalityAgeViewAdapter);
                }
                ArrayList<GenericRecyclerViewAdapter> customAdapters = customAdapters();
                if (vitalityAgeViewAdapter != null && customAdapters != null) {
                    adapters.addAll(customAdapters);
                }
                if (containersRecyclerViewAdapter != null) {
                    containersRecyclerViewAdapter.notifyDataSetChanged();
                    containersRecyclerViewAdapter = new ContainersRecyclerViewAdapter(adapters);
                } else {
                    containersRecyclerViewAdapter = new ContainersRecyclerViewAdapter(adapters);
                }
                recyclerView.setAdapter(containersRecyclerViewAdapter);
                recyclerView.invalidate();
            }
        });
    }

    protected void setUpRecyclerView() {
        recyclerView = findViewById(R.id.main_recyclerview);
        ArrayList<GenericRecyclerViewAdapter> adapters = setUpAdapters();
        if (adapters != null && adapters.size() > 0) {
            containersRecyclerViewAdapter = new ContainersRecyclerViewAdapter(adapters);
            recyclerView.setAdapter(containersRecyclerViewAdapter);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ViewUtilities.scrollToTop(recyclerView);
    }


    protected ArrayList<GenericRecyclerViewAdapter> setUpAdapters() {
        ArrayList<GenericRecyclerViewAdapter> adapters = new ArrayList<>();
        vitalityAgeDetails = vitalityAgeDetails != null ? vitalityAgeDetails : Collections.singletonList(MyHealthContent.getVitalityAge(BaseVitalityAgeActivity.this, null, null, VitalityAgeConstants.VA_UNKNOWN, null));
        GenericRecyclerViewAdapter vitalityAgeAdapter = createVitalityAgeAdapter(vitalityAgeDetails);
        if (vitalityAgeAdapter != null) {
            adapters.add(vitalityAgeAdapter);
        }
        ArrayList<GenericRecyclerViewAdapter> customAdapters = customAdapters();
        if (vitalityAgeAdapter != null && customAdapters != null) {
            adapters.addAll(customAdapters);
        }
        return adapters;
    }

    public void initialize(VitalityAge vitalityAge) {
        if (vitalityAge != null) {
            effectiveFeedbackTypeKey = vitalityAge.getEffectiveType();
            actualFeedbackTypeKey = vitalityAge.getActualType();
            vitalityAgeVariance = vitalityAge.getVariance();
            vitalityAgeValue = vitalityAge.getAge();
            setVitalityAgeContent(vitalityAge.getAge(), vitalityAge.getFeedbackTitle(), vitalityAge.getVariance(), vitalityAge.getEffectiveType());
        } else {
            setVitalityAgeContent(null, null, null, 0);
        }
    }

    public List<VitalityAgeData> getVitalityAgeDetails() {
        return vitalityAgeDetails;
    }

    protected abstract ArrayList<GenericRecyclerViewAdapter> customAdapters();

    protected abstract int globalTintColor();

    protected abstract String getActionBarTitle();

    protected int getEffectiveFeedbackTypeKey() {
        return effectiveFeedbackTypeKey;
    }

    protected int getActualFeedbackTypeKey() {
        return actualFeedbackTypeKey;
    }

    protected String getVitalityAge() {
        return vitalityAgeValue;
    }

    protected String getVitalityAgeVariance() {
        return vitalityAgeVariance;
    }

    @Override
    public void onClick(View v) {
        navigationCoordinator.navigateAfterVitalityAgeCardLearnMoreButtonClicked(BaseVitalityAgeActivity.this, getEffectiveFeedbackTypeKey(), getActualFeedbackTypeKey(), getVitalityAge(), getVitalityAgeVariance());
    }
}
