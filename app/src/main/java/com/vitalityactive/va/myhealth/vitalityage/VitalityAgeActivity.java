package com.vitalityactive.va.myhealth.vitalityage;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.vitalityactive.va.NavigationCoordinator;
import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.myhealth.BaseVitalityAgeActivity;
import com.vitalityactive.va.myhealth.shared.VitalityAgeConstants;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class VitalityAgeActivity extends BaseVitalityAgeActivity<VitalityAgePresenter.UserInterface, VitalityAgePresenter> implements VitalityAgePresenter.UserInterface {

    public static final String MODE = "VITALITY_AGE_TYPE";
    public final static int VHR_MODE = 1;
    public final static int VHC__DONE_VHR_PENDING_MODE = 2;
    @Inject
    protected NavigationCoordinator navigationCoordinator;
    @Inject
    AppConfigRepository appConfigRepository;
    @Inject
    VitalityAgePresenter presenter;
    @Inject
    EventDispatcher eventDispatcher;
    int mode;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setUpActionBarWithTitle(R.string.my_health_vitality_age_screen_title_613)
                .setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mode = extras.getInt(MODE);
            presenter.setVitalityAgeDisplayMode(mode);
        }
        ((Toolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        updateActionBarIcon(R.drawable.ic_close_white_24dp);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected VitalityAgePresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected VitalityAgePresenter getPresenter() {
        return presenter;
    }


    @Override
    public void loadVitalityAgeVHCDoneVHRPending() {
        setVitalityAgeContent(null, null, null, VitalityAgeConstants.VA_NOT_ENOUGH_DATA);
    }

    @Override
    public void setUpRecyclerView() {
        super.setUpRecyclerView();
        ViewUtilities.addDividers(this, recyclerView);
    }

    @Override
    public ArrayList<GenericRecyclerViewAdapter> customAdapters() {
        ArrayList<GenericRecyclerViewAdapter> adapters = new ArrayList<>();
        adapters.add(createVitalityAgeMyHealthUpdatedAdapter());
        return adapters;
    }

    @Override
    protected void setActionBarColor() {
        try {
            super.setActionBarColor(ContextCompat.getColor(this, R.color.jungle_green));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setStatusBarColor() {
        try {
            super.setStatusBarColor(ContextCompat.getColor(this, R.color.jungle_green));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected @ColorInt
    int globalTintColor() {
        return ContextCompat.getColor(this, R.color.jungle_green);
    }

    @Override
    protected String getActionBarTitle() {
        return getString(R.string.my_health_vitality_age_screen_title_613);
    }

    private GenericRecyclerViewAdapter createVitalityAgeMyHealthUpdatedAdapter() {
        List<VitalityAgeMyHealth> vitalityAgeDetails = new ArrayList<>();
        vitalityAgeDetails.add(new VitalityAgeMyHealth());
        return new GenericRecyclerViewAdapter<>(this,
                vitalityAgeDetails,
                R.layout.vitalityage_myhealth_card,
                new MyHealthUpdatedViewHolder.Factory(VitalityAgeActivity.this));
    }

    public class VitalityAgeMyHealth {

    }

}
