package com.vitalityactive.va.partnerjourney;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vitalityactive.va.EmptyStateViewHolder;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.partnerjourney.containers.PartnerContainerViewHolder;
import com.vitalityactive.va.partnerjourney.models.PartnerGroup;
import com.vitalityactive.va.partnerjourney.models.PartnerItem;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.StaticLayoutViewHolder;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PartnerListActivity
        extends BasePartnerActivity<PartnerListPresenterImpl.UserInterface, PartnerListPresenterImpl>
        implements PartnerListPresenterImpl.UserInterface {
    @Inject
    PartnerListPresenterImpl presenter;
    private RecyclerView recyclerView;
    private View emptyStateView;

    @Override
    public void displayServiceItems(List<PartnerGroup> groups) {
        ArrayList<GenericRecyclerViewAdapter> adapters = createAdapters(groups);
        recyclerView.setAdapter(new ContainersRecyclerViewAdapter(adapters));
        ViewUtilities.scrollToTop(recyclerView);
        ViewUtilities.swapVisibility(recyclerView, emptyStateView);
    }

    @Override
    public void hideLoadingIndicator() {
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
    }

    @Override
    public void showLoadingIndicator() {
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
    }

    @Override
    public void showConnectionError() {
        showEmptyStateViewHolder(R.string.connectivity_error_alert_title_44, R.string.connectivity_error_alert_message_45);
    }

    @Override
    public void showGenericError() {
        showEmptyStateViewHolder(R.string.error_unable_to_load_title_503, R.string.error_unable_to_load_message_504);
    }

    @Override
    public void navigateToPartnerDetails(PartnerItem item) {
        navigationCoordinator.navigateAfterPartnerTappedInPartnerList(PartnerListActivity.this, getPartnerType(), item.id);
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.activity_health_services);
        recyclerView = findViewById(R.id.health_service_items);
        emptyStateView = findViewById(R.id.empty_state);
        presenter.setPartnerType(getPartnerType());
        setUpActionBarWithTitle(getScreenTitle())
                .setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        navigationCoordinator.getPartnerJourneyDependencyInjector().inject(this);
    }

    @Override
    protected PartnerListPresenterImpl.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected PartnerListPresenterImpl getPresenter() {
        return presenter;
    }

    @NonNull
    private EmptyStateViewHolder showEmptyStateViewHolder(int titleId, int messageId) {
        EmptyStateViewHolder emptyStateViewHolder = new EmptyStateViewHolder(emptyStateView);

        emptyStateViewHolder
                .setup(titleId, messageId, R.string.try_again_button_title_43)
                .setButtonClickListener(new EmptyStateViewHolder.EmptyStatusButtonClickedListener() {
                    @Override
                    public void onEmptyStateButtonClicked(EmptyStateViewHolder emptyStateViewHolder) {
                        presenter.fetchPartnerItems();
                    }
                })
                .showEmptyStateViewAndHideOtherView(recyclerView);

        return emptyStateViewHolder;
    }

    @NonNull
    private ArrayList<GenericRecyclerViewAdapter> createAdapters(List<PartnerGroup> groups) {
        ArrayList<GenericRecyclerViewAdapter> adapters = new ArrayList<>();
        createHeaderAdapter(adapters);
        for (PartnerGroup group : groups) {
            GenericRecyclerViewAdapter adapter = getHealthServiceItemContainerAdapter(group);
            adapters.add(adapter);
        }
        return adapters;
    }

    private GenericRecyclerViewAdapter getHealthServiceItemContainerAdapter(PartnerGroup group) {
        return new GenericRecyclerViewAdapter<>(this,
                group,
                R.layout.partnerjourney_health_service_item_container,
                new PartnerContainerViewHolder.Factory(presenter));
    }

    private void createHeaderAdapter(ArrayList<GenericRecyclerViewAdapter> adapters) {
        adapters.add(StaticLayoutViewHolder.buildAdapter(this, getPartnerType().listHeaderLayout));
    }
}
