package com.vitalityactive.va.snv.confirmandsubmit.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.EmptyStateViewHolder;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dto.ProofItemDTO;
import com.vitalityactive.va.snv.confirmandsubmit.presenter.SNVAddProofPresenter;
import com.vitalityactive.va.uicomponents.ButtonBarConfigurator;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.utilities.photohandler.CameraGalleryInvoker;
import com.vitalityactive.va.utilities.photohandler.CameraGalleryUtility;
import com.vitalityactive.va.vhc.addproof.CompositeGridViewAdapter;
import com.vitalityactive.va.vhc.addproof.VHCProofItemDetailActivity;

import java.util.List;

import javax.inject.Inject;

public class SNVAddProofActivity extends BasePresentedActivity<SNVAddProofPresenter.UserInterface, SNVAddProofPresenter> implements ButtonBarConfigurator.OnClickListener, SNVAddProofPresenter.UserInterface {

    private static final int NUMBER_OF_COLUMNS = 2;

    @Inject
    SNVAddProofPresenter addProofPresenter;
    @Inject
    CameraGalleryInvoker photoInvoker;

    private EmptyStateViewHolder emptyState;
    private GridView gridView;
    private CompositeGridViewAdapter compositeGridViewAdapter;
    private TextView imageCount;
    private int itemsSelected;

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.activity_add_proof);

        setUpActionBarWithTitle(R.string.proof_add_proof_screen_title_163)
                .setDisplayHomeAsUpEnabled(true);

        setupButtonBar().setForwardButtonTextToNext()
                .setForwardButtonEnabled(false)
                .setForwardButtonOnClick(this);

        setupUi();
    }

    private void setupUi() {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.screenings_camera_green);
        @ColorInt int color = ResourcesCompat.getColor(getResources(), R.color.jungle_green, getTheme());
        emptyState = new EmptyStateViewHolder(findViewById(R.id.vhc_add_proof_empty_state))
                .setImage(ViewUtilities.tintDrawable(drawable, color), getString(R.string.vhc_add_proof_empty_state_icon_content_description))
                .setup(R.string.proof_add_proof_empty_title_164, R.string.SV_add_proof_empty_message_1019, R.string.proof_add_button_166, new EmptyStateViewHolder.EmptyStatusButtonClickedListener() {
                    @Override
                    public void onEmptyStateButtonClicked(EmptyStateViewHolder emptyStateViewHolder) {
                        onSNVAddProofTapped();
                    }
                });

        imageCount = findViewById(R.id.image_count);

        gridView = findViewById(R.id.vhc_image_grid_view);

        compositeGridViewAdapter = new CompositeGridViewAdapter(this, R.layout.vhc_grid_item, R.layout.vhc_grid_item_add_button, initialiseProofItemDimensions());

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.onUserSelectsItem(position);
            }
        });

        photoInvoker.setActivityCallBack(this);
        photoInvoker.setCapturedImageDetails("snv_add_proof", "photo");
    }

    private int initialiseProofItemDimensions() {
        return getRoundedProofItemDimension(getResources().getDisplayMetrics().widthPixels);
    }

    private int getRoundedProofItemDimension(int screenWidth) {
        return (int) (getRawProofItemDimension(screenWidth) + 0.5f);
    }

    private float getRawProofItemDimension(int screenWidth) {
        return (screenWidth - getTotalHorizontalMargins()) / NUMBER_OF_COLUMNS;
    }

    private float getTotalHorizontalMargins() {
        return ViewUtilities.pxFromDp(8) * 3.0f;
    }

    @Override
    protected SNVAddProofPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected SNVAddProofPresenter getPresenter() {
        return addProofPresenter;
    }

    @Override
    public void onButtonBarForwardClicked() {
        navigationCoordinator.navigateAfterSNVProofAdded(this);
    }

    @Override
    public void showProofItems(List<ProofItemDTO> proofItemUris) {
        compositeGridViewAdapter.clearList();
        for (ProofItemDTO proofItem : proofItemUris) {
            compositeGridViewAdapter.addImageItem(proofItem);
        }
        gridView.setAdapter(compositeGridViewAdapter);
        setupButtonBar().setForwardButtonEnabled(isShowingEnoughProofItems());
        refreshEmptyStateVisibility();
    }

    private void refreshEmptyStateVisibility() {
        emptyState.hideEmptyStateAndShowOtherIfHasData(isShowingProofItems(), gridView);
    }

    private boolean isShowingProofItems() {
        return compositeGridViewAdapter.getCount() > 0;
    }

    private boolean isShowingEnoughProofItems() {
        itemsSelected = addProofPresenter.getNumberOfItemsSelected();
        int itemsProof = compositeGridViewAdapter.getCount();

        return itemsProof > 0 && itemsProof <= itemsSelected;
    }

    @Override
    public void updateProofItemCount(int numberOfProofItems) {
        if (numberOfProofItems == 0) {
            imageCount.setVisibility(View.GONE);
            imageCount.setText("");
        } else {
            imageCount.setVisibility(View.VISIBLE);
            imageCount.setText(getString(R.string.proof_attachments_footnote_message_177, numberOfProofItems, itemsSelected));
        }
    }

    @Override
    public void hideAddProofButton() {
        removeAddProofButtonAsLastItem();
    }

    private void removeAddProofButtonAsLastItem() {
        compositeGridViewAdapter.removeAddProofButtonAsLastItem();
        gridView.setAdapter(compositeGridViewAdapter);
    }

    @Override
    public void showAddProofButton() {
        createAddProofButtonAsLastItem();
    }

    private void createAddProofButtonAsLastItem() {
        compositeGridViewAdapter.createAddImageButtonAsLastItem();
        gridView.setAdapter(compositeGridViewAdapter);
    }

    @Override
    public void onSNVAddProofTapped() {
        AlertDialog.Builder choosePictureDialogBuilder = new AlertDialog.Builder(this);
        @SuppressLint("InflateParams") View dialogView = getLayoutInflater().inflate(R.layout.select_dialog, null, false);

        TextView cameraOption = dialogView.findViewById(R.id.text1);
        TextView galleryOption = dialogView.findViewById(R.id.text2);
        cameraOption.setCompoundDrawablesWithIntrinsicBounds(R.drawable.takephoto, 0, 0, 0);
        galleryOption.setCompoundDrawablesWithIntrinsicBounds(R.drawable.choosegallery, 0, 0, 0);

        choosePictureDialogBuilder.setView(dialogView);
        AlertDialog dialog = choosePictureDialogBuilder.create();

        setChoosePictureDialogOptionOnClickListener(dialogView, R.id.text1, CameraGalleryInvoker.ImageSource.CAMERA, dialog);
        setChoosePictureDialogOptionOnClickListener(dialogView, R.id.text2, CameraGalleryInvoker.ImageSource.GALLERY, dialog);

        dialog.show();
    }

    private void setChoosePictureDialogOptionOnClickListener(final View dialogView, int resourceId, final CameraGalleryInvoker.ImageSource imageSource, final AlertDialog dialog) {
        dialogView.findViewById(resourceId).setOnClickListener(v -> {
            photoInvoker.setInvokerMode(imageSource);
            photoInvoker.start();
            dialog.dismiss();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CameraGalleryUtility.IMAGE_SOURCE_CAMERA:
                    Uri capturedImageUri = photoInvoker.getCapturedImageUri();
                    if (capturedImageUri != null) {
                        addProofItemFromUri(capturedImageUri);
                    }
                    break;

                case CameraGalleryUtility.IMAGE_SOURCE_GALLERY:
                    ClipData clipData = data.getClipData();

                    if (clipData != null) {
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            addProofItemFromUri(clipData.getItemAt(i).getUri());
                        }
                        break;
                    }

                    Uri selectedImageUri = data.getData();
                    if (selectedImageUri != null) {
                        addProofItemFromUri(selectedImageUri);
                    }
                    break;

                default:
                    break;
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (wasPermissionsRequestCancelled(permissions, grantResults)) {
            photoInvoker.showStartCameraError();
        }

        if (wasPermissionGranted(grantResults)) {
            photoInvoker.start();
        } else {
            photoInvoker.showStartCameraError();
        }
    }

    private boolean wasPermissionGranted(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private boolean wasPermissionsRequestCancelled(@NonNull String[] permissions, @NonNull int[] grantResults) {
        return permissions.length == 0 && grantResults.length == 0;
    }

    private void addProofItemFromUri(Uri uri) {
        ProofItemDTO proofItem = getPresenter().addProofItemFromUri(uri.toString());
        if (proofItem != null) {
            compositeGridViewAdapter.addImageItem(proofItem);
        }
        setupButtonBar().setForwardButtonEnabled(isShowingEnoughProofItems());
        refreshEmptyStateVisibility();
    }

    @Override
    public void showProofItemDetail(int position) {
        startActivity(getProofItemDetailIntent(position));
    }

    @NonNull
    private Intent getProofItemDetailIntent(int position) {
        Intent intent = new Intent(SNVAddProofActivity.this, SNVProofItemDetailActivity.class);
        intent.putExtra(VHCProofItemDetailActivity.PROOF_ITEM_URI, compositeGridViewAdapter.getImageItemProofItem(position));
        return intent;
    }

    @Override
    protected void destroy() {
        super.destroy();
        photoInvoker.finish();
        photoInvoker = null;
    }
}
