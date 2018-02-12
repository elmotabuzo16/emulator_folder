package com.vitalityactive.va.vhc;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dto.ProofItemDTO;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.uicomponents.ButtonBarConfigurator;
import com.vitalityactive.va.utilities.ImageLoader;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.vhc.summary.PresentableProof;

import java.util.List;

import javax.inject.Inject;

public class VHCSummaryActivity extends BasePresentedActivity<VHCSummaryPresenter.UserInterface, VHCSummaryPresenter> implements ButtonBarConfigurator.OnClickListener, VHCSummaryPresenter.UserInterface, EventListener<AlertDialogFragment.DismissedEvent> {
    private static final String CONNECTION_SUBMISSION_REQUEST_ERROR_MESSAGE = "CONNECTION_SUBMISSION_REQUEST_ERROR_MESSAGE";
    private static final String GENERIC_SUBMISSION_REQUEST_ERROR_MESSAGE = "GENERIC_SUBMISSION_REQUEST_ERROR_MESSAGE";
    private static int PROOF_ITEM_DIMENSION = 200;
    private static int NUMBER_OF_COLUMNS = 3;

    @Inject
    VHCSummaryPresenter vhcSummaryPresenter;

    @Inject
    EventDispatcher eventDispatcher;

    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.activity_vhc_summary);

        setUpActionBarWithTitle(R.string.summary_screen_summary_title_181)
                .setDisplayHomeAsUpEnabled(true);

        setUpRecyclerView();

        setupButtonBar()
                .setForwardButtonEnabled(true)
                .setForwardButtonTextToConfirm()
                .setForwardButtonOnClick(this);
    }

    @Override
    protected void resume() {
        eventDispatcher.addEventListener(AlertDialogFragment.DismissedEvent.class, this);
    }

    @Override
    protected void pause() {
        eventDispatcher.removeEventListener(AlertDialogFragment.DismissedEvent.class, this);
    }

    private int initialiseProofItemDimensions() {
        PROOF_ITEM_DIMENSION = getRoundedProofItemDimension(getResources().getDisplayMetrics().widthPixels);
        return PROOF_ITEM_DIMENSION;
    }

    private int getRoundedProofItemDimension(int screenWidth) {
        return (int) (getRawProofItemDimension(screenWidth) + 0.5f);
    }

    private float getRawProofItemDimension(int screenWidth) {
        return (screenWidth - getTotalHorizontalMargins()) / NUMBER_OF_COLUMNS;
    }

    private float getTotalHorizontalMargins() {
        return ViewUtilities.pxFromDp(16) * 2 + (NUMBER_OF_COLUMNS - 1) * ViewUtilities.pxFromDp(8);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        navigationCoordinator.getVHCCaptureDependencyInjector().inject(this);
    }

    @Override
    protected VHCSummaryPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected VHCSummaryPresenter getPresenter() {
        return vhcSummaryPresenter;
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(new CapturedGroupAdapter(vhcSummaryPresenter.getCapturedGroups(), vhcSummaryPresenter.getUploadedProof()));
        ViewUtilities.scrollToTop(recyclerView);
    }

    @Override
    public void onButtonBarForwardClicked() {
        vhcSummaryPresenter.onUserConfirmed();
    }

    @Override
    public void navigateAfterUserConfirmed() {
        navigationCoordinator.navigateAfterVHCSummaryConfirmed(this);
    }

    @Override
    public void showConnectionErrorMessage() {
        AlertDialogFragment alert = AlertDialogFragment.create(
                CONNECTION_SUBMISSION_REQUEST_ERROR_MESSAGE,
                getString(R.string.connectivity_error_alert_title_44),
                getString(R.string.connectivity_error_alert_message_45),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43));
        alert.show(getSupportFragmentManager(), CONNECTION_SUBMISSION_REQUEST_ERROR_MESSAGE);
    }

    @Override
    public void showGenericErrorMessage() {
        AlertDialogFragment alert = AlertDialogFragment.create(
                GENERIC_SUBMISSION_REQUEST_ERROR_MESSAGE,
                getString(R.string.privacy_policy_unable_to_complete_alert_title_115),
                getString(R.string.summary_screen_vhc_complete_error_message_187),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43));
        alert.show(getSupportFragmentManager(), GENERIC_SUBMISSION_REQUEST_ERROR_MESSAGE);
    }

    @Override
    public void onEvent(AlertDialogFragment.DismissedEvent event) {
        if (wasTryAgainButtonTapped(event, GENERIC_SUBMISSION_REQUEST_ERROR_MESSAGE) ||
                wasTryAgainButtonTapped(event, CONNECTION_SUBMISSION_REQUEST_ERROR_MESSAGE)) {
            presenter.onUserConfirmed();
        }
    }

    private class CapturedGroupAdapter extends RecyclerView.Adapter {

        private List<PresentableCapturedGroup> capturedGroups;
        private PresentableProof presentableProof;

        private CapturedGroupAdapter(List<PresentableCapturedGroup> capturedGroups, PresentableProof presentableProof) {
            this.capturedGroups = capturedGroups;
            this.presentableProof = presentableProof;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case R.layout.vhc_summary_header:
                    return new RecyclerView.ViewHolder(inflateView(parent, viewType)) {
                    };
                case R.layout.vhc_summary_proof_item:
                    return new ProofViewHolder(inflateView(parent, viewType));
            }
            return new CapturedGroupViewHolder(inflateView(parent, viewType));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (position == 0) {
                return;
            }
            if (position == getItemCount() - 1) {
                ((ProofViewHolder)holder).bindWith(presentableProof);
                return;
            }
            ((CapturedGroupViewHolder)holder).bindWith(capturedGroups.get(position-1));
        }

        @Override
        public int getItemCount() {
            return 2 + capturedGroups.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return R.layout.vhc_summary_header;
            }
            if (position == getItemCount() - 1) {
                return R.layout.vhc_summary_proof_item;
            }
            return R.layout.vhc_summary_captured_group_item;
        }
    }

    private View inflateView(ViewGroup parent, int viewType) {
        return getLayoutInflater().inflate(viewType, parent, false);
    }

    private class CapturedGroupViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final RecyclerView recyclerView;

        CapturedGroupViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            recyclerView = itemView.findViewById(R.id.recycler_view);
        }

        public void bindWith(PresentableCapturedGroup capturedGroup) {
            title.setText(capturedGroup.title);
            recyclerView.setAdapter(new CapturedFieldAdapter(capturedGroup.capturedFields));
            ViewUtilities.addDividers(VHCSummaryActivity.this, recyclerView, ViewUtilities.pxFromDp(16));
        }
    }

    private class CapturedFieldAdapter extends RecyclerView.Adapter {
        private List<PresentableCapturedField> capturedFields;

        private CapturedFieldAdapter(List<PresentableCapturedField> capturedFields) {
            this.capturedFields = capturedFields;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CapturedFieldViewHolder(inflateView(parent, viewType));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((CapturedFieldViewHolder)holder).bindWith(capturedFields.get(position));
        }

        @Override
        public int getItemCount() {
            return capturedFields.size();
        }

        @Override
        public int getItemViewType(int position) {
            return R.layout.vhc_summary_captured_field_item;
        }
    }

    private class CapturedFieldViewHolder extends RecyclerView.ViewHolder {
        private final TextView value;
        private final TextView fieldName;
        private final TextView captureDate;

        CapturedFieldViewHolder(View itemView) {
            super(itemView);
            value = itemView.findViewById(R.id.value);
            fieldName = itemView.findViewById(R.id.fieldName);
            captureDate = itemView.findViewById(R.id.captureDate);
        }

        public void bindWith(PresentableCapturedField capturedField) {
            value.setText(capturedField.value);
            if (TextUtilities.isNullOrWhitespace(capturedField.fieldName)) {
                fieldName.setVisibility(View.GONE);
            } else {
                fieldName.setVisibility(View.VISIBLE);
                fieldName.setText(capturedField.fieldName);
            }
            captureDate.setText(capturedField.captureDate);
        }
    }

    private class ProofViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TableLayout table;

        ProofViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            table = itemView.findViewById(R.id.proof_items_table);
            initialiseProofItemDimensions();
        }

        public void bindWith(PresentableProof presentableProof) {
            title.setText(presentableProof.title);
            table.removeAllViews();
            setUpProofItems(table, presentableProof.proofItemUris, itemView);
        }
    }

    private void setUpProofItems(TableLayout table, List<ProofItemDTO> proofItemUris, View itemView) {
        int i = 0;
        TableRow tableRow = new TableRow(this);
        table.addView(tableRow, new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        for (ProofItemDTO proofItem : proofItemUris) {
            ImageView proofItemImageView = (ImageView)getLayoutInflater().inflate(R.layout.vhc_summary_proof_thumbnail_item, (ViewGroup)itemView, false);
            ImageLoader.loadImageFromUriAndRotateBasedOnExifDataAndCenterInside(Uri.parse(proofItem.getUri()), proofItemImageView);
            TableRow.LayoutParams params = new TableRow.LayoutParams(PROOF_ITEM_DIMENSION, PROOF_ITEM_DIMENSION);
            int margins = ViewUtilities.pxFromDp(4);
            params.setMargins(margins, ViewUtilities.pxFromDp(8), margins, 0);
            proofItemImageView.setLayoutParams(params);
            tableRow.addView(proofItemImageView);
            if (++i == NUMBER_OF_COLUMNS) {
                i = 0;
                tableRow = new TableRow(this);
                table.addView(tableRow, new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            }
        }
    }
}
