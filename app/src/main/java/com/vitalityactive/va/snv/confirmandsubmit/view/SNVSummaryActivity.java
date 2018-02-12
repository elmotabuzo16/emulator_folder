package com.vitalityactive.va.snv.confirmandsubmit.view;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dto.ProofItemDTO;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.snv.confirmandsubmit.presenter.SNVSummaryPresenter;
import com.vitalityactive.va.snv.confirmandsubmit.viewholder.SNVProofItemSummaryAdapter;
import com.vitalityactive.va.snv.confirmandsubmit.viewholder.SNVSummaryAdapter;
import com.vitalityactive.va.snv.dto.ConfirmAndSubmitItemDTO;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.uicomponents.ButtonBarConfigurator;

import java.util.List;

import javax.inject.Inject;

public class SNVSummaryActivity extends BasePresentedActivity<SNVSummaryPresenter.UserInterface, SNVSummaryPresenter<SNVSummaryPresenter.UserInterface>>
        implements ButtonBarConfigurator.OnClickListener, SNVSummaryPresenter.UserInterface, EventListener<AlertDialogFragment.DismissedEvent>,
        SNVSummaryAdapter.Callback, SNVProofItemSummaryAdapter.Callback{

    @Inject
    SNVSummaryPresenter snvSummaryPresenter;

    @Inject
    EventDispatcher eventDispatcher;

    RecyclerView screeningsRecyclerView;
    RecyclerView vaccinationsRecyclerView;
    SNVSummaryAdapter screeningAdapter;
    SNVSummaryAdapter vaccinationAdapter;
    SNVProofItemSummaryAdapter proofItemSummaryAdapter;

    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.activity_snv_summary_confirm_and_submit);

        setUpActionBarWithTitle(R.string.summary_screen_summary_title_181)
                .setDisplayHomeAsUpEnabled(true);

        setupRecyclerView();

        setupProofItemTable();

        setupButtonBar()
                .setForwardButtonEnabled(true)
                .setForwardButtonTextToConfirm()
                .setForwardButtonOnClick(this);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    private void setupRecyclerView() {
        screeningsRecyclerView = findViewById(R.id.summary_recycler_screening);
        screeningsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        screeningsRecyclerView.setNestedScrollingEnabled(false);

        vaccinationsRecyclerView = findViewById(R.id.summary_recycler_vaccination);
        vaccinationsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        vaccinationsRecyclerView.setNestedScrollingEnabled(false);
    }

    private void setupProofItemTable() {
        proofItemSummaryAdapter = new SNVProofItemSummaryAdapter(this);
    }

    @Override
    public void updateListItems(final List<ConfirmAndSubmitItemDTO> screeningsDTO, final List<ConfirmAndSubmitItemDTO> vaccinationsDTO, final List<ProofItemDTO> proofItemDTOS) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                screeningAdapter = new SNVSummaryAdapter(SNVSummaryActivity.this, screeningsDTO, SNVSummaryAdapter.TYPE.SCREENINGS);
                screeningsRecyclerView = findViewById(R.id.summary_recycler_screening);
                screeningsRecyclerView.setAdapter(screeningAdapter);
                vaccinationAdapter = new SNVSummaryAdapter(SNVSummaryActivity.this, vaccinationsDTO, SNVSummaryAdapter.TYPE.VACCINATIONS);
                vaccinationsRecyclerView = findViewById(R.id.summary_recycler_vaccination);
                vaccinationsRecyclerView.setAdapter(vaccinationAdapter);

                adjustRecyclerViewHeight(SNVSummaryAdapter.TYPE.SCREENINGS, 0);
                adjustRecyclerViewHeight(SNVSummaryAdapter.TYPE.VACCINATIONS, 0);

                screeningsRecyclerView.refreshDrawableState();
                screeningsRecyclerView.invalidate();
                vaccinationsRecyclerView.refreshDrawableState();
                vaccinationsRecyclerView.invalidate();

                proofItemSummaryAdapter.setUpProofItems(proofItemDTOS);
            }
        });
    }

    @Override
    public void navigateAfterUserConfirmed() {
        navigationCoordinator.navigateAfterSNVSummaryConfirmed(this);
    }

    @Override
    public void showConnectionErrorMessage() {

    }

    @Override
    public void showGenericErrorMessage() {

    }

    @Override
    public void adjustRecyclerViewHeight(SNVSummaryAdapter.TYPE type, int numItemsChecked) {
        if (type == SNVSummaryAdapter.TYPE.SCREENINGS) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) screeningsRecyclerView.getLayoutParams();
            params.height = screeningAdapter.getItemCount() * getResources().getDimensionPixelSize(R.dimen.item_divider_inset);
            screeningsRecyclerView.setLayoutParams(params);
        } else if (type == SNVSummaryAdapter.TYPE.VACCINATIONS) {
            LinearLayout.LayoutParams paramsVaccination = (LinearLayout.LayoutParams) vaccinationsRecyclerView.getLayoutParams();
            paramsVaccination.height = vaccinationAdapter.getItemCount() * getResources().getDimensionPixelSize(R.dimen.item_divider_inset);
            vaccinationsRecyclerView.setLayoutParams(paramsVaccination);
        }
    }

    @Override
    public void onButtonBarForwardClicked() {
        snvSummaryPresenter.onUserConfirmed();
    }

    @Override
    public LayoutInflater getLayoutInflaterForAdapter() {
        return getLayoutInflater();
    }

    @Override
    public TableRow getNewTableRow() {
        return new TableRow(getApplicationContext());
    }

    @Override
    public ImageView getNewImageView() {
        return (ImageView) getLayoutInflater().inflate(R.layout.vhc_summary_proof_thumbnail_item, null, false);
    }

    @Override
    public Resources getResourcesForTable() {
        return getResources();
    }

    @Override
    public View findViewByIdInActivity(@IdRes int resId) {
        return findViewById(resId);
    }

    @Override
    protected SNVSummaryPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected SNVSummaryPresenter getPresenter() {
        return snvSummaryPresenter;
    }

    @Override
    public void onEvent(AlertDialogFragment.DismissedEvent event) {

    }
}
