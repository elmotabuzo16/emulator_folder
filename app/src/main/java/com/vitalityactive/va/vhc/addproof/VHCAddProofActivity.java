package com.vitalityactive.va.vhc.addproof;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.EmptyStateViewHolder;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dto.ProofItemDTO;
import com.vitalityactive.va.uicomponents.ButtonBarConfigurator;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.List;

import javax.inject.Inject;

public class VHCAddProofActivity extends BasePresentedActivity<VHCAddProofPresenter.UserInterface, VHCAddProofPresenter> implements ButtonBarConfigurator.OnClickListener, VHCAddProofPresenter.UserInterface {
    private static final String SELECTED_CHOOSE_PICTURE_DIALOG_OPTION = "SELECTED_CHOOSE_PICTURE_DIALOG_OPTION";
    private static final String CAPTURE_IMAGE_OUTPUT_URI = "CAPTURE_IMAGE_OUTPUT_URI";
    private static final int NUMBER_OF_COLUMNS = 2;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int IMAGE_SOURCE_CAMERA = 0;
    private static final int IMAGE_SOURCE_GALLERY = 1;
    @Inject
    VHCAddProofPresenter addProofPresenter;
    private EmptyStateViewHolder emptyState;
    private GridView gridView;
    private CompositeGridViewAdapter compositeGridViewAdapter;
    private int selectedChoosePictureDialogOption = -1;
    private Uri captureImageOutputUri;
    private TextView imageCount;

    private static void grantWritePermission(@NonNull Context context, Intent intent, Uri uri) {
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    private static void revokeWritePermission(@NonNull Context context, Uri uri) {
        context.revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
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

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.screenings_camera_green);
        @ColorInt int color = ResourcesCompat.getColor(getResources(), R.color.jungle_green, getTheme());
        emptyState = new EmptyStateViewHolder(findViewById(R.id.vhc_add_proof_empty_state))
                .setImage(ViewUtilities.tintDrawable(drawable, color), getString(R.string.vhc_add_proof_empty_state_icon_content_description))
                .setup(R.string.proof_add_proof_empty_title_164, R.string.proof_add_proof_empty_message_165, R.string.proof_add_button_166, new EmptyStateViewHolder.EmptyStatusButtonClickedListener() {
                    @Override
                    public void onEmptyStateButtonClicked(EmptyStateViewHolder emptyStateViewHolder) {
                        onVHCAddProofTapped();
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

        if (savedInstanceState != null) {
            selectedChoosePictureDialogOption = savedInstanceState.getInt(SELECTED_CHOOSE_PICTURE_DIALOG_OPTION);
            captureImageOutputUri = savedInstanceState.getParcelable(CAPTURE_IMAGE_OUTPUT_URI);
        }
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

    @NonNull
    private Intent getProofItemDetailIntent(int position) {
        Intent intent = new Intent(VHCAddProofActivity.this, VHCProofItemDetailActivity.class);
        intent.putExtra(VHCProofItemDetailActivity.PROOF_ITEM_URI, compositeGridViewAdapter.getImageItemProofItem(position));
        return intent;
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        navigationCoordinator.getVHCCaptureDependencyInjector().inject(this);
    }

    @Override
    protected VHCAddProofPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected VHCAddProofPresenter getPresenter() {
        return addProofPresenter;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_CHOOSE_PICTURE_DIALOG_OPTION, selectedChoosePictureDialogOption);
        if (captureImageOutputUri != null) {
            outState.putParcelable(CAPTURE_IMAGE_OUTPUT_URI, captureImageOutputUri);
        }
    }

    @Override
    public void onVHCAddProofTapped() {
        final AlertDialog.Builder choosePictureDialogBuilder = new AlertDialog.Builder(this);

        @SuppressLint("InflateParams") View dialogView = getLayoutInflater().inflate(R.layout.select_dialog, null, false);
        choosePictureDialogBuilder.setView(dialogView);
        AlertDialog dialog = choosePictureDialogBuilder.create();
        setChoosePictureDialogOptionOnClickListener(dialogView, R.id.text1, IMAGE_SOURCE_CAMERA, dialog);
        setChoosePictureDialogOptionOnClickListener(dialogView, R.id.text2, IMAGE_SOURCE_GALLERY, dialog);

        dialog.show();
    }

    @Override
    public void showProofItemDetail(int position) {
        startActivity(getProofItemDetailIntent(position));
    }

    private void setChoosePictureDialogOptionOnClickListener(final View dialogView, int resourceId, final int imageSource, final AlertDialog dialog) {
        dialogView.findViewById(resourceId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture(imageSource);
                dialog.dismiss();
            }
        });
    }

    private void choosePicture(int imageSource) {
        selectedChoosePictureDialogOption = imageSource;
        choosePicture();
    }

    private void choosePicture() {
        switch (selectedChoosePictureDialogOption) {
            case IMAGE_SOURCE_CAMERA:
                if (isLollipopOrBelow() || (isPermissionGranted(Manifest.permission.CAMERA) && isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE) && isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
                    captureImageOutputUri = ProofImageFetcher.getCaptureImageOutputUri(VHCAddProofActivity.this);
                    Intent takePictureIntent = getTakePictureIntent(captureImageOutputUri);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, IMAGE_SOURCE_CAMERA);
                    }
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_IMAGE_CAPTURE);
                }
                break;
            case IMAGE_SOURCE_GALLERY: {
                if (isLollipopOrBelow() || isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Intent pickImageIntent = getPickImageIntent();
                    startActivityForResult(pickImageIntent, IMAGE_SOURCE_GALLERY);
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_READ_EXTERNAL_STORAGE);
                }
                break;
            }
        }
    }

    @NonNull
    private Intent getTakePictureIntent(Uri captureImageOutputUri) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, captureImageOutputUri);
        grantWritePermission(VHCAddProofActivity.this, takePictureIntent, captureImageOutputUri);
        return takePictureIntent;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isPermissionGranted(String permission) {
        return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isLollipopOrBelow() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (wasPermissionsRequestCancelled(permissions, grantResults)) {
            return;
        }

        if (wasPermissionGranted(grantResults)) {
            choosePicture();
        }

        selectedChoosePictureDialogOption = -1;
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

    @NonNull
    private Intent getPickImageIntent() {
        Intent pickImageIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        pickImageIntent.setType("image/*");
        pickImageIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        return pickImageIntent;
    }

    public void onSelectTapped(MenuItem item) {
        compositeGridViewAdapter.enterSelectionMode();
    }

    @Override
    public void onButtonBarForwardClicked() {
        navigationCoordinator.navigateAfterVHCProofAdded(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IMAGE_SOURCE_GALLERY:
                    ClipData clipData = imageReturnedIntent.getClipData();

                    if (clipData != null) {
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            addProofItemFromGallery(clipData.getItemAt(i).getUri());
                        }
                    } else {
                        addProofItemFromGallery(imageReturnedIntent.getData());
                    }
                    break;
                case IMAGE_SOURCE_CAMERA:
                    addProofItemFromCamera(captureImageOutputUri);
                    revokeWritePermission(this, captureImageOutputUri);
                    break;
            }
        }
    }

    private void addProofItemFromGallery(Uri uri) {
        // Persist permission so our access to the uri survives a device restart
        if (!uri.getPath().equals("/external_files/tmp.png"))
        {
            getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION & Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

        addProofItemFromUri(uri);
    }

    private void addProofItemFromCamera(Uri uri) {
        addProofItemFromUri(uri);
    }

    private void addProofItemFromUri(Uri uri) {
        ProofItemDTO proofItem = getPresenter().addProofItemFromUri(uri.toString());
        if (proofItem != null) {
            compositeGridViewAdapter.addImageItem(proofItem);
        }
        setupButtonBar().setForwardButtonEnabled(isShowingProofItems());
        refreshEmptyStateVisibility();
    }

    private boolean isShowingProofItems() {
        return compositeGridViewAdapter.getCount() > 0;
    }

    private void createAddProofButtonAsLastItem() {
        compositeGridViewAdapter.createAddImageButtonAsLastItem();
        gridView.setAdapter(compositeGridViewAdapter);
    }

    private void refreshEmptyStateVisibility() {
        emptyState.hideEmptyStateAndShowOtherIfHasData(isShowingProofItems(), gridView);
    }

    @Override
    public void showProofItems(List<ProofItemDTO> proofItemUris) {
        compositeGridViewAdapter.clearList();
        for (ProofItemDTO proofItem : proofItemUris) {
            compositeGridViewAdapter.addImageItem(proofItem);
        }
        gridView.setAdapter(compositeGridViewAdapter);
        setupButtonBar().setForwardButtonEnabled(isShowingProofItems());
        refreshEmptyStateVisibility();
    }

    @Override
    public void updateProofItemCount(int numberOfProofItems) {
        if (numberOfProofItems == 0) {
            imageCount.setVisibility(View.GONE);
            imageCount.setText("");
        } else {
            imageCount.setVisibility(View.VISIBLE);
            imageCount.setText(getString(R.string.proof_attachments_footnote_message_177, numberOfProofItems, 11));
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

}
