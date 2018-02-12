package com.vitalityactive.va.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.vitalityactive.va.BasePresentedFragment;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.utilities.ImageLoader;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.utilities.photohandler.CameraGalleryInvoker;
import com.vitalityactive.va.utilities.photohandler.CameraGalleryUtility;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

public abstract class BasePersonalDetailsFragment extends BasePresentedFragment<PersonalDetailsPresenter.UI, PersonalDetailsPresenter>
        implements PersonalDetailsPresenter.UI {

    @Inject
    PersonalDetailsPresenter presenter;
    @Inject
    CameraGalleryInvoker photoInvoker;

    private TextView personalEmailView;
    private TextView profileNameView;
    private TextView personalMobileView;
    private TextView personalDOBView;
    private TextView personalGenderView;

    protected TextView profileEditView;
    protected LinearLayout personalMobileContainer;
    protected LinearLayout personalEmailContainer;
    protected TextView profileInitialsView;
    protected ImageView profileImageView;
    protected View parentView;
    protected String currentEntityNumber;

    protected ImageView personalEntityIcon;
    protected TextView personalEntityLabel;
    protected TextView personalEntityView;


    String contextFileDirectory;

    private String globalTintColor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personal_details, container, false);
    }

    @Override
    protected void activityCreated(@Nullable Bundle savedInstanceState) {
        globalTintColor = getArguments().getString(GLOBAL_TINT_COLOR);
        parentView = getView();
        if (parentView == null) {
            return;
        }
        setUpDetailsView(parentView);
    }

    @Override
    protected void appear() {
        super.appear();
        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(getResources().getString(R.string.Settings_profile_landing_personal_details_title_912));
        }
    }

    @Override
    public void activityDestroyed() {
        photoInvoker.finish();
    }

    private void setUpDetailsView(@NonNull View parentView) {
        profileImageView = parentView.findViewById(R.id.profile_image);
        profileInitialsView = parentView.findViewById(R.id.profile_initials);
        profileNameView = parentView.findViewById(R.id.profile_name);
        personalMobileView = parentView.findViewById(R.id.personal_mobile);
        personalEmailView = parentView.findViewById(R.id.personal_email);
        personalDOBView = parentView.findViewById(R.id.personal_date_of_birth);
        personalGenderView = parentView.findViewById(R.id.personal_gender);
        personalMobileContainer = parentView.findViewById(R.id.personal_mobile_container);
        personalEmailContainer = parentView.findViewById(R.id.email_layout);
        profileEditView = parentView.findViewById(R.id.profile_edit);

        personalEmailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((PersonalDetailsActivity) getActivity()).replaceContentWithChangeEmail();
            }
        });

//        profileInitialsView.setOnClickListener(editProfileImageOnClick);
        profileImageView.setOnClickListener(editProfileImageOnClick);
        profileEditView.setOnClickListener(editProfileImageOnClick);

        profileEditView.setTextColor(Color.parseColor(globalTintColor));
        photoInvoker.setFragmentCallback(this);
        photoInvoker.setCapturedImageDetails("personal_details", "photo");
        contextFileDirectory = getActivity().getFilesDir().toString();
    }

    private View.OnClickListener editProfileImageOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            editProfileImage();
        }
    };

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected PersonalDetailsPresenter.UI getUserInterface() {
        return this;
    }

    @Override
    protected PersonalDetailsPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void showProfileInfo(String givenName, String familyName, String dateOfBirth, String gender, String mobileNumber, String emailAddress, String insurerEmail, String entityNumber) {
        profileNameView.setText(getString(R.string.navigation_drawer_header_title, givenName, familyName));
        personalMobileView.setText(mobileNumber);
        personalEmailView.setText(emailAddress);
        personalDOBView.setText(dateOfBirth);
        personalGenderView.setText(gender);
        currentEntityNumber = entityNumber;
        marketUIUpdate();
    }

    @Override
    public void showProfileImage(String profileImagePath) {
        ImageLoader.loadImageFromUriRoundedAndCenterCrop(Uri.parse("file:" + profileImagePath), profileImageView);
        profileImageView.setVisibility(View.VISIBLE);
        profileInitialsView.setVisibility(View.GONE);
    }

    @Override
    public void showProfileInitials(String userInitials) {
        profileImageView.setVisibility(View.GONE);
        profileInitialsView.setVisibility(View.VISIBLE);
        profileInitialsView.setText(userInitials);
    }

    public void editProfileImage() {
        AlertDialog.Builder choosePictureDialogBuilder = new AlertDialog.Builder(getActivity());
        @SuppressLint("InflateParams") View dialogView = getLayoutInflater().inflate(R.layout.select_dialog, null, false);

        TextView cameraOption = dialogView.findViewById(R.id.text1);
        TextView galleryOption = dialogView.findViewById(R.id.text2);

        Drawable photoDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.take_photo_40);
        Drawable galleryDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.choose_gallery_40);

        if (!TextUtils.isEmpty(globalTintColor) && !TextUtils.isEmpty(globalTintColor.trim())) {
            photoDrawable = ViewUtilities.tintDrawable(photoDrawable, Color.parseColor(globalTintColor));
            galleryDrawable = ViewUtilities.tintDrawable(galleryDrawable, Color.parseColor(globalTintColor));
        }

        cameraOption.setCompoundDrawablesWithIntrinsicBounds(photoDrawable, null, null, null);
        galleryOption.setCompoundDrawablesWithIntrinsicBounds(galleryDrawable, null, null, null);


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

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CameraGalleryUtility.IMAGE_SOURCE_CAMERA:
                    Uri capturedImageUri = photoInvoker.getCapturedImageUri();
                    performCrop(capturedImageUri);
                    break;

                case CameraGalleryUtility.IMAGE_SOURCE_GALLERY:
                    Uri selectedImageUri = data.getData();
                    performCrop(selectedImageUri);
                    break;

                case CameraGalleryUtility.IMAGE_CROP:
                    Bitmap croppedImage = data.getExtras().getParcelable("data");
                    // Do not use for now. Image photo is only saved locally
                    //Uri finalImageUri = photoInvoker.getCroppedImageUri(croppedImage);

                    boolean result = photoInvoker.saveToInternalStorageByBitmap(croppedImage);
                    if (result) {
                        presenter.addProfilePhoto(null, null, contextFileDirectory, null);
                    }
                    break;

                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    Uri croppedUri = CropImage.getActivityResult(data).getUri();
                    boolean saveResult = photoInvoker.saveToInternalStorageByUri(croppedUri);
                    if (saveResult) {
                        presenter.addProfilePhoto(null, null, contextFileDirectory, null);
                    }
                    break;

                default:
                    break;
            }
        }
    }

    private InputStream getInputStreamFromFile(Uri uri) {
        try {
            return getActivity().getContentResolver().openInputStream(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void performCrop(Uri uri) {
        CropImage.activity(uri)
                .start(getContext(), this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (wasPermissionsRequestCancelled(permissions, grantResults)) {
            return;
        }

        if (wasPermissionGranted(grantResults)) {
            photoInvoker.start();
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

    protected abstract void marketUIUpdate();
}
