package com.vitalityactive.va.activerewards.rewards;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vitalityactive.va.BasePresentedFragment;
import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.CardMarginSettings;
import com.vitalityactive.va.activerewards.rewards.dto.RewardVoucherDTO;
import com.vitalityactive.va.activerewards.rewards.dto.UnclaimedRewardDTO;
import com.vitalityactive.va.activerewards.rewards.presenters.CurrentRewardsListPresenter;
import com.vitalityactive.va.activerewards.rewards.titledlists.NoRewardAvailableTitledList;
import com.vitalityactive.va.activerewards.rewards.titledlists.ParticipatingPartnersTitledList;
import com.vitalityactive.va.activerewards.rewards.titledlists.RewardVouchersTitledList;
import com.vitalityactive.va.activerewards.rewards.titledlists.UnclaimedRewardsTitledList;
import com.vitalityactive.va.activerewards.rewards.viewholder.ActiveRewardsPartnerViewHolder;
import com.vitalityactive.va.activerewards.rewards.viewholder.RewardVoucherViewHolder;
import com.vitalityactive.va.activerewards.rewards.viewholder.UnclaimedRewardViewHolder;
import com.vitalityactive.va.constants.RewardId;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dto.PartnerItemDTO;
import com.vitalityactive.va.menu.MenuContainerViewHolder;
import com.vitalityactive.va.menu.MenuItemType;
import com.vitalityactive.va.uicomponents.GenericTitledListContainerWithAdapter;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.CMSImageLoader;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class BaseCurrentRewardsFragment extends BasePresentedFragment<CurrentRewardsListPresenter.UserInterface, CurrentRewardsListPresenter>
        implements MenuContainerViewHolder.OnMenuItemClickedListener, ActiveRewardsPartnerViewHolder.OnPartnerClickedListener,
        UnclaimedRewardViewHolder.OnUnclaimedRewardClickedListener, CurrentRewardsListPresenter.UserInterface, RewardVoucherViewHolder.OnRewardVoucherClickedListener {
    private static final String TAG = "CurrentRewardsFragment";

    private RecyclerView recyclerView;
    private Context context;
    private ContainersRecyclerViewAdapter containersAdapter;
    private UnclaimedRewardsTitledList unclaimedRewardsList;
    private RewardVouchersTitledList rewardVouchersList;
    private ParticipatingPartnersTitledList rewardPartnersList;

    @Inject
    CMSImageLoader cmsImageLoader;

    @Inject
    InsurerConfigurationRepository insurerConfigRepo;
    @Inject
    CurrentRewardsListPresenter currentRewardsListPresenter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private GenericRecyclerViewAdapter<GenericTitledListContainerWithAdapter.TitledListWithAdapter, GenericTitledListContainerWithAdapter<GenericTitledListContainerWithAdapter.TitledListWithAdapter>> noRewardsAdapter;
    private GenericRecyclerViewAdapter<GenericTitledListContainerWithAdapter.TitledListWithAdapter, GenericTitledListContainerWithAdapter<GenericTitledListContainerWithAdapter.TitledListWithAdapter>> unclaimedRewardsAdapter;
    private GenericRecyclerViewAdapter<GenericTitledListContainerWithAdapter.TitledListWithAdapter, GenericTitledListContainerWithAdapter<GenericTitledListContainerWithAdapter.TitledListWithAdapter>> rewardVouchersAdapter;
    private GenericRecyclerViewAdapter<GenericTitledListContainerWithAdapter.TitledListWithAdapter, GenericTitledListContainerWithAdapter<GenericTitledListContainerWithAdapter.TitledListWithAdapter>> participatingPartnersAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_current_rewards, container, false);
    }

    @Override
    public void activityCreated(@Nullable Bundle savedInstanceState) {

        if (getView() == null) {
            return;
        }

        context = getView().getContext();
        recyclerView = getView().findViewById(R.id.main_recyclerview);
        setupRecyclerView();

        swipeRefreshLayout = getView().findViewById(R.id.swip_refresh);
        swipeRefreshLayout.setColorSchemeColors(ViewUtilities.getColorPrimaryFromTheme(getActivity()));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentRewardsListPresenter.onRetry();
            }
        });
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        navigationCoordinator.getActiveRewardsDependencyInjector().inject(this);
    }

    @Override
    protected CurrentRewardsListPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected CurrentRewardsListPresenter getPresenter() {
        return currentRewardsListPresenter;
    }

    public void showRewards(List<UnclaimedRewardDTO> unclaimedRewards, List<RewardVoucherDTO> rewardVouchers) {
        ArrayList<GenericRecyclerViewAdapter> adapters = new ArrayList<>();

        boolean hasUnclaimedRewards = !unclaimedRewards.isEmpty();
        adapters.add(hasUnclaimedRewards ? unclaimedRewardsAdapter : noRewardsAdapter);
        unclaimedRewardsList.setData(unclaimedRewards);

        if (!rewardVouchers.isEmpty()) {
            hideLoadingIndicator();
            rewardVouchersList.setData(rewardVouchers);
            adapters.add(rewardVouchersAdapter);
        } else {
            adapters.add(participatingPartnersAdapter);
            presenter.fetchRewardPartners();
        }

        containersAdapter = new ContainersRecyclerViewAdapter(adapters);
        recyclerView.setAdapter(containersAdapter);
    }

    @Override
    public void showRewardPartners(List<PartnerItemDTO> rewardPartners) {
        rewardPartnersList.setData(rewardPartners);
    }

    @Override
    public void showGenericErrorMessage() {
        showSnackBar(R.string.error_unable_to_load_title_503);
    }

    @Override
    public void showConnectionErrorMessage() {
        showSnackBar(R.string.connectivity_error_alert_title_44);
    }

    @Override
    public void showLoadingIndicator() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoadingIndicator() {
        swipeRefreshLayout.setRefreshing(false);
    }

    private void showSnackBar(int stringId) {
        if (getView() == null) {
            return;
        }
        Snackbar.make(getView(), stringId, Snackbar.LENGTH_LONG)
                .setAction(R.string.AR_connection_error_button_retry_789, getSnackBarRetryListener()).show();
    }

    private View.OnClickListener getSnackBarRetryListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentRewardsListPresenter.onRetry();
            }
        };
    }

    @Override
    public void onUnclaimedRewardClicked(UnclaimedRewardDTO reward) {
        if (reward.rewardId == RewardId._WHEELSPIN) {
            navigationCoordinator.navigateFromRewardsListAfterWheelSpinSelected(getActivity(), reward.uniqueId, reward.rewardId);
        } else if (reward.rewardId == RewardId._CHOOSEREWARD) {
            navigationCoordinator.navigateFromRewardsListAfterChooseRewardSelected(getActivity(), reward.uniqueId, reward.rewardId);
        }
    }

    @Override
    public void onPartnerClicked(PartnerItemDTO partner) {
        Log.d(TAG, "clicked on partner: " + partner.title);
        navigationCoordinator.navigateAfterActiveRewardsParticipatingPartner(getActivity(), (int) partner.id);
    }

    @Override
    public void onRewardVoucherClicked(RewardVoucherDTO reward) {
        Log.d(TAG, "clicked on voucher");
        navigationCoordinator.navigateFromRewardsListOnRewardVoucherTapped(getActivity(), reward);
    }

    @Override
    public void onClicked(MenuItemType menuItemType) {
        Log.d(TAG, "menu item clicked: " + menuItemType.toString());
    }

    private void setupRecyclerView() {
        recyclerView.setAdapter(createAdapter());
    }

    private RecyclerView.Adapter createAdapter() {
        containersAdapter = new ContainersRecyclerViewAdapter(createChildAdapters());
        return containersAdapter;
    }

    private ArrayList<GenericRecyclerViewAdapter> createChildAdapters() {
        ArrayList<GenericRecyclerViewAdapter> adapters = new ArrayList<>();
        adapters.add(createNoAvailableRewardsContainer());
        adapters.add(createUnclaimedRewardsContainer());
        adapters.add(createParticipatingPartnersAdapter());
        adapters.add(createRewardVouchersAdapter());
        return adapters;
    }

    private GenericRecyclerViewAdapter createNoAvailableRewardsContainer() {
        CardMarginSettings settings = new CardMarginSettings();
        settings.showBottomMarginOnly();
        noRewardsAdapter = new GenericRecyclerViewAdapter<>(context,
                new NoRewardAvailableTitledList(getSectionTitle(), getSectionSubtitle(), settings),
                R.layout.active_rewards_titled_recycler_list_container,
                new GenericTitledListContainerWithAdapter.Factory<>());
        return noRewardsAdapter;
    }

    private GenericRecyclerViewAdapter createUnclaimedRewardsContainer() {
        CardMarginSettings settings = new CardMarginSettings();
        settings.showBottomMarginOnly();
        unclaimedRewardsList = new UnclaimedRewardsTitledList(context, getSectionTitle(), insurerConfigRepo.hasRewardChoice(), settings, this);
        unclaimedRewardsAdapter = new GenericRecyclerViewAdapter<>(context,
                unclaimedRewardsList,
                R.layout.active_rewards_titled_recycler_list_container,
                new GenericTitledListContainerWithAdapter.Factory<>());
        return unclaimedRewardsAdapter;
    }

    private GenericRecyclerViewAdapter createRewardVouchersAdapter() {
        CardMarginSettings settings = new CardMarginSettings();
        settings.topMargin = false;
        settings.bottomMargin = false;
        rewardVouchersList = new RewardVouchersTitledList(context, getString(R.string.AR_rewards_useable_rewards_section_title_706), settings, this);
        rewardVouchersAdapter = new GenericRecyclerViewAdapter<>(context,
                rewardVouchersList,
                R.layout.active_rewards_titled_recycler_list_container,
                new GenericTitledListContainerWithAdapter.Factory<>());
        return rewardVouchersAdapter;
    }

    private GenericRecyclerViewAdapter createParticipatingPartnersAdapter() {
        CardMarginSettings settings = new CardMarginSettings();
        settings.topMargin = false;
        settings.bottomMargin = false;
        rewardPartnersList = new ParticipatingPartnersTitledList(context, context.getString(R.string.AR_learn_more_participating_partners_title_696), cmsImageLoader, this, settings);
        participatingPartnersAdapter = new GenericRecyclerViewAdapter<>(context,
                rewardPartnersList,
                R.layout.active_rewards_titled_recycler_list_container,
                new GenericTitledListContainerWithAdapter.Factory<>());
        return participatingPartnersAdapter;
    }

    public String getSectionTitle() {
        if (insurerConfigRepo.hasProbabilisticRewards()) {
            return getString(R.string.AR_rewards_available_spins_section_title_721);
        }
        return getString(R.string.card_action_title_choose_rewards);
    }

    public String getSectionSubtitle() {
        if (insurerConfigRepo.hasProbabilisticRewards()) {
            return getString(R.string.AR_rewards_no_available_spins_cell_title_730);
        }
        return getString(R.string.AR_rewards_no_rewards_choose_title_788);
    }
}
