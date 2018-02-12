package com.vitalityactive.va.activerewards.participatingpartners;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.participatingpartners.presenters.ParticipatingPartnersPresenter;
import com.vitalityactive.va.activerewards.rewards.viewholder.ActiveRewardsPartnerViewHolder;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dto.PartnerItemDTO;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.CMSImageLoader;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class BaseActiveRewardsParticipatingPartnersActivity
        extends BasePresentedActivity<ParticipatingPartnersPresenter.UserInterface, ParticipatingPartnersPresenter>
        implements ParticipatingPartnersPresenter.UserInterface, ActiveRewardsPartnerViewHolder.OnPartnerClickedListener {

    private static final String TAG = "ParticipatingPartnersAc";

    @Inject
    CMSImageLoader cmsImageLoader;

    @Inject
    ParticipatingPartnersPresenter partnersPresenter;

    private View rootContainer;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GenericRecyclerViewAdapter<PartnerItemDTO, ActiveRewardsPartnerViewHolder> participatingPartnersAdapter;

    private View.OnClickListener getSnackBarRetryListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partnersPresenter.onRetry();
            }
        };
    }

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.activity_active_rewards_participating_partners);

        rootContainer = findViewById(R.id.activity_active_rewards_participating_partners);

        setUpActionBarWithTitle(R.string.AR_learn_more_participating_partners_title_696)
                .setDisplayHomeAsUpEnabled(true);

        setUpRecyclerView();

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(ViewUtilities.getColorPrimaryFromTheme(this));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                partnersPresenter.onRetry();
            }
        });
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        navigationCoordinator.getActiveRewardsDependencyInjector().inject(this);
    }

    @Override
    protected ParticipatingPartnersPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected ParticipatingPartnersPresenter getPresenter() {
        return partnersPresenter;
    }

    @Override
    public void showRewardPartners(List<PartnerItemDTO> rewardPartners) {
        participatingPartnersAdapter.setData(rewardPartners);
    }

    @Override
    public void showLoadingIndicator() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoadingIndicator() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showGenericErrorMessage() {
        showSnackBar(R.string.error_unable_to_load_title_503);
    }

    @Override
    public void showConnectionErrorMessage() {
        showSnackBar(R.string.connectivity_error_alert_title_44);
    }

    private void showSnackBar(int stringId) {
        Snackbar.make(rootContainer, stringId, Snackbar.LENGTH_LONG)
                .setAction(R.string.AR_connection_error_button_retry_789, getSnackBarRetryListener()).show();
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.main_recyclerview);
        recyclerView.setAdapter(createAdapter());
        ViewUtilities.addDividers(this, recyclerView);
        ViewUtilities.scrollToTop(recyclerView);
    }

    private GenericRecyclerViewAdapter<PartnerItemDTO, ActiveRewardsPartnerViewHolder> createAdapter() {
        participatingPartnersAdapter = new GenericRecyclerViewAdapter<>(this,
                new ArrayList<PartnerItemDTO>(),
                R.layout.active_rewards_participating_partner_item,
                new ActiveRewardsPartnerViewHolder.Factory(cmsImageLoader, this));
        return participatingPartnersAdapter;
    }

    @Override
    public void onPartnerClicked(PartnerItemDTO partnerItem) {
        Log.d(TAG, "clicked on partner: " + partnerItem.title);
        navigationCoordinator.navigateAfterActiveRewardsParticipatingPartner(this, partnerItem.id);
    }
}
