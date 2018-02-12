package com.vitalityactive.va.snv.confirmandsubmit.view;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dto.ProofItemDTO;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.snv.confirmandsubmit.interactor.ConfirmAndSubmitInteractor;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.utilities.ImageLoader;

import javax.inject.Inject;

public class SNVProofItemDetailActivity extends BaseActivity implements EventListener<AlertDialogFragment.DismissedEvent> {
    public static final String PROOF_ITEM_URI = "PROOF_ITEM_URI";
    private static final String IMAGE_DELETE_CONFIRMATION = "IMAGE_DELETE_CONFIRMATION";

    @Inject
    ConfirmAndSubmitInteractor interactor;
    @Inject
    EventDispatcher eventDispatcher;

    private ProofItemDTO proofItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vhc_proof_item_detail);

        getDependencyInjector().inject(this);

        if (showBitmap(initialiseProofItemFromIntentAndGetUri())) {
            hideActionBarTitleAndShowHomeButton();
        } else {
            finish();
        }
    }

    private boolean showBitmap(@Nullable String uri) {
        if (uri == null) {
            return false;
        }

        ImageView imageView = findViewById(R.id.proof_item_image);
        ImageLoader.loadImageFromUriAndRotateBasedOnExifDataAndCenterInside(Uri.parse(uri), imageView);
        return true;
    }

    @Nullable
    private String initialiseProofItemFromIntentAndGetUri() {
        proofItem = getIntent().getParcelableExtra(PROOF_ITEM_URI);
        return proofItem.getUri();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trash_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_trash:
                showConfirmationDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showConfirmationDialog() {
        AlertDialogFragment.create(IMAGE_DELETE_CONFIRMATION,
                getString(R.string.proof_remove_title_273),
                getString(R.string.proof_remove_message_274),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.proof_remove_button_275))
                .show(getSupportFragmentManager(), IMAGE_DELETE_CONFIRMATION);
    }

    @Override
    public void onEvent(AlertDialogFragment.DismissedEvent event) {
        if (event.getClickedButtonType().equals(AlertDialogFragment.DismissedEvent.ClickedButtonType.Positive)) {
            interactor.removeProofItem(proofItem);
            finish();
        }
    }

    private void hideActionBarTitleAndShowHomeButton() {
        ActionBar actionBar = setUpActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventDispatcher.addEventListener(AlertDialogFragment.DismissedEvent.class, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        eventDispatcher.removeEventListener(AlertDialogFragment.DismissedEvent.class, this);
    }
}
