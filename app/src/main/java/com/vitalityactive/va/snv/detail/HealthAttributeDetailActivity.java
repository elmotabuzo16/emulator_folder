package com.vitalityactive.va.snv.detail;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.snv.content.SNVHealthAttributeContent;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.vhc.dto.HealthAttributeDTO;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;


public class HealthAttributeDetailActivity
        extends BasePresentedActivity<HealthAttributeDetailPresenter.UserInterface, HealthAttributeDetailPresenter>
        implements HealthAttributeDetailPresenter.UserInterface {

    public static final String HEALTH_ATTRIBUTE_GROUP_FEATURE_TYPE = "HEALTH_ATTRIBUTE_GROUP_TYPE_KEY";

    @Inject
    HealthAttributeDetailPresenter presenter;
    @Inject
    SNVHealthAttributeContent snvHealthAttributeContent;

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.activity_vhc_health_attribute_detail);

        final int healthAttributeGroupFeatureType = getIntent().getIntExtra(HEALTH_ATTRIBUTE_GROUP_FEATURE_TYPE, 0);

        presenter.setHealthAttributeGroupFeatureType(healthAttributeGroupFeatureType);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected HealthAttributeDetailPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected HealthAttributeDetailPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setActionBarTitle(String groupDescription) {
        setActionBarTitleAndDisplayHomeAsUp(groupDescription);
    }

    @Override
    public void setUpRecyclerView(List<HealthAttributeDTO> healthAttributeDTOs,
                                  Date membershipPeriodStart,
                                  Date membershipPeriodEnd,
                                  String groupDescription) {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);

        final GenericRecyclerViewAdapter adapter = new GenericRecyclerViewAdapter<>(
                this,
                healthAttributeDTOs,
                R.layout.health_attribute_group_detail,
                new HealthAttributeGroupDetailViewHolder.Factory(snvHealthAttributeContent,
                        membershipPeriodStart,
                        membershipPeriodEnd,
                        groupDescription));

        recyclerView.setAdapter(adapter);
    }
}
